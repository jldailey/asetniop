#define MASK_HAS(mask, bits) ((mask & bits) is bits)
#define MASK_ON(mask, bits) (mask = mask | bits)
#define MASK_OFF(mask, bits) (mask = mask ^ (mask & bits))
#define MASK_TOGGLE(mask, bits) if MASK_HAS(mask, bits) then MASK_OFF(mask, bits) else MASK_ON(mask, bits)

# the various bits of the gesture mask:
#define A_BIT 1
#define S_BIT 2
#define E_BIT 4
#define T_BIT 8
#define N_BIT 16
#define I_BIT 32
#define O_BIT 64
#define P_BIT 128
#define SHIFT_BIT 256
#define SPACE_BIT 512

# the evt.keyCode values from keyup/down events
#define A_KEY 65
#define Q_KEY 81
#define S_KEY 83
#define W_KEY 87
#define D_KEY 68
#define E_KEY 69
#define F_KEY 70
#define R_KEY 82
#define J_KEY 74
#define U_KEY 85
#define K_KEY 75
#define I_KEY 73
#define L_KEY 76
#define O_KEY 79
#define SEMICOLON_KEY 186
#define P_KEY 80
#define C_KEY 67
#define V_KEY 86
#define N_KEY 66
#define M_KEY 78
#define SPACE_KEY 32

# each key in Combos is a bitmask
KeyMap =
	A_KEY: A_BIT
	Q_KEY: A_BIT
	S_KEY: S_BIT
	W_KEY: S_BIT
	D_KEY: E_BIT
	E_KEY: E_BIT
	F_KEY: T_BIT
	R_KEY: T_BIT
	J_KEY: N_BIT
	U_KEY: N_BIT
	K_KEY: I_BIT
	I_KEY: I_BIT
	L_KEY: O_BIT
	O_KEY: O_BIT
	SEMICOLON_KEY: P_BIT
	P_KEY: P_BIT
	C_KEY: SHIFT_BIT
	V_KEY: SHIFT_BIT
	N_KEY: SHIFT_BIT
	M_KEY: SHIFT_BIT
	SPACE_KEY: SPACE_BIT
Combos =
	0: ""  # 
	1: "a" # a
	2: "s" # s
	3: "w" # a+s
	4: "e" # e
	5: "x" # a+e
	6: "d" # s+e
	7: "we" # a+s+e
	8: "t" # t
	9: "f" # a+t
	10: "c" # s+t
	11: "ac" # a+s+t
	12: "r" # e+t
	13: "ar" # a+e+t
	14: "est" # s+e+t
	15: "wr" # a+s+e+t
	16: "n" # n
	17: "q" # a+n
	18: "j" # s+n
	19: "wn" # a+s+n
	20: "y" # e+n
	21: "ay" # a+e+n
	22: "nd" # s+e+n
	23: "and" # a+s+e+n
	24: "b" # t+n
	25: "ab" # a+t+n
	26: "nc" # s+t+n
	27: "can" # a+s+t+n
	28: "be" # e+t+n
	29: "ran" # a+e+t+n
	30: "best" # s+e+t+n
	31: "between" # a+s+e+t+n
	32: "i" # i
	33: "!" # a+i
	34: "z" # s+i
	35: "wi" # a+s+i
	36: "," # e+i
	37: "xi" # a+e+i
	38: "di" # s+e+i
	39: "said" # a+s+e+i
	40: "v" # t+i
	41: "fi" # a+t+i
	42: "ci" # s+t+i
	43: "-" # a+s+t+i
	44: "ve" # e+t+i
	45: "five" # a+e+t+i
	46: "tried" # s+e+t+i
	47: "first" # a+s+e+t+i
	48: "h" # n+i
	49: "ha" # a+n+i
	50: "sh" # s+n+i
	51: "wh" # a+s+n+i
	52: "he" # e+n+i
	53: "-" # a+e+n+i
	54: "she" # s+e+n+i
	55: "when" # a+s+e+n+i
	56: "th" # t+n+i
	57: "that" # a+t+n+i
	58: "ch" # s+t+n+i
	59: "with" # a+s+t+n+i
	60: "the" # e+t+n+i
	61: "have" # a+e+t+n+i
	62: "these" # s+e+t+n+i
	63: "where" # a+s+e+t+n+i
	64: "o" # o
	65: "(" # a+o
	66: "." # s+o
	67: "wo" # a+s+o
	68: "-" # e+o
	69: "xo" # a+e+o
	70: "do" # s+e+o
	72: "g" # t+o
	73: "fo" # a+t+o
	74: "co" # s+t+o
	75: "two" # a+s+t+o
	76: "ro" # e+t+o
	77: "for" # a+e+t+o
	78: "dg" # s+e+t+o
	79: "worst" # a+s+e+t+o
	80: "u" # n+o
	81: "qu" # a+n+o
	82: "su" # s+n+o
	83: "now" # a+s+n+o
	84: "eu" # e+n+o
	86: "du" # s+e+n+o
	87: "down" # a+s+e+n+o
	88: "but" # t+n+o
	89: "fu" # a+t+n+o
	90: "cu" # s+t+n+o
	91: "cannot" # a+s+t+n+o
	92: "ru" # e+t+n+o
	93: "before" # a+e+t+n+o
	94: "course" # s+e+t+n+o
	95: "because" # a+s+e+t+n+o
	96: "l" # i+o
	97: "al" # a+i+o
	98: "sl" # s+i+o
	99: "will" # a+s+i+o
	100: "el" # e+i+o
	102: "dl" # s+e+i+o
	103: "well" # a+s+e+i+o
	104: "gi" # t+i+o
	105: "fl" # a+t+i+o
	106: "cl" # s+t+i+o
	107: "class" # a+s+t+i+o
	108: "rl" # e+t+i+o
	116: "ly" # e+n+i+o
	118: "ould" # s+e+n+i+o
	119: "would" # a+s+e+n+i+o
	120: "tion" # t+n+i+o
	121: "from" # a+t+n+i+o
	122: "such" # s+t+n+i+o
	128: "p" # p
	129: "?" # a+p
	130: ")" # s+p
	132: "'" # e+p
	133: "xp" # a+e+p
	134: "s" # s+e+p
	136: "<Backspace>" # t+p
	139: "past" # a+s+t+p
	140: "rp" # e+t+p
	141: "part" # a+e+t+p
	142: "step" # s+e+t+p
	144: "m" # n+p
	145: "am" # a+n+p
	146: "sm" # s+n+p
	148: "em" # e+n+p
	149: "may" # a+e+n+p
	150: "seem" # s+e+n+p
	151: "same" # a+s+e+n+p
	152: "tm" # t+n+p
	156: "rm" # e+t+n+p
	159: "came" # a+s+e+t+n+p
	160: "k" # i+p
	161: "ak" # a+i+p
	162: "sk" # s+i+p
	163: "ask" # a+s+i+p
	164: "ke" # e+i+p
	167: "asked" # a+s+e+i+p
	170: "ck" # s+t+i+p
	172: "rk" # e+t+i+p
	173: "take" # a+e+t+i+p
	176: "mi" # n+i+p
	177: "main" # a+n+i+p
	181: "make" # a+e+n+i+p
	187: "back" # a+s+t+n+i+p
	188: "them" # e+t+n+i+p
	192: ";" # o+p
	196: "o'" # e+o+p
	200: "top" # t+o+p
	202: "stop" # s+t+o+p
	204: "pro" # e+t+o+p
	208: "mo" # n+o+p
	214: "some" # s+e+n+o+p
	216: "put" # t+n+o+p
	218: "most" # s+t+n+o+p
	220: "more" # e+t+n+o+p
	221: "from" # a+e+t+n+o+p
	222: "come" # s+e+t+n+o+p
	224: "lp" # i+o+p
	240: "lm" # n+i+o+p
	256: "<Shift>" # shift
	257: "A" # a+shift
	258: "S" # s+shift
	259: "W" # a+s+shift
	260: "E" # e+shift
	261: "X" # a+e+shift
	262: "D" # s+e+shift
	264: "T" # t+shift
	265: "F" # a+t+shift
	266: "C" # s+t+shift
	268: "R" # e+t+shift
	272: "N" # n+shift
	273: "Q" # a+n+shift
	274: "J" # s+n+shift
	276: "Y" # e+n+shift
	280: "B" # t+n+shift
	288: "I" # i+shift
	289: "<Ctrl>" # a+i+shift
	290: "Z" # s+i+shift
	292: "<" # e+i+shift
	296: "V" # t+i+shift
	304: "H" # n+i+shift
	320: "O" # o+shift
	321: "<Alt>" # a+o+shift
	322: ">" # s+o+shift
	324: "_" # e+o+shift
	326: "->" # s+e+o+shift
	328: "G" # t+o+shift
	336: "U" # n+o+shift
	346: "constructor"
	352: "L" # i+o+shift
	384: "P" # p+shift
	385: "/" # a+p+shift
	386: "<Esc>" # s+p+shift
	388: '"' # e+p+shift
	392: "\n" # t+p+shift
	400: "M" # n+p+shift
	416: "K" # i+p+shift
	448: ":" # o+p+shift
	480: "->" # i+o+p+shift
	512: " " # space
	768: "\t" # shift+space

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
			MASK_ON(gesture, sticky | KeyMap[evt.keyCode])
			console.log evt.type, "#{evt.keyCode} -> #{KeyMap[evt.keyCode]} + #{sticky} == #{gesture}"
			hasNewKeys = true
			false
		t.bind 'keyup', (evt) ->
			value = Combos[gesture]
			console.log evt.type, "#{evt.keyCode} -> #{KeyMap[evt.keyCode]} + #{sticky} == #{gesture} (#{value})"
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
							MASK_TOGGLE(sticky, SHIFT_BIT)
				else if value?
					c = @caretPos
					@value = $.stringSplice @value, c, c, value
					@caretPos = c + value.length
					modified = true
			code = KeyMap[evt.keyCode]
			MASK_OFF(gesture, code)
			if modified
				MASK_OFF(gesture, sticky)
				sticky = 0
				hasNewKeys = false
			false
