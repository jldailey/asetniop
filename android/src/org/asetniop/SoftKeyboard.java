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

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.softkeyboard.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

public class SoftKeyboard extends InputMethodService {
	static final boolean DEBUG = false;
	static final int A_KEY = 1 << 0;
	static final int S_KEY = 1 << 1;
	static final int E_KEY = 1 << 2;
	static final int T_KEY = 1 << 3;
	static final int N_KEY = 1 << 4;
	static final int I_KEY = 1 << 5;
	static final int O_KEY = 1 << 6;
	static final int P_KEY = 1 << 7;
	static final int SHIFT_KEY = 1 << 8;
	static final int SPACE_KEY = 1 << 9;
	static final int NUMSHIFT_KEY = 1 << 10;
	static final int STICKY_KEYS = SHIFT_KEY | NUMSHIFT_KEY;

	static final int TOP_LEFT_EDGE = 1 << 11;
	static final int TOP_RIGHT_EDGE = 1 << 12;
	static final int RIGHT_EDGE = 1 << 13;
	static final int BOTTOM_EDGE = 1 << 14;
	static final int LEFT_EDGE = 1 << 15;

	private static final SparseIntArray fatFingerEdges = new SparseIntArray() {{
		put(SHIFT_KEY + TOP_LEFT_EDGE, A_KEY);
		put(SHIFT_KEY + TOP_RIGHT_EDGE, S_KEY);
		put(SHIFT_KEY + RIGHT_EDGE, SPACE_KEY);
		put(NUMSHIFT_KEY + TOP_LEFT_EDGE, O_KEY);
		put(NUMSHIFT_KEY + TOP_RIGHT_EDGE, P_KEY);
		put(NUMSHIFT_KEY + LEFT_EDGE, SPACE_KEY);
		put(SPACE_KEY + LEFT_EDGE, SHIFT_KEY);
		put(SPACE_KEY + RIGHT_EDGE, NUMSHIFT_KEY);
		put(A_KEY + RIGHT_EDGE, S_KEY);
		put(S_KEY + RIGHT_EDGE, E_KEY);
		put(E_KEY + RIGHT_EDGE, T_KEY);
		put(T_KEY + RIGHT_EDGE, N_KEY);
		put(N_KEY + RIGHT_EDGE, I_KEY);
		put(I_KEY + RIGHT_EDGE, O_KEY);
		put(O_KEY + RIGHT_EDGE, P_KEY);
		put(S_KEY + LEFT_EDGE, A_KEY);
		put(E_KEY + LEFT_EDGE, S_KEY);
		put(T_KEY + LEFT_EDGE, E_KEY);
		put(N_KEY + LEFT_EDGE, T_KEY);
		put(I_KEY + LEFT_EDGE, N_KEY);
		put(O_KEY + LEFT_EDGE, I_KEY);
		put(P_KEY + LEFT_EDGE, O_KEY);
		put(A_KEY + BOTTOM_EDGE, SHIFT_KEY);
		put(S_KEY + BOTTOM_EDGE, SHIFT_KEY);
		put(E_KEY + BOTTOM_EDGE, SPACE_KEY);
		put(T_KEY + BOTTOM_EDGE, SPACE_KEY);
		put(N_KEY + BOTTOM_EDGE, SPACE_KEY);
		put(I_KEY + BOTTOM_EDGE, SPACE_KEY);
		put(O_KEY + BOTTOM_EDGE, NUMSHIFT_KEY);
		put(P_KEY + BOTTOM_EDGE, NUMSHIFT_KEY);
	}};

	// maps combinations of the key bits to string outputs
	// we read these from res/raw/gestures.json
	private static SparseArray<String> mGestures = new SparseArray<String>();

	private static HashMap<String, String> mOutputs = new HashMap<String, String> () {
		private static final long serialVersionUID = 1L;
	{
		put("<Tab>", "\t");
		put("<Newline>", "\n");
		put("<LF>", "\n");
		put("<NL>", "\n");
		put("<CR>", "\r");
		put("<Shift>", "");
		put("<Ctrl>", "");
		put("<Alt>", "");
		put("<Number>", "");
		put("<Backspace>", "");
		put("<LessThan>", "<"); // because eclipse is _re_tar_ded_ about JSON files
		put("<GreaterThan>", ">");
	}};
	private static HashMap<String, String> mLabels = new HashMap<String, String> () {
		private static final long serialVersionUID = 1L;
	{
		put("<Tab>", "\\t");
		put("<Newline>", "\\n");
		put("<LF>", "\\n");
		put("<NL>", "\\n");
		put("<CR>", "\\r");
		put("<Shift>", "sh");
		put("<Ctrl>", "ctl");
		put("<Alt>", "alt");
		put("<Number>", "num");
		put("<Backspace>", "bs");
		put("<LessThan>", "<");
		put("<GreaterThan>", ">");
		put(" ", "space");
		put("a", "a");
		put("s", "s");
		put("e", "e");
		put("t", "t");
		put("n", "n");
		put("i", "i");
		put("o", "o");
		put("p", "p");
	}};

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
			}
		} catch( JSONException e ) {
			Log.e("json-error", e.toString());
		}


	}

	/**
	 * This is the point where you can do all of your UI initialization.  It
	 * is called after creation and any configuration change.
	 */
	@Override public void onInitializeInterface() { }

	private class MyButton extends Button {
		private Drawable originalBackground;
		public MyButton(Context context) {
			super(context);
			this.originalBackground = getBackground();
		}
		public int keyCode;
		private int status = 0; // this can be -1, 0, or 1; because the UI will sometimes send the up,down messages out of order
		public void updateBackground() {
			if( status == 1 ) {
				setBackgroundColor(Color.DKGRAY);
			} else {
				setBackground(originalBackground);
			}
		}
		public void updateText(int gesture) {
			int fakeGesture = gesture | keyCode;
			String text = mGestures.get(fakeGesture);
			if( text == null ) text = "";
			String label = mLabels.get(text);
			if( label == null ) label = text;
			setText(label);
		}
		public void reset() {
			status = 0;
			updateBackground();
			updateText(0);
		}
		public int press(boolean p) {
			if( p ) {
				status = 1; // Math.min(1,  status + 1);
			} else {
				status = 0; // Math.max(-1, status - 1);
			}
			return status;
		}
		public String toString() {
			return "{MyButton keyCode:"+keyCode + " status: " + status + "}";
		}
	}

	private class ButtonSet {
		@SuppressLint("UseSparseArrays")
		private HashMap<Integer,MyButton> buttons = new HashMap<Integer,MyButton>();
		public int gesture = 0;
		public int sticky = 0;
		public void setPressed(int keyCode, boolean flag) {
			MyButton b = buttons.get(keyCode);
			if( b == null ) return;
			if( b.press(flag) == 1 ) {
				gesture = gesture | keyCode;
			} else {
				gesture = gesture ^ ( gesture & keyCode );
			}
			refresh(gesture | sticky);
		}
		public void refresh(int g) {
			for( MyButton b : buttons.values() ) {
				b.updateBackground();
				b.updateText(g);
			}
		}
		public void stick(int keyCode) {
			sticky = sticky | keyCode;
			setPressed(keyCode, true);
			refresh(gesture | sticky);
		}
		public void unstick(int keyCode) {
			sticky = sticky ^ ( sticky & keyCode );
			setPressed(keyCode, false);
			refresh(gesture | sticky);
		}
		public void add(MyButton button) {
			buttons.put(button.keyCode, button);
		}
		public void reset() {
			gesture = 0;
			for( MyButton b : buttons.values() ) {
				b.reset();
			}
		}
	}

	private class PointerSet {
		private SparseIntArray pointers = new SparseIntArray();
		public void set(int pointerId, int value) { // set the buttons pressed by this pointer
			pointers.put(pointerId, value);
		}
		public void release(int pointerId, ButtonSet buttons) {
			// b is the buttons pushed by this pointer
			int b = pointers.get(pointerId, 0);
			// if there are no buttons to release, do nothing
			if( b == 0 ) return;
			// for each button in the system
			for( int i = A_KEY; i <= NUMSHIFT_KEY; i = i << 1 ) {
				// if this pointer pushed this button
				if( (b & i) == i ) {
					// and it isn't currently stuck down
					if( ! ((buttons.sticky & i) == i) ) {
						// release it
						buttons.setPressed(i, false);
					}
				}
			}
			// clear the pointer from the set
			pointers.delete(pointerId);
		}
	}


	private class KeyToucher implements View.OnTouchListener {
		private boolean hasNewKeys = false;
		public ButtonSet buttons = new ButtonSet();
		public PointerSet pointers = new PointerSet();

		private SoftKeyboard kb;
		public KeyToucher( SoftKeyboard kb ) {
			this.kb = kb;
		}

		private int fatFingerPress(View v, MotionEvent.PointerCoords coords, boolean pressed) {
			// the original button pressed
			int button = (Integer)v.getTag();
			// the extra button we may spill-over onto with our fat fingers
			int button2 = 0;

			// first, find the finger
			Rect finger = new Rect();
			// we ignore orientation for now, and just treat max(major,minor)^2 as a square bounding box
			int m = Math.round(Math.max(coords.touchMajor, coords.touchMinor) / 6); // divide down so we get a centered box, smaller than the real hit box
			finger.left = Math.round(coords.x) - m;
			finger.right = Math.round(coords.x) + m;
			finger.top = Math.round(coords.y) - m;
			finger.bottom = Math.round(coords.y) + m;

			// test each of the edges
			int edge = 0;
			int w = v.getWidth();
			if( finger.left < 0 )
				edge = LEFT_EDGE;
			else if( finger.right > w )
				edge = RIGHT_EDGE;
			else if( finger.top < 0 )
				edge = finger.left < (w/2) ? TOP_LEFT_EDGE : TOP_RIGHT_EDGE;
			else if( finger.bottom > v.getHeight() )
				edge = BOTTOM_EDGE;

			// press the original button
			buttons.setPressed(button,  pressed);
			// if we crossed an edge
			if( edge > 0 ) {
				// find the button on the other side of that edge
				button2 = fatFingerEdges.get(button | edge, 0);
				if( button2 > 0 ) {
					// and press it also
					buttons.setPressed(button2, pressed);
					button = button | button2;
				}
			}
			return button;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();

			// what should happen is that when a pointer goes down
			// we write down the pointer id, and the buttons they pushed
			// so later we can release all those buttons when that pointer id is lifted
			// no matter the location

			int button = (Integer)v.getTag();
			int p, i = 0;
			MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();

			switch( action ){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN: // primary and secondary pointer events are all the same to us
				hasNewKeys = true;
			case MotionEvent.ACTION_MOVE:
				for( p = 0; p < event.getPointerCount(); p++) {
					i = event.getPointerId(p);
					pointers.release(i, buttons);
					event.getPointerCoords(p, coords);
					pointers.set(i, fatFingerPress(v, coords, true));
				}
				composeGesture(buttons.gesture | buttons.sticky);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP: // primary and secondary pointer events are all the same to us
				if( hasNewKeys ) {
					if( (buttons.gesture & STICKY_KEYS) == buttons.gesture ) { // if the gesture is all sticky keys
						if( buttons.gesture == buttons.sticky ) { // if it was already stuck
							buttons.unstick(buttons.gesture);
						} else {
							buttons.stick(buttons.gesture);
						}
					} else {
						boolean modified = kb.doGesture(buttons.gesture | buttons.sticky);
						if( modified ) {
							// consume the sticky keys
							buttons.unstick(buttons.sticky);
						}
					}
				}
				// consume the released buttons
				for(p = 0; p < event.getPointerCount(); p++) {
					pointers.release(event.getPointerId(p), buttons);
				}
				composeGesture(0);
				hasNewKeys = false;
				break;
			}
			Log.d("onTouch", "button: " + button + " action: " + event.getAction() + " gesture: " + buttons.gesture);
			return true;
		}

		public void reset() {
			hasNewKeys = false;
			buttons.reset();
		}
	}

	private void addButton(LinearLayout row, int keyCode, LayoutParams layout) {
		MyButton b = new MyButton(this);
		b.keyCode = keyCode;
		b.setLayoutParams(layout);
		b.setText(mLabels.get(mGestures.get(keyCode)));
		b.setTag(Integer.valueOf(keyCode));
		b.setOnTouchListener(this.toucher);
		toucher.buttons.add(b);
		row.addView(b);
	}

	private LinearLayout addRow(LinearLayout base) {
		LinearLayout row = new LinearLayout(this);
		row.setOnTouchListener(toucher);
		base.addView(row);
		return row;
	}

	@Override public View onCreateInputView() {

		// compute the button sizes
		int w = this.getMaxWidth();
		int d = 3;
		if( w < 1000 ) d = 2;
		LayoutParams smallKey = new LayoutParams(w/8, w/(2*d));
		LayoutParams shiftKey = new LayoutParams(w/4, w/(4*d));
		LayoutParams spaceKey = new LayoutParams(w/2, w/(4*d));

		// create the root layout (linear, vertical)
		LinearLayout L = new LinearLayout(this);
		L.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		L.setOrientation(LinearLayout.VERTICAL);

		// reset whatever gestures/shifts were in progress last time
		toucher.reset();

		LinearLayout row = addRow(L);
		addButton(row, A_KEY, smallKey);
		addButton(row, S_KEY, smallKey);
		addButton(row, E_KEY, smallKey);
		addButton(row, T_KEY, smallKey);
		addButton(row, N_KEY, smallKey);
		addButton(row, I_KEY, smallKey);
		addButton(row, O_KEY, smallKey);
		addButton(row, P_KEY, smallKey);

		row = addRow(L);
		addButton(row, SHIFT_KEY, shiftKey);
		addButton(row, SPACE_KEY, spaceKey);
		addButton(row, NUMSHIFT_KEY, shiftKey);

		return L;
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

	private void composeGesture(int gesture) {
		String value = mGestures.get(gesture, "");
		String output = mOutputs.get(value);
		if( output == null )
			output = value;
		getCurrentInputConnection().setComposingText(output, 1);
	}

	private boolean doGesture(int gesture) {
		String value = mGestures.get(gesture, "");
		Log.d("doGesture", value);
		if( value.length() > 0 ) {
			if( value.equals("<Backspace>") ) {
				getCurrentInputConnection().deleteSurroundingText(1,0);
			} else {
				String output = mOutputs.get(value);
				if( output != null )
					value = output;
				getCurrentInputConnection().commitText(value, 1);
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
