package org.asetniop;

import android.util.SparseIntArray;

class Hand {
	private SparseIntArray fingers = new SparseIntArray();
	public void set(int fingerId, int value) { // set the buttons pressed by this finger
		fingers.put(fingerId, value);
	}
	public void release(int fingerId, ButtonPanel buttons) {
		// b is the buttons pushed by this finger
		int b = fingers.get(fingerId, 0);
		// if there are no buttons to release, do nothing
		if( b == 0 ) return;
		// for each button in the system
		for( int i = SoftKeyboard.A_KEY; i <= SoftKeyboard.NUMSHIFT_KEY; i = i << 1 ) {
			// if this finger pushed this button
			if( (b & i) == i ) {
				// and it isn't currently stuck down
				if( ! ((buttons.stuck & i) == i) ) {
					// release it
					buttons.setPressed(i, false);
				}
			}
		}
		// clear the finger from the set
		fingers.delete(fingerId);
	}
}