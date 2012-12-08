package org.asetniop;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;

@SuppressWarnings("serial")
public class ChordSet {
	private SparseArray<String> chords;

	private final HashMap<String, String> outputs = new HashMap<String, String> () {{
		put("<Tab>", "\t");
		put("<Newline>", "\n");
		put("<LF>", "\n");
		put("<NL>", "\n");
		put("<CR>", "\r");
		put("<Shift>", "");
		put("<Ctrl>", "");
		put("<Alt>", "");
		put("<Esc>", "");
		put("<Number>", "");
		put("<Backspace>", "");
		put("<LessThan>", "<"); // because eclipse is buggy about linting JSON
		put("<GreaterThan>", ">");
	}};

	private final HashMap<String, String> labels = new HashMap<String, String> () {{
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
		put("<Esc>", "esc");
		put("<F1>", "F1");
		put("<F2>", "F2");
		put("<F3>", "F3");
		put("<F4>", "F4");
		put("<F5>", "F5");
		put("<F6>", "F6");
		put("<F7>", "F7");
		put("<F8>", "F8");
		put("<F9>", "F9");
		put("<F10>", "F10");
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
	
	private final HashMap<String, Integer> rawKeys = new HashMap<String, Integer> () {{
		put("<Esc>", KeyEvent.KEYCODE_ESCAPE);
		put("<F1>", KeyEvent.KEYCODE_F1);
		put("<F2>", KeyEvent.KEYCODE_F2);
		put("<F3>", KeyEvent.KEYCODE_F3);
		put("<F4>", KeyEvent.KEYCODE_F4);
		put("<F5>", KeyEvent.KEYCODE_F5);
		put("<F6>", KeyEvent.KEYCODE_F6);
		put("<F7>", KeyEvent.KEYCODE_F7);
		put("<F8>", KeyEvent.KEYCODE_F8);
		put("<F9>", KeyEvent.KEYCODE_F9);
		put("<F10>", KeyEvent.KEYCODE_F10);
	}};

	public ChordSet(InputStream jsonStream) {
		chords = new SparseArray<String>(1024);
		try {
			JSONObject obj = new JSONObject(convertStreamToString(jsonStream));
			@SuppressWarnings("unchecked")
			Iterator<String> i = obj.keys();
			while( i.hasNext() ) {
				String key = i.next();
				chords.put(Integer.parseInt(key), obj.getString(key));
			}
		} catch( JSONException e ) {
			Log.e("json-error", e.toString());
		}
	}

	public String getChord(int chord, String def) {
		return chords.get(chord, def);
	}

	public String getOutput(int chord, String def) {
		String ret = getChord(chord, def);
		return outputs.containsKey(ret) ? outputs.get(ret) : ret;
	}

	public String getLabel(int chord, String def) {
		String ret = getChord(chord, def);
		return labels.containsKey(ret) ? labels.get(ret) : ret;
	}

	public int getRawKey(int chord, int def) {
		String val = getChord(chord, "");
		return rawKeys.containsKey(val) ? rawKeys.get(val) : def;
	}

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}


}
