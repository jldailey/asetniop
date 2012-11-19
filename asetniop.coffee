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


$.get "../gestures.json", (data) ->
	MaskToOutput = JSON.parse(data)

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
