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

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.softkeyboard.R;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
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
    private View mInputView;
    private KeyToucher toucher;
    
    @Override public void onCreate() {
        super.onCreate();
        try {
        	JSONObject obj = new JSONObject(convertStreamToString( getResources().openRawResource(R.raw.gestures)));
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
    
    private void addButton(LinearLayout row, String label, Object tag, LayoutParams layout, View.OnTouchListener touched ) {
    	Button a = new Button(this);
    	a.setLayoutParams(layout);
    	a.setText(label);
    	a.setTag(tag);
    	a.setOnTouchListener(touched);
    	row.addView(a);
    }
    
    private class KeyToucher implements View.OnTouchListener {
    	public int gesture = 0; // the total combined gesture of all downed keys
    	public int sticky = 0; // the total of the sticky downed keys
    	private boolean hasNewKeys = false;
    	
    	private SoftKeyboard kb;
    	public KeyToucher( SoftKeyboard kb ) {
    		this.kb = kb;
    	}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Object tag = v.getTag();
			Integer button = Integer.valueOf(0);
			if( tag != null )
				button = (Integer)tag;
			switch( event.getAction() ){
			case 0:
				hasNewKeys = true;
				if( button == SHIFT_KEY )
					sticky = sticky | SHIFT_KEY;
				else if( button == NUMSHIFT_KEY )
					sticky = sticky | NUMSHIFT_KEY;
				gesture = gesture | button | sticky;
				break;
			case 1:
				if( hasNewKeys ) {
					boolean modified = kb.doGesture(gesture);
					gesture = gesture ^ (gesture & sticky);
				}
				hasNewKeys = false;
				gesture = gesture ^ (gesture & button);
				break;
			}
			Log.d("onTouch", "button: " + button + " action: " + event.getAction() + " gesture: " + gesture);
			return false;
		}
    }
    
    @Override public View onCreateInputView() {
    	int w = this.getMaxWidth();
    	LinearLayout L = new LinearLayout(this.getBaseContext());
    	L.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	L.setOrientation(LinearLayout.VERTICAL);
    	
    	KeyToucher toucher = new KeyToucher(this);
    	LinearLayout row;
    	
    	L.addView(row = new LinearLayout(this.getBaseContext()));
    	
    	LayoutParams smallKey = new LayoutParams(w/8, LayoutParams.WRAP_CONTENT);
    	LayoutParams shiftKey = new LayoutParams(w/4, LayoutParams.WRAP_CONTENT);
    	LayoutParams spaceKey = new LayoutParams(w/2, LayoutParams.WRAP_CONTENT);
    	
    	addButton(row, "a", 1, smallKey, toucher);
    	addButton(row, "s", 2, smallKey, toucher);
    	addButton(row, "e", 4, smallKey, toucher);
    	addButton(row, "t", 8, smallKey, toucher);
    	addButton(row, "n", 16, smallKey, toucher);
    	addButton(row, "i", 32, smallKey, toucher);
    	addButton(row, "o", 64, smallKey, toucher);
    	addButton(row, "p", 128, smallKey, toucher);
    	
    	L.addView(row = new LinearLayout(this.getBaseContext()));
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
            case InputType.TYPE_CLASS_PHONE: // could auto-complete from contacts
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
        
        // Clear current composing text and candidates.
        
        
        if (mInputView != null) {
            // mInputView.closing();
        }
    }
    
    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
    }
    
    private void doClose() {
        requestHideSelf(0);
    }
    private void doBackspace() {
    	getCurrentInputConnection().deleteSurroundingText(1,0);
    }
    private boolean doGesture(int gesture) {
    	String value = mGestures.get(gesture, "");
    	if( value.length() > 0 ) {
    		if( value == "<Backspace>") {
    			doBackspace();
    		} else {
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
