
# A Clock emits 'tick' events at 60Hz
class Clock extends $.EventEmitter
	requestInterval = window.requestAnimationFrame or
		window.mozRequestAnimationFrame or
		window.webkitRequestAnimationFrame or
		window.msRequestAnimationFrame or
		(f) -> window.setTimeout f, 1000/60

	cancelInterval = window.cancelAnimationFrame or
		window.mozCancelAnimationFrame or
		window.webkitCancelAnimationFrame or
		window.msCancelAnimationFrame or
		window.clearTimeout

	constructor: ->
		@interval = null
	start: ->
		return if @interval
		t = $.now
		do ticker = =>
			t += (dt = $.now - t)
			@emit 'tick', dt
			@interval = requestInterval ticker
		@
	stop: ->
		return unless @interval
		cancelInterval @interval
		@interval = null
		@
	toggle: ->
		if @interval then @stop() else @start()
