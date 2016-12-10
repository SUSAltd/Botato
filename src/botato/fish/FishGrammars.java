package botato.fish;

import java.util.Arrays;
import java.util.Calendar;

import botato.fish.Holiday.HolidayName;


/**
 * A class containing grammars for fish (includes special holiday-themed fish if you like to get festive)
 * 
 * @author Ray
 *
 */
public class FishGrammars {
	
	private static final String[] FISH_AND_ADJECTIVES = new String[] {
		"<adj> ::= amazing | armoured | awkward | beached | bigeye | blanched | chocolate-covered | cool | crunchy | dangerous | dead | delicious | delirious | despicable | dirty | electric | electronic | explosive | expressive | fabulous | fallacious | fishy | flawless | frozen | genetically modified | glass | glow-in-the-dark | glowing | greedy | gregarious | hairy | holy | keen | lazy | leopard-spotted | literary | lovely | magical | metal | musical | normal | nice | nosey | one-legged | oppressed | optimal | pampered | peaceful | poisonous | professional | quintessential | radiant | radioactive | red-hot | rich | robotic | scaly | scandalous | scorched | secret | self-cleaning | sharp | shiny | slimy | slippery | slithering | smoked | sophisticated | special | spike-tailed | spiky | squishy | steamed | stinky | three-legged | toothed | treacherous | uncomfortable | unique | wiggly | winged | wise | yellowish",
		"<adj_big> ::= big | big-boned | bloated | bulky | burly | capacious | chubby | colossal | commodious | enormous | fat | gargantuan | gigantic | ginormous | heavy | heavyweight | hefty | huge | hulking | humongous | immense | jumbo | massive | mega | muscly | oversized | prodigious | school of | sizable | substantial | super-colossal | tremendous | voluminous | well-endowed | whopper",
		"<adj_sml> ::= baby | diminutive | eensy-weensy | gaunt | gimpy | inconsiderable amount of | itsy-bitsy | light-weight | little | malnourished | miniature | newborn | petite | puny | skinny | small | teeny | tiny | tiny baby",
		
		"<adj_huge_1> ::= absolute monstrosity of a | Ahab's prized | legendary beast known only as the | Neptune's own | Poseidon's own",
		"<adj_huge_2> ::= as was foretold long ago | for the history books | marked for an eternity of greatness | of legends untold | spoken of only as a myth | the likes of which have never been seen before | thought to be nothing more than a sailor's tale | whose name will only ever be uttered with an air of dread",
		
		"<fish> ::= aholehole | albacore | algae-eater | alligatorfish | anchovy | anenomefish | angelfish | angler | archerfish | armorfish | bandfish | barracuda | barreleye | bass | beardfish | blackfish | bonytail chub | buffalofish | butterflyfish | cardinalfish | carp | carpsucker | catfish | cavefish | cherubfish | chubsucker | clownfish | cod | coelacanth | coffinfish | combfish | cornetfish | cowfish | cutlassfish | damselfish | dartfish | dogfish | dottyback | dragonet | dragonfish | eagle ray | eel | electric red | electric ray | elephant fish | elephantnose fish | fangtooth | featherback | filefish | fingerfish | fish | flagfin | flagfish | flashlight fish | flathead | flounder | flyingfish | footballfish | four-eyed fish | frogfish | gibberfish | glassfish | goby | goldfish | goosefish | gourami | graveldiver | guitarfish | gulper | gunard | guppy | hagfish | halibut | handfish | hatchetfish | hawkfish | herring | hogsucker | icefish | jackfish | jawfish | jellyfish | jellynose fish | jewelfish | Jewfish | kelpfish | killifish | kingfish | knifefish | labyrinth fish | ladyfish | lampfish | lamprey | lancetfish | lanternfish | leaffish | lightfish | lionfish | lizardfish | loach | longfin | lungfish | mackerel | Mandarin fish | manefish | man-of-war fish | manefish | manta ray | marblefish | marlin | medusafish | milkfish | minnow | mudminnow | mustache triggerfish | needlefish | nibblefish | nurseryfish | oarfish | oilfish | paradise fish | parrotfish | pearlfish | pencilfish | perch | pigfish | pike | pike conger | pilotfish | pineapplefish | pineconefish | piranha | pipefish | platyfish | plunderfish | pollyfish | ponyfish | poolfish | porcupinefish | pricklefish | prowfish | pufferfish | pupfish | quillfish | rabbitfish | ragfish | rainbowfish | ray | razorfish | redfish | reedfish | ricefish | rockfish | roosterfish | ropefish | rudderfish | sabertooth fish | sablefish | sailfish | salmon | sand diver | sandfish | scabbardfish | scalyfin | scorpionfish | shark | shrimpfish | silverside | skilfish | smelt | smoothtongue | snailfish | snipefish | soldierfish | sole | spadefish | spiderfish | spikefish | spookfish | springfish | squaretail | squawfish | squirrelfish | stingfish | stingray | stonefish | sturgeon | sunfish | surgeonfish | swordfish | swordtail | tadpolefish | telescopefish | thornfish | tilefish | tonguefish | torrentfish | treefish | triggerfish | tripodfish | trout | trumpetfish | trunkfish | unicornfish | velvetfish | viperfish | waryfish | weatherfish | weeverfish | whalefish | whitefish | wormfish | x-ray fish | yellowtail | zebrafish | 39fish | caretfish | jefffish | legofish | shamanfish | nwinfish | rm -rfish | satrixfish | sigmafish | susafish | tetrafish | zoraelfish",

		"<adj_420> ::= 420 | 81 x 7 | baked | blazed-up | blazing | burning | dank | high | lit-up | peaced-out | smoking | stoned | toked",
		"<fish_420> ::= 420fish | blazefish | bluntfish | bongfish | bowl of herb | cannabisfish | entfish | hempfish | jointfish | marijuanafish | Mary Jane | memefish | potfish | resident of Colorado | resident of Washington | weedfish"
	};
	
	private static final String[] FISH_ADJ_VLTN = new String[] {
		"<adj_h> ::= amorous | dreamy | enamoring | forever alone | heart-shaped | laced | lovey-dovey | loving | lovely | luscious | perfumed | romantic | St. Valentine's own | sweet | true",
		"<fish_h> ::= box of chocolate fish | cupidfish | fish of love | heartfish | kissfish | love letter | secret admirer | sole mate | Valentinefish"
	};
	
	private static final String[] FISH_ADJ_PTRK = new String[] {
		"<adj_h> ::= 777 | four-leafed | golden | green | Irish | lucky | pot o' | rainbow | St. Patrick's own",
		"<fish_h> ::= blarneyfish | charm | cloverfish | horseshoe | leprechaunfish | rabbit's foot | shamrockfish"
	};
	
	private static final String[] FISH_ADJ_HLWN = new String[] {
		"<adj_h> ::= 2spooky | apparitional | bloodcurling | cannibalized | chilling | creepy | cryptic | dark | Death's own | demonic | eerie | evil | ghastly | ghostly | grim | haunted | hellish | horrific | infernal | lifeless | mummified | mysterious | necromantic | nightmarish | occult | rotting | scary | skeletal | soulless | spectral | spine-chilling | splattered | spooky | spooky scary | staring | supernatural | terrifying | undead | unholy | voodoo-cursed | whispering | wicked | zombie",
		"<fish_h> ::= bloodfish | bonefish | corpsefish | deathfish | demonfish | devilfish | exorcistfish | face in the dark | fish costume | fish soul | fish-o-lantern | Frankenfish | full moon | ghostfish | goblinfish | Hellfish | horrorfish | monsterfish | mummyfish | necrofish | nightfish | pumpkinfish | reaperfish | shadowfish | shiver down the spine | skeletonfish | skullfish | spookfish | thirteenth hour | vampirefish | werefish | witchfish | zombiefish",
	};
	
	private static final String[] FISH_ADJ_XMAS = new String[] {
		
		"<adj_h> ::= blizzardy | chilly | festive | frosted | frosty | gift-giving | icy | jingling | jolly | merry | naughty | nice | nippy | one-horse open | Saint Nick's own | snowy | red-nosed | wintry | yuletide",
		"<fish_h> ::= Christmasfish | elffish | iciclefish | Grinchfish | Jesusfish | jinglefish | ornamentfish | presentfish | reindeerfish | Santafish | Scroogefish | sleighfish | snowfish | snowmanfish | tinselfish | winterfish"
	};
	
	private static final String[] FISH_GRAMMAR_DEFAULT = concat(FISH_AND_ADJECTIVES, new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj> <fish>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj> <fish>",
		"<fish_avg> ::= <adj> <fish>",
		
		"<fish_big_420> ::= <adj_big> <fish_420> | <adj_big> <adj> <fish_420> | <adj_big> <adj_420> <fish> | <adj_big> <adj_420> <fish_420>",
		"<fish_sml_420> ::= <adj_sml> <fish_420> | <adj_sml> <adj> <fish_420> | <adj_sml> <adj_420> <fish> | <adj_sml> <adj_420> <fish_420>",
		"<fish_avg_420> ::= <adj_420> <fish> | <adj> <fish_420> | <adj_420> <fish_420>",
		
		"<fh2> ::= <adj> <fish> <adj_huge_2>",
		"<fish_huge> ::= <adj_huge_1> <adj> <fish> | <fh2> | <fh2>"
	});
	
	private static final String[] FISH_GRAMMAR_HOLIDAY = concat(FISH_AND_ADJECTIVES, new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj> <fish> | <adj_big> <adj_h> <fish> | <adj_big> <fish_h> | <adj_big> <adj_h> <fish_h>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj> <fish> | <adj_sml> <adj_h> <fish> | <adj_sml> <fish_h> | <adj_sml> <adj_h> <fish_h>",
		"<fish_avg> ::= <adj> <fish> | <adj_h> <fish> | <adj> <fish_h> | <adj_h> <fish_h>",

		"<fish_big_420> ::= <adj_big> <fish_420> | <adj_big> <adj> <fish_420> | <adj_big> <adj_420> <fish> | <adj_big> <adj_420> <fish_420> | <adj_big> <adj_420> <fish_h> | <adj_big> <adj_h> <fish_420>",
		"<fish_sml_420> ::= <adj_sml> <fish_420> | <adj_sml> <adj> <fish_420> | <adj_sml> <adj_420> <fish> | <adj_sml> <adj_420> <fish_420> | <adj_sml> <adj_420> <fish_h> | <adj_sml> <adj_h> <fish_420>",
		"<fish_avg_420> ::= <adj_420> <fish> | <adj> <fish_420> | <adj_420> <fish_420> | <adj_h> <fish_420> | <adj_420> <fish_h> | <adj_h> <fish_h>",
		
		"<fish_huge> ::= <adj_huge_1> <adj> <fish> | <adj_huge_1> <adj_h> <fish> | <adj_huge_1> <adj> <fish_h> | <adj_huge_1> <adj_h> <fish_h> | <adj> <fish> <adj_huge_2> | <adj_h> <fish> <adj_huge_2> | <adj> <fish_h> <adj_huge_2> | <adj_h> <fish_h> <adj_huge_2>",
	});
	
	private static final Holiday[] HOLIDAYS = new Holiday[] {
		new Holiday(HolidayName.VALENTINE, 2, 9, 2, 15),
		new Holiday(HolidayName.PATRICK, 3, 12, 3, 18),
		new Holiday(HolidayName.HALLOWEEN, 10, 1, 11, 1),
		new Holiday(HolidayName.CHRISTMAS, 12, 1, 1, 1),
	};
	
	public static String[] getFishGrammar() {
		Calendar currDate = Calendar.getInstance();
		for (Holiday h : HOLIDAYS) {
			if (h.getStartDate().before(currDate) && h.getEndDate().after(currDate)) {
				return getFishGrammar(h.getName());
			}
		}
		return getFishGrammar(HolidayName.DEFAULT);
	}
	
	public static String[] getFishGrammar(HolidayName h) {
		String[] grammar;
		switch (h) {
		case VALENTINE:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_VLTN);
			break;
		case PATRICK:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_PTRK);
			break;
		case HALLOWEEN:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_HLWN);
			break;
		case CHRISTMAS:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_XMAS);
			break;
		case DEFAULT:
		default:
			grammar = FISH_GRAMMAR_DEFAULT;
			break;
		}
		
		return grammar;
	}
	
	private static String[] concat(String[] first, String[] second) {
		String[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
