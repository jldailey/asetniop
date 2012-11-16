#define MASK_HAS(mask, bits) ((mask & bits) is bits)
#define MASK_ON(mask, bits) (mask = mask | bits)
#define MASK_OFF(mask, bits) (mask = mask ^ (mask & bits))
#define MASK_TOGGLE(mask, bits) (mask = mask ^ bits)

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
# for better use in writing code, I consider the right shifts to be a Numeric Shift
#define NUMSH_BIT 1024

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
#define CAPS_KEY 17

KeyNames =
	A_KEY: "a"
	Q_KEY: "a"
	S_KEY: "s"
	W_KEY: "s"
	D_KEY: "e"
	E_KEY: "e"
	F_KEY: "t"
	R_KEY: "t"
	J_KEY: "n"
	U_KEY: "n"
	K_KEY: "i"
	I_KEY: "i"
	L_KEY: "o"
	O_KEY: "o"
	SEMICOLON_KEY: "p"
	P_KEY: "p"
	C_KEY: "shift"
	V_KEY: "shift"
	N_KEY: "number"
	M_KEY: "number"
	SPACE_KEY: "space"

# each key in MaskToOutput is a bitmask
KeyToBit =
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
	N_KEY: NUMSH_BIT
	M_KEY: NUMSH_BIT
	SPACE_KEY: SPACE_BIT

MaskToOutput =
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
	27: "can" # a+s+t+n
	28: "be" # e+t+n
	32: "i" # i
	33: "!" # a+i
	34: "z" # s+i
	35: "wi" # a+s+i
	36: "," # e+i
	37: "xi" # a+e+i
	38: "di" # s+e+i
	40: "v" # t+i
	41: "fi" # a+t+i
	42: "ci" # s+t+i
	44: "ve" # e+t+i
	48: "h" # n+i
	49: "ha" # a+n+i
	50: "sh" # s+n+i
	51: "wh" # a+s+n+i
	52: "he" # e+n+i
	54: "she" # s+e+n+i
	56: "th" # t+n+i
	57: "that" # a+t+n+i
	58: "this" # s+t+n+i
	60: "the" # e+t+n+i
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
	76: "or" # e+t+o
	77: "for" # a+e+t+o
	78: "dg" # s+e+t+o
	80: "u" # n+o
	81: "qu" # a+n+o
	82: "su" # s+n+o
	83: "now" # a+s+n+o
	84: "eu" # e+n+o
	86: "du" # s+e+n+o
	88: "but" # t+n+o
	89: "fu" # a+t+n+o
	90: "cu" # s+t+n+o
	92: "ru" # e+t+n+o
	96: "l" # i+o
	97: "al" # a+i+o
	98: "sl" # s+i+o
	100: "el" # e+i+o
	102: "dl" # s+e+i+o
	104: "gi" # t+i+o
	105: "fl" # a+t+i+o
	106: "cl" # s+t+i+o
	107: "class" # a+s+t+i+o
	108: "rl" # e+t+i+o
	116: "ly" # e+n+i+o
	120: "tion" # t+n+i+o
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
	152: "tm" # t+n+p
	156: "rm" # e+t+n+p
	160: "k" # i+p
	161: "ak" # a+i+p
	162: "sk" # s+i+p
	164: "ke" # e+i+p
	170: "ck" # s+t+i+p
	172: "rk" # e+t+i+p
	176: "mi" # n+i+p
	192: "\t" # o+p
	193: "="  # a+o+p
	196: "->" # e+o+p
	208: "mo" # n+o+p
	216: "put" # t+n+o+p
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
	316: "The" # e+t+n+i+shift
	320: "O" # o+shift
	321: "[" # a+o+shift
	322: ">" # s+o+shift
	324: "_" # e+o+shift
	328: "G" # t+o+shift
	336: "U" # n+o+shift
	346: "constructor"
	352: "L" # i+o+shift
	384: "P" # p+shift
	385: "/" # a+p+shift
	386: "]" # s+p+shift
	388: '"' # e+p+shift
	400: "M" # n+p+shift
	416: "K" # i+p+shift
	448: ":" # o+p+shift
	512: " " # space
	768: "\n" # shift+space
	1024: "<Number>" # number
	1072: "\t" # number+n+i
	1089: "{" # number+a+o
	1154: "}" # number+s+p
	1280: "0" # number+shift

# map in all the "a " combos
for i in [513...1024] when (j = i - 512) of MaskToOutput and MaskToOutput[j].length < 3
	MaskToOutput[i] = MaskToOutput[j] + " "

# map in all the ASCII numbers for 1-15
for i in [1025..(1025+15)]
	MaskToOutput[i] ?= "" + (i - 1024) # each ascii number "n" is number+<bits of n>

reverseMap = (map) ->
	ret = Object.create(null)
	for k,v of map
		unless v of ret
			ret[v] = k
	ret
BitToKey = reverseMap KeyToBit
OutputToMask = reverseMap MaskToOutput

maskToKeys = (mask) ->
	(KeyNames[BitToKey[i]] for i in [256, 1024, 1, 2, 4, 8, 16, 32, 64, 128, 512] when MASK_HAS(mask, i)).join "+"

getCaretPos = (el) ->
	el.focus()
	switch true
		when 'selection' of document
			sel = document.selection.createRange()
			sel.moveStart 'character', el.value.length
			sel.text.length
		when 'selectionStart' of el
			el.selectionStart
		else
			el.value.length

setCaretPos = (el, pos) ->
	switch true
		when 'setSelectionRange' of this
			el.focus()
			el.setSelectionRange(pos,pos)
		when 'createTextRange' of this
			range = el.createTextRange()
			range.collapse(true)
			range.moveEnd('character', pos)
			range.moveStart('character', pos)
			range.select()

$.asetniop =
	init: (selector) ->
		$(selector).log('binding ASETNIOP to:').each ->
			gesture = 0 # bit mask of all the keys pressed during one gesture
			sticky = 0 # bit mask of the keys that are in 'sticky' state
			lock = 0 # stickier than sticky, this is for things like caps lock
			hasNewKeys = false # a flag that lets us ignore keyup events on keys that were just used as a gesture

			t = $(@)
			t.bind 'keydown', (evt) ->
				key = KeyToBit[evt.keyCode]
				if evt.keyCode is CAPS_KEY
					MASK_TOGGLE(lock, SHIFT_BIT)
				# if the key was already down, then this is really a repeat
				if MASK_HAS(gesture, key)
					t.trigger 'keyup', evt # so fire the same if it were being pressed/released really fast
				hasNewKeys = true
				# record the key press in the current gesture
				MASK_ON(gesture, sticky | lock | key )
				# log everything
				# $.log evt.type, "#{evt.keyCode} -> #{maskToKeys KeyToBit[evt.keyCode]}#{if sticky then " + " + maskToKeys sticky else ""} == #{gesture} (#{maskToKeys(gesture)})"
				false
			t.bind 'keyup', (evt) ->
				# get the output value of the current gesture
				value = MaskToOutput[gesture]
				# log everything
				# $.log evt.type, "#{evt.keyCode} -> #{maskToKeys KeyToBit[evt.keyCode]}#{if sticky then " + " + maskToKeys sticky else ""} == #{gesture} (#{maskToKeys(gesture)})#{if hasNewKeys then " -> " + value else ""}"
				modified = false
				if hasNewKeys
					if /^<\w+>$/.test value
						switch value
							when "<Backspace>"
								c = getCaretPos @
								@value = $.stringSplice @value, c-1, c, ''
								setCaretPos @, c - 1
								modified = true
							when "<Shift>"
								MASK_TOGGLE(sticky, SHIFT_BIT)
							when "<Number>"
								MASK_TOGGLE(sticky, NUMSH_BIT)
					else if value?
						c = getCaretPos @
						@value = $.stringSplice @value, c, c, value
						setCaretPos @, c + value.length
						modified = true
				code = KeyToBit[evt.keyCode]
				MASK_OFF(gesture, code)
				if modified
					MASK_OFF(gesture, sticky)
					sticky = 0
					hasNewKeys = false
					t.trigger 'change'
				false
		
	hint: (output) -> # returns the list of keys to press to produce output
		switch
			when output of OutputToMask
				maskToKeys OutputToMask[output]
			when output.length > 1
				# find the longest single chunk on the end
				j = output.length - 1
				--j while j >= 0 and output[j...output.length] of OutputToMask
				++j
				# split into two chunks
				a = output[0...j] # the unprocessed head
				b = output[j...output.length] # the largest match on the end
				$.asetniop.hint(a) + ", " + $.asetniop.hint(b)
			else ""

$(document).ready -> $.asetniop.init(".asetniop")
