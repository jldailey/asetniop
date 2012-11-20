/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.asetniop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.softkeyboard.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

public class SoftKeyboard extends InputMethodService {
    static final boolean DEBUG = false;
    static final int A_KEY = 1;
    static final int S_KEY = 2;
    static final int E_KEY = 4;
    static final int T_KEY = 8;
    static final int N_KEY = 16;
    static final int I_KEY = 32;
    static final int O_KEY = 64;
    static final int P_KEY = 128;
    static final int SHIFT_KEY = 256;
    static final int SPACE_KEY = 512;
    static final int NUMSHIFT_KEY = 1024;
    
    // maps combinations of the above keys to string outputs
    private static SparseArray<String> mGestures = new SparseArray<String>();
    
    // the root view that we return to the framework when it wants to show the keyboard
    @SuppressWarnings("unused")
	private View mInputView;
    private KeyToucher toucher;
    
    @Override public void onCreate() {
        super.onCreate();
        toucher = new KeyToucher(this);
        try {
        	JSONObject obj = new JSONObject(convertStreamToString( getResources().openRawResource(R.raw.gestures)));
        	@SuppressWarnings("unchecked")
			Iterator<String> i = obj.keys();
        	while( i.hasNext() ) {
        		String key = i.next();
        		mGestures.put(Integer.parseInt(key), obj.getString(key));
        		Log.d("gesture", "key: " + key + " value: " + obj.getString(key));
        	}
        } catch( JSONException e ) {
        	Log.e("json-error", e.toString());
        }
    }
    
    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override public void onInitializeInterface() {        
    }
    
    private class MyButton extends Button {
    	private Drawable originalBackground;
    	public MyButton(Context context) {
			super(context);
			this.originalBackground = getBackground();
		}
		public int keyCode;
    	private int pressed = 0; // this can be -1, 0, or 1; because the UI will sometimes send the up,down messages out of order
    	public void updateBackground() {
    		if( pressed == 1 ) {
    			setBackgroundColor(Color.DKGRAY);
    		} else {
    			setBackground(originalBackground);
    		}
    	}
    	public void reset() {
    		pressed = 0;
    		updateBackground();
    	}
		public void setPressed(boolean p) {
			if( p ) {
				pressed = Math.min(1,  pressed + 1);
			} else {
				pressed = Math.max(-1, pressed - 1);
			}
    		updateBackground();
		}
    	
    }
    
    private class ButtonSet {
    	@SuppressLint("UseSparseArrays")
		private HashMap<Integer,MyButton> buttons = new HashMap<Integer,MyButton>();
    	public int getGesture() {
    		int gesture = 0;
    		for( MyButton b : buttons.values() ) {
    			if( b.pressed == 1 ) {
    				gesture = gesture | b.keyCode;
    			}
    		}
    		return gesture;
    	}
    	public void setPressed(int keyCode, boolean flag) {
    		buttons.get(keyCode).setPressed(flag);
    	}
    	public void add(int keyCode, MyButton button) {
    		buttons.put(Integer.valueOf(keyCode), button);
    	}
    	public void reset() {
    		for( MyButton b : buttons.values() ) {
    			b.reset();
    		}
    	}
    }
    
    
    private class KeyToucher implements View.OnTouchListener {
    	public int sticky = 0; // the total of the sticky downed keys
    	private boolean hasNewKeys = false;
    	public ButtonSet buttons = new ButtonSet();
    	
    	private SoftKeyboard kb;
    	public KeyToucher( SoftKeyboard kb ) {
    		this.kb = kb;
    	}
    	
    	private int fatFingerButtons(View v, MotionEvent event, boolean pressed) {
    		int button = (Integer)v.getTag();
    		buttons.setPressed(button,  pressed);
			for( int p = 0; p < event.getPointerCount(); p++) {
				MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
				event.getPointerCoords(p, coords);
				Rect finger = new Rect();
				// we ignore orientation for now, and just treat max(major,minor)^2 as a square bounding box
				int m = Math.round(Math.max(coords.touchMajor, coords.touchMinor) / 4);
				finger.left = Math.round(coords.x) - m;
				finger.right = Math.round(coords.x) + m;
				finger.top = Math.round(coords.y) - m;
				finger.bottom = Math.round(coords.y) + m;
				if( finger.left < 0 && button > 1 ) {
					buttons.setPressed(button >> 1,  pressed);
				} else if( finger.right > v.getWidth()
						&& button != 128
						&& button != 1024 ) {
					buttons.setPressed(button << 1, pressed);
				}
			}
			return buttons.getGesture();
    	}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if( action == MotionEvent.ACTION_MOVE ) return false;
			
			int button = (Integer)v.getTag();
			switch( action ){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				fatFingerButtons(v, event, true);
				hasNewKeys = true;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				int gesture = fatFingerButtons(v, event, false);
				Log.d("single gesture", "" + (gesture | sticky));
				if( hasNewKeys ) {
					if( gesture == SHIFT_KEY ) {
						sticky = sticky ^ SHIFT_KEY;
						buttons.buttons.get(SHIFT_KEY).setPressed( (sticky & SHIFT_KEY) == SHIFT_KEY );
					} else if( gesture == NUMSHIFT_KEY ) {
						sticky = sticky ^ NUMSHIFT_KEY;
						buttons.buttons.get(NUMSHIFT_KEY).setPressed( (sticky & NUMSHIFT_KEY) == NUMSHIFT_KEY );
					} else {
						boolean modified = kb.doGesture(gesture | sticky);
						if( modified ) {
							// consume the sticky keys
							sticky = 0;
						}
					}
				}
				// consume the released buttons
				if( action == MotionEvent.ACTION_UP ) {
					buttons.reset();
				}
				hasNewKeys = false;
				break;
			}
			Log.d("onTouch", "button: " + button + " action: " + (event.getAction() == 1 ? "up" : "down") + " sticky: " + sticky + " gesture: " + buttons.getGesture());
			return true;
		}

		public void reset() {
			sticky = 0;
			hasNewKeys = false;
			buttons.reset();
		}
    }
    
    private int buttonCount = 0;
    private int rowCount = -1;
    private void addButton(LinearLayout row, String label, int keyCode, LayoutParams layout, KeyToucher touched ) {
    	MyButton a = new MyButton(this);
    	a.keyCode = keyCode;
    	a.setLayoutParams(layout);
    	a.setText(label);
    	a.setTag(Integer.valueOf(keyCode));
    	a.setOnTouchListener(touched);
    	touched.buttons.add(keyCode,  a);
    	row.addView(a);
    	buttonCount++;
    }
    
    private LinearLayout addRow(LinearLayout base, KeyToucher touched) {
    	LinearLayout row = new LinearLayout(this);
    	row.setOnTouchListener(touched);
    	base.addView(row);
    	buttonCount = 0;
    	rowCount++;
    	return row;
    }
    @Override public View onCreateInputView() {
    	
    	// compute the button sizes
    	int w = this.getMaxWidth();
    	LayoutParams smallKey = new LayoutParams(w/8, w/7);
    	LayoutParams shiftKey = new LayoutParams(w/4, w/7);
    	LayoutParams spaceKey = new LayoutParams(w/2, w/7);
    	
    	// create the root layout (linear, vertical)
    	LinearLayout L = new LinearLayout(this);
    	L.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	L.setOrientation(LinearLayout.VERTICAL);
    	
    	// reset whatever gestures/shifts were in progress last time
    	toucher.reset();

    	LinearLayout row = addRow(L, toucher);
    	addButton(row, "a", 1, smallKey, toucher);
    	addButton(row, "s", 2, smallKey, toucher);
    	addButton(row, "e", 4, smallKey, toucher);
    	addButton(row, "t", 8, smallKey, toucher);
    	addButton(row, "n", 16, smallKey, toucher);
    	addButton(row, "i", 32, smallKey, toucher);
    	addButton(row, "o", 64, smallKey, toucher);
    	addButton(row, "p", 128, smallKey, toucher);
    	
    	row = addRow(L, toucher);
    	addButton(row, "sh", 256, shiftKey, toucher);
    	addButton(row, "space", 512, spaceKey, toucher);
    	addButton(row, "#sh", 1024, shiftKey, toucher);
    	   	
    	return mInputView = L;
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        
        
        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_PHONE:
            case InputType.TYPE_CLASS_TEXT:
                
            default:
        }
    }

    /**
     * This is called when the user is done editing a field.  We can use
     * this to reset our state.
     */
    @Override public void onFinishInput() {
        super.onFinishInput();
    }
    
    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
    }
    
    @SuppressWarnings("unused")
	private void doClose() {
        requestHideSelf(0);
    }
    private void doBackspace() {
    	getCurrentInputConnection().deleteSurroundingText(1,0);
    }
    private boolean doGesture(int gesture) {
    	String value = mGestures.get(gesture, "");
    	Log.d("doGesture", value);
    	if( value.length() > 0 ) {
    		if( value.equals("<Backspace>") ) {
    			doBackspace();
    		} else {
    			Log.d("assert", "" + value + " != <Backspace>");
    			getCurrentInputConnection().commitText(value, value.length());
    		}
    		return true;
    	}
    	return false;
    }
    
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
