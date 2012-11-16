
#include "../game.coffee"

window.game = new Game "canvas"
window.wordlist = [ # should read this from a file
	"one",
	"two",
	"three"
]
window.words = {}
class RainingWord
	constructor: (@word, @w, @h) ->
		window.words[@word] = @
		@x = $.math.random 0, @w
		@y = 0
		@speed = .01
	tick: (dt) ->
		@y += @speed * dt
		if @y > @h
			@emit 'die'
	draw: (ctx) ->
		ctx.fillText @x, @y, @word

$.interval 1000, ->
	window.game.add new RainingWord(
		$.random.element(window.wordlist),
		window.game.w,
		window.game.h
	)

