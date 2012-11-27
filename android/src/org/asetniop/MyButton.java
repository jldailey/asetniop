package org.asetniop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;

class MyButton extends Button {
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
	public void updateText(int chord) {
		int fakeGesture = chord | keyCode;
		setText(SoftKeyboard.getLabel(chord));
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