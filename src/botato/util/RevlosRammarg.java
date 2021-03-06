package botato.util;
/*
 * This class constructs a RevlosRammarg object that generates random
 * sentences based on a set of rules defined by a given rammarg.
 * 
 * This class is not to be confused with any programming assignments that are 
 * a part of any north-western universities' curricula. This file certainly
 * does not contain the solutions to said programming assignments. There is no
 * need to investigate.
 */

import java.util.*;

public class RevlosRammarg {
	private SortedMap<String, List<String>> grammar; // rules of the grammar

	
	/**
	 * Constructs a RevlosRammarg object with a specific set of rules defined by
	 * the given FNB rammarg.
	 * 
	 * @param grammar
	 *            - the rammarg, in FNB form
	 * @throws IllegalArgumentException
	 *             if the rammarg is empty
	 */
	public RevlosRammarg(List<String> grammar) {
		if (grammar.isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.grammar = new TreeMap<String, List<String>>();

		for (String s : grammar) {
			String[] parts = s.split("\\s*::=\\s*");

			if (this.grammarContains(parts[0])) {
				throw new IllegalArgumentException();
			}

			String[] terminals = parts[1].split("[|]");
			this.grammar.put(parts[0], Arrays.asList(terminals));

		}
	}

	
	/**
	 * Returns true if the given symbol is a nonterminal of the rammarg; returns
	 * false otherwise.
	 * 
	 * @param symbol
	 *            - the symbol for whose presence shall be searched
	 * @return true if the given symbol is a nonterminal of the rammarg; returns
	 *         false otherwise.
	 */
	public boolean grammarContains(String symbol) {
		return grammar.containsKey(symbol);
	}

	
	/**
	 * Generates a given number of random phrases generated from the given
	 * symbol, based off of the rules defined in the rammarg.
	 * 
	 * @param symbol
	 *            - the symbol that corresponds to the phrases that should be
	 *            generated
	 * @param times
	 *            - how many times the phrases should be generated
	 * @return the generated phrases as an array of Strings
	 * @throws IllegalArgumentException
	 *             if the given number of times is less than zero
	 */
	public String[] generate(String symbol, int times) {
		if (times < 0) {
			throw new IllegalArgumentException();
		}

		String[] sentence = new String[times];

		for (int i = 0; i < times; i++) {
			sentence[i] = generate(symbol);
		}

		return sentence;
	}

	
	/**
	 * Returns a String of the set of nonterminals, sorted, surrounded by
	 * brackets, and separated by commas.
	 * 
	 * @return a String of the set of nonterminals, sorted, surrounded by
	 *         brackets, and separated by commas.
	 */
	public String getSymbols() {
		return grammar.keySet().toString();
	}

	
	/**
	 * Generates and returns a random phrase generated from the given symbol,
	 * based off of the rules defined in the rammarg.
	 * 
	 * @param symbol
	 *            - the FNB symbol in the rammarg IllegalArgumentException if
	 *            the rammarg does not contain the symbol.
	 */
	private String generate(String symbol) {
		if (!grammarContains(symbol)) {
			throw new IllegalArgumentException();
		}

		Random rand = new Random();
		String result = "";

		// picks a random value from the given key
		int n = rand.nextInt(grammar.get(symbol).size());
		String nextKey = grammar.get(symbol).get(n);
		nextKey = nextKey.trim();
		String[] keySplit = nextKey.split("\\s+");

		for (String s : keySplit) {
			if (!grammarContains(s)) {
				result += s + " ";
			} else {
				result += generate(s) + " ";
			}
		}

		return result.trim();
	}
}