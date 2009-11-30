/*
 * YodaspeakifyServlet.groovy
 */

package com.appspot.yodaspeakify;

import com.google.wave.api.*

class YodaspeakifyServlet extends AbstractRobotServlet {
    // TODO: add more verbs, with various tenses
    private static String wordBank = """
    	accept
    	account
    	achieve
    	act
    	add
    	admit
    	affect
    	afford
    	agree
    	aim
    	allow
    	answer
    	appear
    	apply
    	argue
    	arrange
    	arrive
    	ask
    	attack
    	avoid
    	base
    	be
    	beat
    	become
    	begin
    	believe
    	belong
    	break
    	build
    	burn
    	buy
    	call
    	can
    	care
    	carry
    	catch
    	cause
    	change
    	charge
    	check
    	choose
    	claim
    	clean
    	clear
    	climb
    	close
    	collect
    	come
    	commit
    	compare
    	complain
    	complete
    	concern
    	confirm
    	connect
    	consider
    	consist
    	contact
    	contain
    	continue
    	contribute
    	control
    	cook
    	copy
    	correct
    	cost
    	could
    	count
    	cover
    	create
    	cross
    	cry
    	cut
    	damage
    	dance
    	deal
    	decide
    	deliver
    	demand
    	deny
    	depend
    	describe
    	design
    	destroy
    	develop
    	die
    	disappear
    	discover
    	discuss
    	divide
    	do
    	draw
    	dress
    	drink
    	drive
    	drop
    	eat
    	enable
    	encourage
    	end
    	enjoy
    	examine
    	exist
    	expect
    	experience
    	explain
    	express
    	extend
    	face
    	fail
    	fall
    	fasten
    	feed
    	feel
    	fight
    	fill
    	find
    	finish
    	fit
    	fly
    	fold
    	follow
    	force
    	forget
    	forgive
    	form
    	found
    	gain
    	get
    	give
    	go
    	grow
    	handle
    	happen
    	hate
    	have
    	head
    	hear
    	help
    	hide
    	hit
    	hold
    	hope
    	hurt
    	identify
    	imagine
    	improve
    	include
    	increase
    	indicate
    	influence
    	inform
    	intend
    	introduce
    	invite
    	involve
    	join
    	jump
    	keep
    	kick
    	kill
    	knock
    	know
    	last
    	laugh
    	lay
    	lead
    	learn
    	leave
    	lend
    	let
    	lie
    	like
    	limit
    	link
    	listen
    	live
    	look
    	lose
    	love
    	make
    	manage
    	mark
    	matter
    	may
    	mean
    	measure
    	meet
    	mention
    	might
    	mind
    	miss
    	move
    	must
    	need
    	notice
    	obtain
    	occur
    	offer
    	open
    	order
    	ought
    	own
    	pass
    	pay
    	perform
    	pick
    	place
    	plan
    	play
    	point
    	prefer
    	prepare
    	present
    	press
    	prevent
    	produce
    	promise
    	protect
    	prove
    	provide
    	publish
    	pull
    	push
    	put
    	raise
    	reach
    	read
    	realize
    	receive
    	recognize
    	record
    	reduce
    	refer
    	reflect
    	refuse
    	regard
    	relate
    	release
    	remain
    	remember
    	remove
    	repeat
    	replace
    	reply
    	report
    	represent
    	require
    	rest
    	result
    	return
    	reveal
    	ring
    	rise
    	roll
    	run
    	save
    	say
    	see
    	seem
    	sell
    	send
    	separate
    	serve
    	set
    	settle
    	shake
    	shall
    	share
    	shoot
    	should
    	shout
    	show
    	shut
    	sing
    	sit
    	sleep
    	smile
    	sort
    	sound
    	speak
    	stand
    	start
    	state
    	stay
    	stick
    	stop
    	study
    	succeed
    	suffer
    	suggest
    	suit
    	supply
    	support
    	suppose
    	survive
    	take
    	talk
    	teach
    	tell
    	tend
    	test
    	thank
    	think
    	throw
    	touch
    	train
    	travel
    	treat
    	try
    	turn
    	understand
    	use
    	used to
    	visit
    	vote
    	wait
    	walk
    	want
    	warn
    	wash
    	watch
    	wear
    	will
    	win
    	wish
    	wonder
    	work
    	worry
    	would
    	write
    	"""
    
    // TODO: add rest of relaxed pronunciations
    //http://www.davidtulga.com/contractions.htm
    private static def HashMap<String, String> relaxedPronunciations = ["wanna":"want to", "gunna":"going to", "coulda":"could have", "musta":"must have", "shoulda":"should have", "gotta":"got to", "ya":"you", "y'all":"you all", "lemme":"let me", "oughta":"ought to", "whatcha":"what are you", "getcha":"get you", "gotcha":"got you", "betcha":"bet you", "kinda":"kind of", "gimme":"give me"];
    
    // TODO: add rest of contractions
    //http://www.zoomastronomy.com/grammar/contractions/list.shtml
    private static def HashMap<String, String> contractions = [["i'm":"I am", "i've":"I have", "i'll":"I will", "i'd":"I would", "you'll":"you will", "you've":"you have", "you're":"you are", "we're":"we are", "we've":"we have", "we'll":"we will", "he'll":"he will", "she'll":"she will", "he's":"he is", "she's":"she is", "who'll":"who will", "who's":"who is", "they'll":"they will", "they're":"they are", "that'll":"that will", "that's":"that is", "it's":"it is", "it'll":"it will", "how'll":"how will", "how's":"how is", "where'll":"where will", "where's":"where is", "when'll":"when will", "when's":"when is", "why'll":"why will", "why's":"why is", "what'll":"what will", "what's":"what is"]];
    
    private static def HashMap<String, String> colloquialisms = ["howdy":"hello"]
    
    @Override
    public void processEvents(RobotMessageBundle bundle) {
        Wavelet wavelet = bundle.getWavelet()
        
        // if just added, send greeting
        if (bundle.wasSelfAdded()) {
            Blip blip = wavelet.appendBlip()
            TextView textView = blip.getDocument()
            textView.append("Yodaspeakify I am.  Like me I will make you talk.")
        }
        
        // go through all the blips not submitted by Yodaspeakify and parse them
        for (Event event : bundle.getEvents()) {
            if (event.getType() == EventType.BLIP_SUBMITTED
            && !(event.getModifiedBy()
            .equalsIgnoreCase("yodaspeakify@appspot.com"))) {
                parseBlip(event.getBlip())
            }
        }
    }
    
    /**
     * Method to parse and replace text in blip
     * @param blip the blip to parse
     */
    private void parseBlip(Blip blip) {
        String blipText = blip.getDocument().getText()
        TextView textView = blip.getDocument()
        
        String text = ""
        def sentences = blipText.split(/[?|\!|\.]+/)
        def punctuations = blipText.findAll(/\!|\?|\./)
        sentences.eachWithIndex() { String sentence, int i->
            text += yodaspeakify(sentence.trim() + punctuations[i]) + "  "
        }
        
        textView.replace(blip. text.trim())
    }
    
    public String yodaspeakify(String sentence) {        
        //TODO: load wordbank, contractions, relaxed pronunciations, and colloqialisms from files?
        
        def words = wordBank.split()
        
        // TODO: retain case, find a way to check for all cases of contractions and relaxed pronunciations
        String newSentence = sentence.toLowerCase()
        
        // remove relaxed pronunciations
        relaxedPronunciations.each() { entry ->
            newSentence = newSentence.replaceAll(entry.key, entry.value)
        }
        
        // remove contractions
        contractions.each() { entry ->
            newSentence = newSentence.replaceAll(entry.key, entry.value)
        }
        
        // remove colloquialism
        colloquialisms.each() { entry ->
            newSentence = newSentence.replaceAll(entry.key, entry.value)
        }
        
        // find where to pivot on, pivot on verb furthest into sentence
        int index = -1
        words.each() {String word ->
            String wordToFind = newSentence.find(/$word[ |.|!|?]/)
            int newIndex = newSentence.indexOf("$wordToFind")
            if (newIndex != -1 && newIndex > index) {
                index = newIndex
            }
        }
        
        // if couldn't find a word to pivot on, just return the original
        if (index == -1) {
            return sentence
        }
        
        String end = newSentence.substring(index).trim()
        String front = (newSentence - end).trim()
        
        // move punctuation to end
        Character punctuation = end.charAt(end.length() - 1)
        if (punctuation == '?' || punctuation == '!' || punctuation == '.') {
            end = end.replace("$punctuation", "")
            newSentence = "$end $front$punctuation"
            // add some extra Yoda effects, but only sometimes
            if (punctuation == '?') {
                int y_n = Math.random() * 2
                if (y_n == 0)
                    newSentence += " Hmmmm?"
            }
        } else {
            newSentence = "$end $front"
        }
        
        // capitalize new first word
        String firstWord = newSentence.find(/\w+/)
        Character firstCharacter = firstWord.charAt(0)
        newSentence = newSentence.replaceFirst("$firstCharacter", "${firstCharacter.toUpperCase()}")
        
        // capitalize stuff that's always capital
        newSentence = newSentence.replaceAll("i ", "I ").replaceAll(" i", " I")
        
        return newSentence
    }
}