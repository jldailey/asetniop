package org.asetniop;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;

class ButtonSet {

	class MyButton extends Button {
		private Drawable originalBackground;
		private ChordSet chords;
		public MyButton(Context context, ChordSet chords) {
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
		public void updateText(int chord) {
			setText(chords.getLabel(chord | this.keyCode, ""));
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
	
	public Button create(SoftKeyboard kb, int keyCode) {
		MyButton b = new MyButton(kb, kb.mChords);
		b.keyCode = keyCode;
		b.setTag(Integer.valueOf(keyCode));
		return b;
	}

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer,MyButton> buttons = new HashMap<Integer,MyButton>();
	public int chord = 0;
	public int sticky = 0;
	public void setPressed(int keyCode, boolean flag) {
		MyButton b = buttons.get(keyCode);
		if( b == null ) return;
		if( b.press(flag) == 1 ) {
			chord = chord | keyCode;
		} else {
			chord = chord ^ ( chord & keyCode );
		}
		refresh(chord | sticky);
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
		refresh(chord | sticky);
	}
	public void unstick(int keyCode) {
		sticky = sticky ^ ( sticky & keyCode );
		setPressed(keyCode, false);
		refresh(chord | sticky);
	}
	public void add(MyButton button) {
		buttons.put(button.keyCode, button);
	}
	public void reset() {
		chord = 0;
		for( MyButton b : buttons.values() ) {
			b.reset();
		}
	}
}