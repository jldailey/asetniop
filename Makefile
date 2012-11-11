COFFEE=node_modules/.bin/coffee
COFFEEJS=js/coffee-script.js
BLING=node_modules/bling/dist/bling.js
D3JS=js/d3.js
MOCHA=node_modules/.bin/mocha
MOCHA_OPTS=--compilers coffee:coffee-script --globals document,window,Bling,$$,_ -R dot
SRC_FILES=$(shell ls *.coffee)
TEST_FILES=$(shell ls test/*.coffee 2> /dev/null)
FILTER_COMMENTS=grep -v '^\s*\# ' | perl -ne 's/^\s*[\#]/\#/p; print'
PREPROC=cpp

all: js/asetniop.js $(COFFEEJS) $(D3JS)

test: all test/pass
	@echo "All tests are passing."

test/pass: $(MOCHA) $(SRC_FILES) $(TEST_FILES)
	$(MOCHA) $(MOCHA_OPTS) $(TEST_FILES) && touch test/pass

js:
	mkdir -p js

js/asetniop.js: js $(SRC_FILES) $(COFFEE) Makefile
	@mkdir -p stage js
	@for file in $(SRC_FILES); do cat $$file | $(FILTER_COMMENTS) > stage/$$file; done
	@(cd stage && cat asetniop.coffee | $(FILTER_COMMENTS) | $(PREPROC) | ../$(COFFEE) -sc > ../$@)

js/bling.js: js $(BLING)
	cp $(BLING) js/bling.js

$(COFFEEJS): js $(COFFEE)
	curl http://coffeescript.org/extras/coffee-script.js > $@

$(COFFEE):
	npm install coffee-script
	# PATCH: avoid a warning message from the coffee compiler
	sed -ibak -e 's/path.exists/fs.exists/' node_modules/coffee-script/lib/coffee-script/command.js
	rm -f node_modules/coffee-script/lib/coffee-script/command.js.bak

$(BLING):
	npm install bling

$(MOCHA):
	npm install mocha

$(D3JS):
	curl http://d3js.org/d3.v2.min.js > $@

clean:
	rm -rf js
	rm -rf node_modules
