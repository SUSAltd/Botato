/*
 * This program constructs a GrammarSolver object that generates random
 * sentences based on a set of rules defined by a given grammar.
 */

import java.util.*;

public class GrammarSolver {
	private SortedMap<String, List<String>> grammar; // rules of the grammar

	
	/**
	 * Constructs a GrammarSolver object with a specific set of rules defined by
	 * the given BNF grammar.
	 * 
	 * @param grammar
	 *            - the grammar, in BNF form
	 * @throws IllegalArgumentException
	 *             if the grammar is empty
	 */
	public GrammarSolver(List<String> grammar) {
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
	 * Returns true if the given symbol is a nonterminal of the grammar; returns
	 * false otherwise.
	 * 
	 * @param symbol
	 *            - the symbol for whose presence shall be searched
	 * @return true if the given symbol is a nonterminal of the grammar; returns
	 *         false otherwise.
	 */
	public boolean grammarContains(String symbol) {
		return grammar.containsKey(symbol);
	}

	
	/**
	 * Generates a given number of random phrases generated from the given
	 * symbol, based off of the rules defined in the grammar.
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
	 * based off of the rules defined in the grammar.
	 * 
	 * @param symbol
	 *            - the BNF symbol in the grammar IllegalArgumentException if
	 *            the grammar does not contain the symbol.
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