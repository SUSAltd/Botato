import java.util.Arrays;


/**
 * @author Ray
 *
 * A class containing grammars for fish (includes special holiday-themed fish if you like to get festive)
 */
public class FishGrammars {
	
	private static final String[] FISH_GRAMMAR_HLWN = new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj_hlwn> <fish> | <adj_big> <fish_hlwn> | <adj_big> <adj_hlwn> <fish_hlwn>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj_hlwn> <fish> | <adj_sml> <fish_hlwn> | <adj_sml> <adj_hlwn> <fish_hlwn>",
		"<fish_avg> ::= <adj_avg> <fish> | <adj_hlwn> <fish> | <adj_avg> <fish_hlwn> | <adj_hlwn> <fish_hlwn>",
		
		"<adj_hlwn> ::= 2spooky | apparitional | bloodcurling | creepy | cryptic | demonic | eerie | ghastly | ghostly | haunted | mummified | mysterious | scary | skeletal | spectral | spine-chilling | spooky | supernatural | undead | voodoo-cursed | zombie",
		"<fish_hlwn> ::= bonefish | corpsefish | demonfish | devilfish | ghostfish | horrorfish | mummyfish | shadowfish | skeletonfish | skullfish | thirteenth hour | vampirefish | witchfish | zombiefish",
		
	};
	
	private static final String[] FISH_GRAMMAR_XMAS = new String[] {
		"<fish_big> ::= <adj_big> <fish> | <adj_big> <adj_xmas> <fish> | <adj_big> <fish_xmas> | <adj_big> <adj_xmas> <fish_xmas>",
		"<fish_sml> ::= <adj_sml> <fish> | <adj_sml> <adj_xmas> <fish> | <adj_sml> <fish_xmas> | <adj_sml> <adj_xmas> <fish_xmas>",
		"<fish_avg> ::= <adj_avg> <fish> | <adj_xmas> <fish> | <adj_avg> <fish_xmas> | <adj_xmas> <fish_xmas>",

		"<adj_xmas> ::= chilly | festive | frosty | gift-giving | happy | icy | jingling | jolly | merry | naughty | nice | nippy | snowy | red-nosed | wintry",
		"<fish_xmas> ::= Christmasfish | elffish | iciclefish | Grinchfish | jinglefish | ornamentfish | presentfish | reindeerfish | Santafish | Scroogefish | sleighfish | snowfish | snowmanfish | tinselfish | winterfish"
	};
	
	private static final String[] FISH_GRAMMAR_DEFAULT = new String[] {
		"<fish_big> ::= <adj_big> <fish>",
		"<fish_sml> ::= <adj_sml> <fish>",
		"<fish_avg> ::= <adj_avg> <fish>",
	};
	
	private static final String[] FISH_AND_ADJECTIVES = new String[] {
		"<adj_avg> ::= African | amazing | Arctic | armoured | Asian | awkward | beached | bigeye | blanched | burning | chocolate-covered | cool | dangerous | dead | delirious | despicable | electric | electronic | European | explosive | expressive | fabulous | fallacious | flawless | frozen | genetically modified | glass | glow-in-the-dark | glowing | greedy | gregarious | hairy | high | insidious | jocular | keen | lazy | leopard-spotted | literary | lovely | magical | malnourished | metal | musical | normal | nosey | noxious | one-legged | oppressed | optimal | oxidized | pampered | peaceful | poisonous | professional | quintessential | radiant | radioactive | rainbow | red-hot | resplendent | rich | robotic | scorched | scorching | secret | shiny | slippery | slithering | smoked | spike-tailed | spiky | steamed | steaming | three-legged | unanimous | uncomfortable | unique | volatile | wicked | wiggly | winged | wise | X-rated | yellowish | zealous",
		"<adj_big> ::= big | big-boned | bloated | bulky | burly | capacious | chubby | colossal | commodious | enormous | fat | gargantuan | gigantic | ginormous | heavy | heavyweight | hefty | huge | hulking | humongous | immense | jumbo | massive | mega | muscly | oversized | prodigious | sizable | substantial | super-colossal | tremendous | voluminous | well-endowed | whopper",
		"<adj_sml> ::= baby | diminutive | eensy-weensy | gaunt | gimpy | inconsiderable amount of | itsy-bitsy | light-weight | little | miniature | newborn | petite | puny | skinny | small | teeny | tiny | tiny baby",
		"<fish> ::= aholehole | albacore | algae-eater | alligatorfish | anchovy | anenomefish | angelfish | angler | archerfish | armorfish | bandfish | barracuda | barreleye | bass | beardfish | blackfish | bonytail chub | buffalofish | butterflyfish | cardinalfish | carp | carpsucker | catfish | cavefish | cherubfish | chubsucker | clownfish | cod | coelacanth | coffinfish | combfish | cornetfish | cutlassfish | damselfish | dartfish | dhufish | dogfish | dottyback | dragonet | dragonfish | eagle ray | eel | electric red | electric ray | elephant fish | elephantnose fish | fangtooth | featherback | filefish | fingerfish | fish | flagfin | flagfish | flashlight fish | flathead | flounder | flyingfish | footballfish | four-eyed fish | frogfish | gibberfish | glassfish | goby | goldfish | goosefish | gourami | graveldiver | guitarfish | gulper | gunard | guppy | hagfish | halibut | handfish | hatchetfish | hawkfish | herring | hogsucker | icefish | jackfish | jawfish | jellyfish | jellynose fish | jewelfish | Jewfish | kelpfish | killifish | kingfish | knifefish | labyrinth fish | ladyfish | lampfish | lamprey | lancetfish | lanternfish | leaffish | lightfish | lionfish | lizardfish | loach | longfin | lungfish | mackerel | Mandarin fish | manefish | man-of-war fish | manefish | manta ray | marblefish | marlin | medusafish | milkfish | minnow | mudminnow | mustache triggerfish | needlefish | nibblefish | nurseryfish | oarfish | oilfish | paradise fish | parrotfish | pearlfish | pencilfish | perch | pigfish | pike | pike conger | pilotfish | pineapplefish | pineconefish | piranha | pipefish | platyfish | plunderfish | pollyfish | ponyfish | poolfish | porcupinefish | pricklefish | prowfish | pufferfish | pupfish | quillfish | rabbitfish | ragfish | rainbowfish | ray | razorfish | redfish | reedfish | ricefish | rockfish | roosterfish | ropefish | rudderfish | sabertooth fish | sablefish | sailfish | salmon | sand diver | sandfish | scabbardfish | scalyfin | scorpionfish | shark | shrimpfish | silverside | skilfish | smelt | smoothtongue | snailfish | snipefish | soldierfish | sole | spadefish | spiderfish | spikefish | spookfish | springfish | squaretail | squawfish | squirrelfish | stingfish | stingray | stonefish | sturgeon | sunfish | surgeonfish | swordfish | swordtail | tadpolefish | telescopefish | thornfish | tilefish | tonguefish | torrentfish | treefish | triggerfish | tripodfish | trout | trumpetfish | trunkfish | unicornfish | velvetfish | viperfish | waryfish | weatherfish | weeverfish | whalefish | whitefish | wormfish | x-ray fish | yellowtail | zebrafish | 39fish | caretfish | jefffish | legofish | shamanfish | nwinfish | rm -rfish | satrixfish | susafish | tejfish | tetrafish | zoraelfish",
	};
	
	public static enum Holiday {
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
		switch(h) {
		case HALLOWEEN:
			grammar = concat(FISH_AND_ADJECTIVES, FISH_GRAMMAR_HLWN);
			break;
		case CHRISTMAS:
			grammar = concat(FISH_AND_ADJECTIVES, FISH_GRAMMAR_XMAS);
			break;
		default:
			grammar = concat(FISH_AND_ADJECTIVES, FISH_GRAMMAR_DEFAULT);
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
