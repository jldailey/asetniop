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

package com.example.android.softkeyboard;

import java.util.HashMap;
import java.util.Map;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class SoftKeyboard extends InputMethodService 
        implements KeyboardView.OnKeyboardActionListener {
    static final boolean DEBUG = false;

    // private InputMethodManager mInputMethodManager;

    private KeyboardView mInputView;
    // private CompletionInfo[] mCompletions;
    

    private Keyboard mKeyboard;
    
    private String mWordSeparators;
    
    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override public void onCreate() {
        super.onCreate();
        mWordSeparators = getResources().getString(R.string.word_separators);
    }
    
    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override public void onInitializeInterface() {        
        mKeyboard = new Keyboard(this, R.xml.asetniop);
    }
    
    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override public View onCreateInputView() {
        mInputView = (KeyboardView) getLayoutInflater().inflate(
                R.layout.input, null);
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(mKeyboard);
        return mInputView;
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
            mInputView.closing();
        }
    }
    
    @Override public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        mInputView.setKeyboard(mKeyboard);
        mInputView.closing(); // really?
    }


    
        
    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// return false to let the underlying text editor handle the key
    	// return true if we handled it
    	Log.d("kb", "onKeyDown:" + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
    	Log.d("kb", "onKeyUp:" + keyCode);
        return super.onKeyUp(keyCode, event);
    }
    
    @Override public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
    	Log.d("kb", "onKeyMultiple: " + keyCode + " " + count + " " + event.toString());
		return false;
    }
    
    @Override public boolean onTrackballEvent(MotionEvent event) {
    	Log.d("kb", "trackball event");
		return false;
    }
    
    
    private static final HashMap<Integer, String> mKeys = new HashMap<Integer, String>() {{
    	put(1, "a");
    	put(2, "s");
    	put(3, "w");
    	put(4, "e");
    	put(5, "x");
    	put(6, "c");
    	put(7, "");
    	put(8, "t");
    	put(16, "n");
    	put(32, "i");
    	put(64, "o");
    	put(128, "p");
    }};
    
    private String getOutput(int gesture, String def) {
    	if (mKeys.containsKey(gesture)) {
    		return mKeys.get(gesture);
    	}
    	return def;
    }
    
    // Implementation of KeyboardViewListener
    public void onKey(int primaryCode, int[] keyCodes) {
    	int gesture = 0;
    	for(int i = 0; i < keyCodes.length; i++) {
    		gesture = gesture | keyCodes[i];
    	}
    	Log.d("kb", "onKey: " + primaryCode + " " + gesture + " " + keyCodes.length);
    	String text = getOutput(gesture, "");
    	InputConnection ic = getCurrentInputConnection();
    	if( ic == null ) return;
    	ic.commitText(text, text.length());
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }
    
    private void doClose() {
        requestHideSelf(0);
        mInputView.closing();
    }
    
    public boolean isWordSeparator(int code) {
        return mWordSeparators.indexOf(code) > -1;
    }
    
    public void swipeRight() {
    }
    
    public void swipeLeft() {
    }

    public void swipeDown() {
        doClose();
    }

    public void swipeUp() {
    }
    
    public void onPress(int primaryCode) {
    }
    
    public void onRelease(int primaryCode) {
    }
}
