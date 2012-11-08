#define MASK_ON(mask, bits) (mask = mask | bits)
#define MASK_OFF(mask, bits) (mask = mask ^ (mask & bits))

# each key in Combos is a bitmask
KeyCodes =
	65: 1 # a-key = a = 1
	81: 1 # qwer - uiop aliases
	83: 2 # s-key = s = 2
	87: 2
	68: 4 # d-key = e = 4
	69: 4
	70: 8 # f-key = t = 8
	82: 8
	74: 16 # j-key = n = 16
	85: 16
	75: 32 # k-key = i = 32
	73: 32
	76: 64 # l-key = o = 64
	79: 64
	186: 128 # ;-key = p = 128
	80: 128
	67: 256 # c-key = shift = 256
	86: 256 # v-key = shift = 256
	66: 256 # b-key = shift = 256
	78: 256 # n-key = shift = 256
	32: 512 # space = space = 512
Combos =
	0: "" # unused
	1: "a"
	2: "s"
	3: "w"
	4: "e"
	5: "x"
	6: "d"
	7: "ad"
	8: "t"
	9: "f"
	10: "c"
	11: "ac"
	12: "r"
	13: "ar"
	14: "est"
	15: "wr"
	16: "n"
	17: "q"
	18: "j"
	19: "wn"
	20: "y"
	21: "ay"
	22: "nd"
	23: "and"
	24: "b"
	25: "ab"
	26: "nc"
	27: "can"
	28: "be"
	29: "ran"
	30: "best"
	31: "between"
	32: "i"
	33: "!"
	34: "z"
	35: "wi"
	36: ","
	37: "xi"
	38: "di"
	39: "said"
	40: "v"
	41: "fi"
	42: "ci"
	43: "-"
	44: "ve"
	45: "five"
	46: "tried"
	47: "first"
	48: "h"
	49: "ha"
	50: "sh"
	51: "wh"
	52: "he"
	53: "-"
	54: "she"
	55: "when"
	56: "th"
	57: "that"
	58: "ch"
	59: "with"
	60: "the"
	61: "have"
	62: "these"
	63: "where"
	64: "o"
	65: "("
	66: "."
	67: "wo"
	68: "-"
	69: "xo"
	70: "do"
	71: "-"
	72: "g"
	73: "fo"
	74: "co"
	75: "two"
	76: "ro"
	77: "for"
	78: "dg"
	79: "worst"
	80: "u"
	81: "qu"
	82: "su"
	83: "now"
	84: "eu"
	85: "-"
	86: "du"
	87: "down"
	88: "but"
	89: "fu"
	90: "cu"
	91: "cannot"
	92: "ru"
	93: "before"
	94: "course"
	95: "because"
	96: "l"
	97: "al"
	98: "sl"
	99: "will"
	100: "el"
	101: "-"
	102: "dl"
	103: "well"
	104: "gi"
	105: "fl"
	106: "cl"
	107: "social"
	108: "rl"
	109: "felt"
	110: "told"
	111: "world"
	112: "ion"
	113: "hall"
	114: "ious1"
	115: "who"
	116: "ly"
	117: "alone"
	118: "ould"
	119: "would"
	120: "tion"
	121: "from"
	122: "such"
	123: "against"
	124: "through"
	125: "another"
	126: "could"
	127: "already"
	128: "p"
	129: "?"
	130: ")"
	131: "-"
	132: "'"
	133: "xp"
	134: "s"
	135: "-"
	136: "<Backspace>"
	137: "-"
	138: "-"
	139: "past"
	140: "rp"
	141: "part"
	142: "step"
	143: "expected"
	144: "m"
	145: "am"
	146: "sm"
	147: "mass"
	148: "em"
	149: "may"
	150: "seems"
	151: "same"
	152: "tm"
	153: "-"
	154: "-"
	155: "-"
	156: "rm"
	157: "army"
	158: "system"
	159: "came"
	160: "k"
	161: "ak"
	162: "sk"
	163: "ask"
	164: "ke"
	165: "-"
	166: "-"
	167: "asked"
	168: "-"
	169: "-"
	170: "ck"
	171: "attack"
	172: "rk"
	173: "take"
	174: "spirit"
	175: "dark"
	176: "mi"
	177: "main"
	178: "miss"
	179: "-"
	180: "-"
	181: "make"
	182: "mind"
	183: "knew"
	184: "think"
	185: "-"
	186: "-"
	187: "back"
	188: "them"
	189: "taken"
	190: "didn't"
	191: "american"
	192: ";"
	193: "-"
	194: "-"
	195: "-"
	196: "o'"
	197: "-"
	198: "-"
	199: "-"
	200: "top"
	201: "-"
	202: "stop"
	203: "-"
	204: "pro"
	205: "-"
	206: "stopped"
	207: "power"
	208: "mo"
	209: "-"
	210: "-"
	211: "woman"
	212: "open"
	213: "-"
	214: "some"
	215: "women"
	216: "put"
	217: "among"
	218: "most"
	219: "-"
	220: "more"
	221: "from"
	222: "come"
	223: "company"
	224: "lp"
	225: "-"
	226: "-"
	227: "-"
	228: "like"
	229: "-"
	230: "looked"
	231: "walked"
	232: "took"
	233: "talk"
	234: "stock"
	235: "lack"
	236: "-"
	237: "-"
	238: "period"
	239: "work"
	240: "lm"
	241: "human"
	242: "-"
	243: "small"
	244: "home"
	245: "example"
	246: "simply"
	247: "played"
	248: "book"
	249: "taking"
	250: "much"
	251: "almost"
	252: "problem"
	253: "family"
	254: "economic"
	# 1 unused
	256: "<Shift>"
	257: "A"
	258: "S"
	259: "W"
	260: "E"
	261: "X"
	262: "D"
	# 1 unused
	264: "T"
	265: "F"
	266: "C"
	# 1 unused
	268: "R"
	# 3 unused
	272: "N"
	273: "Q"
	274: "J"
	# 1 unused
	276: "Y"
	# 3 unused
	280: "B"
	# 7 unused
	288: "I"
	289: "<Ctrl>"
	290: "Z"
	# 1 unused
	292: "<"
	# 3 unused
	296: "V"
	# [297-303] unused
	304: "H"
	# [305-319] unused
	320: "O"
	321: "<Alt>"
	322: ">"
	# 1 unused
	324: "_"
	# [325-327] unused
	328: "G"
	# [329-335] unused
	336: "U"
	# [337-351] unused
	352: "L"
	# [353-383] unused
	384: "P"
	385: "/"
	386: "<Esc>"
	# 387 unused
	388: '"'
	# [389-391] unused
	392: "\t"
	# [393-399] unused
	400: "M"
	# [401-415] unused
	416: "K"
	# [417-447] unused
	448: ":"
	# [449-511] unused
	512: " "
	768: "\n"


$(document).ready ->
	$(".asetniop").each ->
		gesture = 0 # bit mask of all the keys pressed during one gesture
		sticky = 0 # bit mask of the shift,ctrl,alt keys that are in 'sticky' state
		hasNewKeys = false # a flag that lets us ignore keyup events on keys that were just used as a gesture

		$.extend @style,
			backgroundImage: 'url(http://asetniop.com/images/LayoutCompleteLetters.png)'
			backgroundSize: '50%'
			backgroundPosition: '100% 100%'
			backgroundRepeat: 'no-repeat no-repeat'
		$.defineProperty @, 'caretPos',
			get: =>
				@focus()
				switch true
					when 'selection' of document
						sel = document.selection.createRange()
						sel.moveStart 'character', @value.length
						sel.text.length
					when 'selectionStart' of @
						@selectionStart
					else
						@value.length
			set: (pos) =>
				switch true
					when 'setSelectionRange' of @
						@focus()
						@setSelectionRange(pos,pos)
					when 'createTextRange' of @
						range = @createTextRange()
						range.collapse(true)
						range.moveEnd('character', pos)
						range.moveStart('character', pos)
						range.select()
		t = $(@)
		t.bind 'keydown', (evt) ->
			MASK_ON(gesture, sticky | KeyCodes[evt.keyCode])
			hasNewKeys = true
			false
		t.bind 'keyup', (evt) ->
			code = KeyCodes[evt.keyCode]
			value = Combos[gesture]
			modified = false
			if hasNewKeys
				if /^<\w+>$/.test value
					switch value
						when "<Backspace>"
							c = @caretPos
							@value = $.stringSplice @value, c-1, c, ''
							@caretPos = c - 1
							modified = true
						when "<Shift>"
							MASK_ON(sticky, 256)
				else if value?
					c = @caretPos
					@value = $.stringSplice @value, c, c, value
					@caretPos = c + value.length
					modified = true
			MASK_OFF(gesture, code)
			if modified
				if gesture is sticky
					gesture = 0
				sticky = 0
				hasNewKeys = false
			false
