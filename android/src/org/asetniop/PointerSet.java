package org.asetniop;

import android.util.SparseIntArray;

class PointerSet {
	private SparseIntArray pointers = new SparseIntArray();
	public void set(int pointerId, int value) { // set the buttons pressed by this pointer
		pointers.put(pointerId, value);
	}
	public void release(int pointerId, ButtonPanel buttons) {
		// b is the buttons pushed by this pointer
		int b = pointers.get(pointerId, 0);
		// if there are no buttons to release, do nothing
		if( b == 0 ) return;
		// for each button in the system
		for( int i = SoftKeyboard.A_KEY; i <= SoftKeyboard.NUMSHIFT_KEY; i = i << 1 ) {
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