
#include "./clock.coffee"

class Game extends Clock
	constructor: (selector) ->
		@canvas = $(selector).first()
		@ctx = @canvas.getContext('2d')
		@w = @canvas.getAttribute('width')
		@h = @canvas.getAttribute('height')
		@objects = $()
		@dying = $()
		@on 'tick', (dt) =>
			obj.tick() for obj in @objects
			@remove(obj) for obj in @dying
			obj.draw() for obj in @objects
	add: (obj) ->
		obj.on? 'die', => @dying.push obj
		@objects.push obj
	remove: (obj) ->
		if (i = @objects.indexOf obj) > -1
			@objects.splice i, 1

	
