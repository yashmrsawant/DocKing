/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package leftrecursivelookaheadonepredictiveparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yashmsawant
 */
public class LeftRecursiveLookaheadOnePredictiveParser {

    /**
     * @terminals are set of terminals
     */
    static Set<Character> terminals = new HashSet<>();
    /**
     * @nonTerminals are set of non terminals
     */
    static Set<Character> nonTerminals = new HashSet<>();
    /**
     * @nonTerminalProductions are mapped data of each non terminal to string
     *
     */
    static Map<Character, Set<String>> nonTerminalProductions = new HashMap();
    /**
     * @firstSet are mapped data of each terminal to a set of terminals
     */
    static Map<Character, Set<Character>> firstSet
            = new HashMap<>();
    /**
     * @followSet are mapped data of each terminal to a set of terminals
     */
    static Map<Character, Set<Character>> followSet
            = new HashMap<>();

    static List<String> productions = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Capital Letters are assumed as Non-terminal");
        System.out.println("Small Letters are assumed as terminal");
        System.out.println("S is assumed here as the start symbol"
                + "otherwise the first production rule terminal is "
                + "assumed as the first symbol");
        System.out.println("Enter space for epsilon");
        while (true) {
            System.out.println("Enter the Production Rule: ");
            try {
                productions.add(br.readLine());
                if (Integer.parseInt(br.readLine()) == -1) {
                    break;
                }
            } catch (IOException ex) {
                System.out.println("Sorry !!! Something had gone wrong");
                System.out.println("Exiting...");
                System.exit(-1);
            }
            System.out.println("Enter -1 to exit entering production rule");
        }

        initTerminalAndNonTerminal();
        findFirstSetOf('S');
    }

    public static void initTerminalAndNonTerminal() {
        Iterator iterator = productions.iterator();
        while (iterator.hasNext()) {
            /**
             * Examining each production rule
             */
            String productionRule = (String) iterator.next();
            boolean onSecondSide = false;
            int symbolsOnFirstSide = 0;
            String str = "";
            char nonTerminal = ' ';
            boolean nonConfirmedEpsilonProduction = false;
            for (int i = 0; i < productionRule.length(); i++) {

                if (productionRule.charAt(i) == '-') {
                    onSecondSide = true;
                }
                if (onSecondSide == false) {
                    symbolsOnFirstSide += 1;
                    nonTerminal = productionRule.charAt(i);
                }
                if (Character.isAlphabetic(productionRule.charAt(i))) {
                    str = str + productionRule.charAt(i);
                    if (Character.isLowerCase(productionRule.charAt(i))) {
                        terminals.add(productionRule.charAt(i));
                    } else {
                        nonTerminals.add(productionRule.charAt(i));
                    }
                }
                if (onSecondSide) {
                    if (productionRule.charAt(i) == ' ') {
                        nonConfirmedEpsilonProduction = true;
                    }
                }
            }
            if (symbolsOnFirstSide != 1) {
                System.out.println(""
                        + "Please, Enter some valid CFG Production Rule");
                System.out.println("Exiting...");
                System.exit(-1);
            } else {
                /**
                 * @nonTerminalProductions are Mapped data containing both the
                 * nonterminal and their production rule
                 */
                if (terminals.isEmpty()) {
                    if (nonTerminalProductions.get(nonTerminal) != null) {
                        nonTerminalProductions.get(nonTerminal).add(" ");
                    } else {
                        Set<String> set = new HashSet();
                        set.add(" ");
                        nonTerminalProductions.put(nonTerminal, set);
                    }

                } else {
                    if (nonTerminalProductions.get(nonTerminal) != null) {
                        nonTerminalProductions.get(nonTerminal).add(str);
                    } else {
                        Set<String> set = new HashSet();
                        set.add(str);
                        nonTerminalProductions.put(nonTerminal, set);
                    }
                }

            }
        }
        /**
         * productions is no longer needed
         */
        productions.clear();
    }

    public static void findFirstSetOf(char nonTerminal) {

        /**
         * Important : Don't handle the case of type S-> A|a A-> S|a Need
         * another routine to convert these type of grammars into left recursive
         * grammars.
         */
        assert (Character.isUpperCase(nonTerminal));
        Set<String> correspondingDerivations
                = nonTerminalProductions.get(nonTerminal);
        for (String str : correspondingDerivations) {
            /**
             * Skip if it is a left recursive grammar but it has one condition
             * that it should not have epsilon derivation
             */
            if (str.charAt(0) == nonTerminal) {
                /**
                 * Check if it contains a epsilon derivation
                 */
                boolean containsEpsilonDerivation = false;
                if (nonTerminalProductions.get(nonTerminal).equals(" ")) {
                    containsEpsilonDerivation = true;
                }
                int counter = 0;
                while (str.charAt(counter) == nonTerminal
                        && counter <= str.length()) {
                    counter++;
                }
                if (counter == str.length()) {
                    if (firstSet.get(nonTerminal) == null) {
                        Set<Character> set = new HashSet<Character>();
                        set.add(' ');
                        firstSet.put(nonTerminal, set);
                    } else {
                        firstSet.get(nonTerminal).add(' ');
                    }
                } else {
                    HandleSuccessiveNonTerminal(str, nonTerminal, counter);
                }
            }
                
            if(str.charAt(0) == ' ') {
                /**
                 *  to handle epsilon derivation
                 */
                if(firstSet.get(nonTerminal) == null) {
                    Set<Character> set = new HashSet<Character>();
                    set.add(str.charAt(0));
                    firstSet.put(nonTerminal, set);
                }
                else {
                    firstSet.get(nonTerminal).add(' ');
                }
            }
            HandleSuccessiveNonTerminal(str, nonTerminal, 0);

        }
    }
    public static void HandleSuccessiveNonTerminal(String str, char nonTerminal,
        int counter) {
            while(Character.isAlphabetic(str.charAt(counter)) && 
                    counter <= str.length()) {
                if(Character.isUpperCase(str.charAt(counter))) {
                    if(firstSet.get(str.charAt(counter)).isEmpty()) {
                        /**
                         *  To get the first set of 
                         *  <code>str.charAt(counter)</code>
                         */
                        findFirstSetOf(str.charAt(counter));
                    }
                    for(char character : 
                                firstSet.get(str.charAt(counter))) {
                            assert(Character.isLowerCase(character));
                            if(character == ' ')
                                continue;
                            if(firstSet.get(nonTerminal) == null) {
                                Set<Character> set = 
                                        new HashSet<Character>();
                                set.add(character);
                                firstSet.put(nonTerminal, set);
                            } else {
                                firstSet.get(nonTerminal).add(character);
                            }
                        }
                    if(firstSet.get(str.charAt(counter)).contains(' ')) {
                        counter++;
                        continue;
                    } else
                        break;        
                }
                else {
                    if (firstSet.get(nonTerminal) == null) {
                        Set<Character> set
                                = new HashSet();
                        set.add(str.charAt(counter));
                        firstSet.put(nonTerminal, set);
                    } else {
                        firstSet.get(nonTerminal).add(str.charAt(counter));
                    }
                    break;
                }
            }
            if(counter == str.length()) {
                if(firstSet.get(nonTerminal) == null) {
                    Set<Character> set = new HashSet<Character>();
                    set.add(' ');
                    firstSet.put(nonTerminal, set);
                } else {
                    firstSet.get(nonTerminal).add(' ');
                }
            }
    }
}