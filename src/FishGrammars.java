import java.util.Arrays;


/**
 * @author Ray
 *
 * A class containing grammars for fish (includes special holiday-themed fish if you like to get festive)
 */
public class FishGrammars {
	
	private static final String[] FISH_AND_ADJECTIVES = new String[] {
		"<adj> ::= amazing | armoured | awkward | beached | bigeye | blanched | burning | chocolate-covered | cool | dangerous | dead | delirious | despicable | electric | electronic | explosive | expressive | fabulous | fallacious | flawless | frozen | genetically modified | glass | glow-in-the-dark | glowing | greedy | gregarious | hairy | high | insidious | jocular | keen | lazy | leopard-spotted | literary | lovely | magical | malnourished | metal | musical | normal | nosey | noxious | one-legged | oppressed | optimal | oxidized | pampered | peaceful | poisonous | professional | quintessential | radiant | radioactive | rainbow | red-hot | resplendent | rich | robotic | scaly | scorched | scorching | secret | shiny | slippery | slithering | smoked | spike-tailed | spiky | steamed | steaming | three-legged | unanimous | uncomfortable | unique | volatile | wicked | wiggly | winged | wise | yellowish | zealous",
		"<adj_big> ::= big | big-boned | bloated | bulky | burly | capacious | chubby | colossal | commodious | enormous | fat | gargantuan | gigantic | ginormous | heavy | heavyweight | hefty | huge | hulking | humongous | immense | jumbo | massive | mega | muscly | oversized | prodigious | sizable | substantial | super-colossal | tremendous | voluminous | well-endowed | whopper",
		"<adj_sml> ::= baby | diminutive | eensy-weensy | gaunt | gimpy | inconsiderable amount of | itsy-bitsy | light-weight | little | miniature | newborn | petite | puny | skinny | small | teeny | tiny | tiny baby",
		
		"<adj_huge_1> ::= absolute monstrosity of a | Neptune's own | Poseidon's own",
		"<adj_huge_2> ::= as was foretold long ago | for the history books | marked for an eternity of greatness | of legends untold | spoken of only as a myth | the likes of which have never been seen before | thought to be nothing but a sailor's tale | whose name will only ever be uttered with an air of fear",
		
		"<fish> ::= aholehole | albacore | algae-eater | alligatorfish | anchovy | anenomefish | angelfish | angler | archerfish | armorfish | bandfish | barracuda | barreleye | bass | beardfish | blackfish | bonytail chub | buffalofish | butterflyfish | cardinalfish | carp | carpsucker | catfish | cavefish | cherubfish | chubsucker | clownfish | cod | coelacanth | coffinfish | combfish | cornetfish | cutlassfish | damselfish | dartfish | dhufish | dogfish | dottyback | dragonet | dragonfish | eagle ray | eel | electric red | electric ray | elephant fish | elephantnose fish | fangtooth | featherback | filefish | fingerfish | fish | flagfin | flagfish | flashlight fish | flathead | flounder | flyingfish | footballfish | four-eyed fish | frogfish | gibberfish | glassfish | goby | goldfish | goosefish | gourami | graveldiver | guitarfish | gulper | gunard | guppy | hagfish | halibut | handfish | hatchetfish | hawkfish | herring | hogsucker | icefish | jackfish | jawfish | jellyfish | jellynose fish | jewelfish | Jewfish | kelpfish | killifish | kingfish | knifefish | labyrinth fish | ladyfish | lampfish | lamprey | lancetfish | lanternfish | leaffish | lightfish | lionfish | lizardfish | loach | longfin | lungfish | mackerel | Mandarin fish | manefish | man-of-war fish | manefish | manta ray | marblefish | marlin | medusafish | milkfish | minnow | mudminnow | mustache triggerfish | needlefish | nibblefish | nurseryfish | oarfish | oilfish | paradise fish | parrotfish | pearlfish | pencilfish | perch | pigfish | pike | pike conger | pilotfish | pineapplefish | pineconefish | piranha | pipefish | platyfish | plunderfish | pollyfish | ponyfish | poolfish | porcupinefish | pricklefish | prowfish | pufferfish | pupfish | quillfish | rabbitfish | ragfish | rainbowfish | ray | razorfish | redfish | reedfish | ricefish | rockfish | roosterfish | ropefish | rudderfish | sabertooth fish | sablefish | sailfish | salmon | sand diver | sandfish | scabbardfish | scalyfin | scorpionfish | shark | shrimpfish | silverside | skilfish | smelt | smoothtongue | snailfish | snipefish | soldierfish | sole | spadefish | spiderfish | spikefish | spookfish | springfish | squaretail | squawfish | squirrelfish | stingfish | stingray | stonefish | sturgeon | sunfish | surgeonfish | swordfish | swordtail | tadpolefish | telescopefish | thornfish | tilefish | tonguefish | torrentfish | treefish | triggerfish | tripodfish | trout | trumpetfish | trunkfish | unicornfish | velvetfish | viperfish | waryfish | weatherfish | weeverfish | whalefish | whitefish | wormfish | x-ray fish | yellowtail | zebrafish | 39fish | caretfish | jefffish | legofish | shamanfish | nwinfish | rm -rfish | satrixfish | susafish | tejfish | tetrafish | zoraelfish",
	};
	
	private static final String[] FISH_ADJ_VLTN = new String[] {
		
		"<adj_h> ::= amorous | dreamy | enamoring | forever alone | heart-shaped | laced | lovey-dovey | loving | luscious | perfumed | romantic | sweet | true",
		"<fish_h> ::= box of chocolates | cupidfish | fish of love | heartfish | love letter | secret admirer | Valentinefish"
	};
	
	private static final String[] FISH_ADJ_HLWN = new String[] {
		"<adj_h> ::= 2spooky | apparitional | bloodcurling | creepy | cryptic | demonic | eerie | ghastly | ghostly | haunted | mummified | mysterious | scary | skeletal | spectral | spine-chilling | spooky | supernatural | undead | voodoo-cursed | zombie",
		"<fish_h> ::= bonefish | corpsefish | demonfish | devilfish | exorcistfish | ghostfish | horrorfish | mummyfish | shadowfish | skeletonfish | skullfish | thirteenth hour | vampirefish | witchfish | zombiefish",
		
	};
	
	private static final String[] FISH_ADJ_XMAS = new String[] {
		
		"<adj_h> ::= chilly | festive | frosty | gift-giving | happy | icy | jingling | jolly | merry | naughty | nice | nippy | snowy | red-nosed | wintry",
		"<fish_h> ::= Christmasfish | elffish | iciclefish | Grinchfish | jinglefish | ornamentfish | presentfish | reindeerfish | Santafish | Scroogefish | sleighfish | snowfish | snowmanfish | tinselfish | winterfish"
	};
	
	private static final String[] FISH_GRAMMAR_DEFAULT = concat(FISH_AND_ADJECTIVES, new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj> <fish>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj> <fish>",
		"<fish_avg> ::= <adj> <fish>",
		
		"<fish_huge> ::= <adj_huge_1> <adj> <fish> | <adj> <fish> <adj_huge_2>"
	});
	
	private static final String[] FISH_GRAMMAR_HOLIDAY = concat(FISH_AND_ADJECTIVES, new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj> <fish> | <adj_big> <adj_h> <fish> | <adj_big> <fish_h> | <adj_big> <adj_h> <fish_h>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj> <fish> | <adj_sml> <adj_h> <fish> | <adj_sml> <fish_h> | <adj_sml> <adj_h> <fish_h>",
		"<fish_avg> ::= <adj> <fish> | <adj_h> <fish> | <adj_avg> <fish_h> | <adj_h> <fish_h>",

		"<fish_huge> ::= <adj_huge_1> <adj> <fish> | <adj_huge_1> <adj_h> <fish> | <adj_huge_1> <adj> <fish_h> | <adj_huge_1> <adj_h> <fish_h> | <adj> <fish> <adj_huge_2> | <adj_h> <fish> <adj_huge_2> | <adj> <fish_h> <adj_huge_2> | <adj_h> <fish_h> <adj_huge_2>",
	});
	
	public static enum Holiday {
		
		/**
		 * Valentine's Day
		 */
		VALENTINE,
		
		
		/**
		 * Halloween
		 */
		HALLOWEEN,
		
		
		/**
		 * Christmas
		 */
		CHRISTMAS,
		
		
		/**
		 * No particular holiday
		 */
		DEFAULT
	}
	
	public static String[] getFishGrammar(Holiday h) {
		String[] grammar;
		switch (h) {
		case VALENTINE:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_VLTN);
			break;
		case HALLOWEEN:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_HLWN);
			break;
		case CHRISTMAS:
			grammar = concat(FISH_GRAMMAR_HOLIDAY, FISH_ADJ_XMAS);
			break;
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
