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

import org.asetniop.R;

import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.*;

public class SoftKeyboard extends InputMethodService {
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

	// this is a map of the connected edges, so entries are like A_KEY + RIGHT_EDGE => E_KEY
	// filled out by onCreateView, since that also defines the layout
	private static SparseIntArray buttonEdges = new SparseIntArray();

	private KeyToucher toucher;
	public ChordSet mChords;

	@Override public void onCreate() {
		super.onCreate();
		toucher = new KeyToucher();
		mChords = new ChordSet(getResources().openRawResource(R.raw.chords));
	}

	// onTouch events from all the various buttons and views go through KeyToucher
	private class KeyToucher implements View.OnTouchListener {
		private boolean hasNewKeys = false;
		public ButtonPanel buttons = new ButtonPanel();
		public Hand pointers = new Hand();

		private int fatFingerPress(View v, MotionEvent.PointerCoords coords, boolean pressed) {
			if( v == null ) return 0;
			Object tag = v.getTag();
			if( tag == null ) return 0;
			// the original button pressed
			int button = (Integer)tag;
			if( button == 0 ) return 0;

			// press the original button
			buttons.setPressed(button,  pressed);

			// find the finger
			Rect finger = new Rect();
			// we ignore orientation for now, and just treat max(major,minor)^2 as a square bounding box
			int m = Math.round(Math.max(coords.touchMajor, coords.touchMinor) / 5); // divide down so we get a centered box, smaller than the real hit box
			finger.left = Math.round(coords.x) - m;
			finger.right = Math.round(coords.x) + m;
			finger.top = Math.round(coords.y) - m;
			finger.bottom = Math.round(coords.y) + m;

			// test each of the edges
			int w = v.getWidth();
			int h = v.getHeight();
			int overlapMargin = w/4;

			int cursorKey = button;
			for(; w > 0 && finger.left < 0; finger.left += w) {
				if( finger.left > -overlapMargin ) {
					buttons.setPressed(cursorKey, pressed);
					button = button | cursorKey;
				}
				cursorKey = buttonEdges.get(cursorKey | LEFT_EDGE, cursorKey);
				if( cursorKey > 0 ) w = buttons.get(cursorKey).getWidth();
			}
			for(finger.right -= w; w > 0 && finger.right > 0; finger.right -= w) {
				if( finger.right < overlapMargin ) {
					buttons.setPressed(cursorKey, pressed);
					button = button | cursorKey;
				}
				cursorKey = buttonEdges.get(cursorKey | RIGHT_EDGE, cursorKey);
				if( cursorKey > 0 ) w = buttons.get(cursorKey).getWidth();
			}
			if( finger.top < 0 ) {
				if( finger.top > -overlapMargin ) {
					buttons.setPressed(cursorKey, pressed);
					button = button | cursorKey;
				}
				cursorKey = buttonEdges.get(cursorKey | (finger.left < (w/2) ? TOP_LEFT_EDGE : TOP_RIGHT_EDGE), cursorKey);
				if( cursorKey > 0 ) w = buttons.get(cursorKey).getWidth();
			}
			if( finger.bottom > h ) {
				if( (finger.bottom - h) < overlapMargin ) {
					buttons.setPressed(cursorKey, pressed);
					button = button | cursorKey;
				}
				cursorKey = buttonEdges.get(cursorKey | BOTTOM_EDGE, cursorKey);
				if( cursorKey > 0 ) w = buttons.get(cursorKey).getWidth();
			}
			if( cursorKey > 0 ) {
				// and press the second key also
				buttons.setPressed(cursorKey, pressed);
				button = button | cursorKey;
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
				if( hasNewKeys )
					composeChord(buttons.chord | buttons.sticky);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP: // primary and secondary pointer events are all the same to us
				if( hasNewKeys ) {
					// if the chord is exactly one sticky key
					if( (buttons.chord & STICKY_KEYS) == buttons.chord && Integer.bitCount(buttons.chord) == 1) {
						// toggle that sticky key
						buttons.toggleSticky();
					} else if( commitChord(buttons.chord | buttons.sticky) ) {
						// consume all stuck keys
						buttons.unstick(buttons.sticky);
					}
				}
				// consume the released buttons
				for(p = 0; p < event.getPointerCount(); p++) {
					pointers.release(event.getPointerId(p), buttons);
				}
				composeChord(0);
				hasNewKeys = false;
				break;
			}
			return true;
		}

		public void reset() {
			composeChord(0);
			hasNewKeys = false;
			buttons.reset();
		}
	}

	private void addButton(LinearLayout row, int keyCode, LayoutParams layout) {
		Button b = toucher.buttons.createButton(this, keyCode);
		b.setLayoutParams(layout);
		b.setText(mChords.getLabel(keyCode, ""));
		b.setOnTouchListener(this.toucher);
		row.addView(b);
	}

	private LinearLayout addRow(LinearLayout base) {
		LinearLayout row = new LinearLayout(this);
		row.setOnTouchListener(toucher);
		base.addView(row);
		return row;
	}

	@Override public View onCreateInputView() {
		// reset whatever chords/shifts were in progress last time
		reset();

		// compute the button sizes
		int w = this.getMaxWidth();
		int h = (int)getResources().getDimension(R.dimen.key_height);
		LayoutParams smallKey = new LayoutParams(w/6, h);
		LayoutParams shiftKey = new LayoutParams(w/6, h);
		LayoutParams spaceKey = new LayoutParams(w/3, h);

		// create the root layout (linear, vertical), which will hold two horizontal rows
		LinearLayout root = new LinearLayout(this);
		root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		root.setOrientation(LinearLayout.VERTICAL);

		// create the first row
		LinearLayout row = addRow(root);

		addButton(row, A_KEY, smallKey);
		buttonEdges.put(A_KEY + RIGHT_EDGE, T_KEY);
		buttonEdges.put(A_KEY + BOTTOM_EDGE, S_KEY);

		addButton(row, T_KEY, smallKey);
		buttonEdges.put(T_KEY + RIGHT_EDGE, SHIFT_KEY);
		buttonEdges.put(T_KEY + LEFT_EDGE, A_KEY);
		buttonEdges.put(T_KEY + BOTTOM_EDGE, E_KEY);

		addButton(row, SHIFT_KEY, shiftKey);
		buttonEdges.put(SHIFT_KEY + RIGHT_EDGE, NUMSHIFT_KEY);
		buttonEdges.put(SHIFT_KEY + LEFT_EDGE, T_KEY);
		buttonEdges.put(SHIFT_KEY + BOTTOM_EDGE, SPACE_KEY);

		addButton(row, NUMSHIFT_KEY, shiftKey);
		buttonEdges.put(NUMSHIFT_KEY + RIGHT_EDGE, N_KEY);
		buttonEdges.put(NUMSHIFT_KEY + LEFT_EDGE, SHIFT_KEY);
		buttonEdges.put(NUMSHIFT_KEY + BOTTOM_EDGE, SPACE_KEY);

		addButton(row, N_KEY, smallKey);
		buttonEdges.put(N_KEY + RIGHT_EDGE, P_KEY);
		buttonEdges.put(N_KEY + LEFT_EDGE, NUMSHIFT_KEY);
		buttonEdges.put(N_KEY + BOTTOM_EDGE, I_KEY);

		addButton(row, P_KEY, smallKey);
		buttonEdges.put(P_KEY + LEFT_EDGE, N_KEY);
		buttonEdges.put(P_KEY + BOTTOM_EDGE, O_KEY);

		// create the second row
		row = addRow(root);
		addButton(row, S_KEY, smallKey);
		buttonEdges.put(S_KEY + RIGHT_EDGE, E_KEY);
		buttonEdges.put(S_KEY + TOP_LEFT_EDGE, A_KEY);
		buttonEdges.put(S_KEY + TOP_RIGHT_EDGE, A_KEY);

		addButton(row, E_KEY, smallKey);
		buttonEdges.put(E_KEY + RIGHT_EDGE, SPACE_KEY);
		buttonEdges.put(E_KEY + LEFT_EDGE, S_KEY);
		buttonEdges.put(E_KEY + TOP_LEFT_EDGE, T_KEY);
		buttonEdges.put(E_KEY + TOP_RIGHT_EDGE, T_KEY);

		addButton(row, SPACE_KEY, spaceKey);
		buttonEdges.put(SPACE_KEY + RIGHT_EDGE, I_KEY);
		buttonEdges.put(SPACE_KEY + LEFT_EDGE, E_KEY);
		buttonEdges.put(SPACE_KEY + TOP_LEFT_EDGE, SHIFT_KEY);
		buttonEdges.put(SPACE_KEY + TOP_RIGHT_EDGE, NUMSHIFT_KEY);

		addButton(row, I_KEY, smallKey);
		buttonEdges.put(I_KEY + RIGHT_EDGE, O_KEY);
		buttonEdges.put(I_KEY + LEFT_EDGE, SPACE_KEY);
		buttonEdges.put(I_KEY + TOP_LEFT_EDGE, N_KEY);
		buttonEdges.put(I_KEY + TOP_RIGHT_EDGE, N_KEY);

		addButton(row, O_KEY, smallKey);
		buttonEdges.put(O_KEY + LEFT_EDGE, I_KEY);
		buttonEdges.put(O_KEY + TOP_LEFT_EDGE, P_KEY);
		buttonEdges.put(O_KEY + TOP_RIGHT_EDGE, P_KEY);

		return root;
	}

	@Override public void onStartInput(EditorInfo attribute, boolean restarting) {
		super.onStartInput(attribute, restarting);
		reset();

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

	public void reset() {
		toucher.reset();
		composeChord(0);
	}

	@Override public void onFinishInput() {
		super.onFinishInput();
		reset();
	}

	@Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
		super.onStartInputView(attribute, restarting);
		reset();
	}
	
	public boolean sendKeyPress(int keyCode) {
		InputConnection ic = getCurrentInputConnection();
		ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
		ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode));
		return true;
	}

	public void composeChord(int chord) {
		String output = mChords.getOutput(chord, "");
		InputConnection ic = getCurrentInputConnection();
		if( ic == null ) return;
		if( output == null )
			ic.finishComposingText();
		else
			ic.setComposingText(output, 1);
	}

	public boolean commitChord(int chord) {
		InputConnection ic = getCurrentInputConnection();
		if( ic == null ) return false;

		String value = mChords.getChord(chord, "");

		if( value.length() == 0 )
			return false;

		if( value.equals("<Backspace>") )
			return ic.deleteSurroundingText(1,0) || true;

		int raw = mChords.getRawKey(chord, 0);
		if( raw > 0 )
			return sendKeyPress(raw);

		return ic.commitText(mChords.getOutput(chord,  value), 1) || true;
	}

	public String getLabel(int chord) {
		return mChords.getLabel(chord,  "" );
	}

}
