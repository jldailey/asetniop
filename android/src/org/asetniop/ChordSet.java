package org.asetniop;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

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
	
	public ChordSet(InputStream jsonStream) {
		chords = new SparseArray<String>(1024);
		try {
			JSONObject obj = new JSONObject(convertStreamToString(jsonStream));
			@SuppressWarnings("unchecked")
			Iterator<String> i = obj.keys();
			while( i.hasNext() ) {
				String key = i.next();
				Log.d("ChordSet", "reading key: " + key);
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


	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}


}
