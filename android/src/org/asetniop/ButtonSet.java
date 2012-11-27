package org.asetniop;

import java.util.HashMap;

import android.annotation.SuppressLint;

class ButtonSet {
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