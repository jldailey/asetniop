
class O extends $.EventEmitter
	tick: ->
	draw: ->

class FallingWord extends O
	constructor: (@word, range) ->
		super @
		@x = $.random.integer 0, range
		@y = 0
		@speed = .01
	tick: (dt) ->
		@y += @speed * dt
	draw: (ctx) ->
		ctx.fillText @x, @y, @word

class TimedScript extends O
	constructor: (@script) ->
		@elapsed = 0
		@cur = 0
	tick: (dt) ->
		@elapsed += dt
		while @cur < @script.length and @script[@cur][0] < @elapsed
			item = @script[@cur]
			switch item[1]
				when 'goto' then @elapsed = item[2] | 0
				else do item[1]

class Game extends Clock
	constructor: (selector) ->
		@canvas = $(selector).first()
		@ctx = @canvas.getContext('2d')
		@w = @canvas.getAttribute('width')
		@h = @canvas.getAttribute('height')
		@objects = []
	add: (obj) ->
		@objects.push obj
		obj.on 'die', =>
			if (i = @objects.indexOf obj) > -1
				@objects.splice i, 1

wordlist = ["one", "two", "three"]
game = new Game "canvas"
game.add new TimedScript [
	[ 0, -> game.add new FallingWord $.random.element(wordlist), game.w ],
	[ 1000, 'goto', 0 ]
]
