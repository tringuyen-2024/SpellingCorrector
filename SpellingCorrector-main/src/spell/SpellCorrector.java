package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     *
     * @param dictionaryFileName the file containing the words to be used
     * @throws IOException If the file cannot be read
     * @pre SpellCorrector will have had empty-param constructor called, but dictionary has nothing in it.
     * @post SpellCorrector will have dictionary filled and be ready to suggestSimilarWord any number of times.
     */
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File dictionaryFile = new File(dictionaryFileName);
        Scanner scanner = new Scanner(dictionaryFile);

        dictionary = new Trie();


        while (scanner.hasNext()) {
            String word = scanner.next();
            dictionary.add(word);
            //System.out.println(word);
        }


    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>.
     *
     * @param inputWord the word we are trying to find or find a suggestion for
     * @return the suggestion or null if there is no similar word in the dictionary
     */
    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase(Locale.ROOT);
        Set<String> possibleWords = new HashSet<>();

        if (dictionary.find(inputWord) != null) {
            return inputWord;
        }

        deletionAddition(inputWord, possibleWords);
        transpositionAddition(inputWord, possibleWords);
        alterationAddition(inputWord, possibleWords);
        insertionAddition(inputWord, possibleWords);

        Set<String> cleanedUpWords = createMaxValueWordSet(possibleWords);
        if (cleanedUpWords.size() > 0) {
            //System.out.println("Answer: " + selectWord(cleanedUpWords));
            return selectWord(cleanedUpWords);
        }
        Set<String> secondIteration = new HashSet<>();
        for (String word: possibleWords) {
            deletionAddition(word, secondIteration);
            transpositionAddition(word, secondIteration);
            alterationAddition(word, secondIteration);
            insertionAddition(word, secondIteration);
        }
        cleanedUpWords = createMaxValueWordSet(secondIteration);
        if (cleanedUpWords.size() > 0) {
            //System.out.println("Answer: " + selectWord(cleanedUpWords));
            return selectWord(cleanedUpWords);
        }

        //System.out.println(cleanedUpWords);
        return null;
    }
    private void deletionAddition(String word, Set<String> wordSet) {
        for (int i = 0; i < word.length(); ++i) {
            StringBuilder tempWord = new StringBuilder(word);
            tempWord.deleteCharAt(i);
            wordSet.add(tempWord.toString());
        }
    }

    private void transpositionAddition(String word, Set<String> wordSet) {
        for (int i = 0; i < word.length() - 1; ++i) {
            StringBuilder tempWord = new StringBuilder(word);
            char tempChar = tempWord.charAt(i);
            tempWord.setCharAt(i, tempWord.charAt(i + 1));
            tempWord.setCharAt(i + 1, tempChar);
            wordSet.add(tempWord.toString());
        }
    }

    private void alterationAddition(String word, Set<String> wordSet) {
        for (int i = 0; i < word.length(); ++i) {
            StringBuilder tempWord = new StringBuilder(word);
            for (char letter = 'a'; letter <= 'z'; ++letter) {
                if (word.charAt(i) != letter) {
                    tempWord.setCharAt(i, letter);
                    wordSet.add(tempWord.toString());
                }
            }
        }
    }

    private void insertionAddition(String word, Set<String> wordSet) {
        for (int i = 0; i < word.length() + 1; ++i) {
            for (char letter = 'a'; letter <= 'z'; ++letter) {
                StringBuilder tempWord = new StringBuilder(word);
                tempWord.insert(i, letter);
                wordSet.add(tempWord.toString());
            }
        }
    }

    private Set<String> createMaxValueWordSet(Set<String> wordSet) {
        Set<String> newSet = new HashSet<>(wordSet);
        int maxValue = 0;
        for (String word : newSet) {
            if (dictionary.find(word) != null) {
                if (dictionary.find(word).getValue() > maxValue) {
                    maxValue = dictionary.find(word).getValue();
                }
            }
        }

        newSet.removeIf(word->dictionary.find(word) == null);

        final int finalMaxValue = maxValue;
        newSet.removeIf(word -> dictionary.find(word).getValue() < finalMaxValue);

        //System.out.println("TEST: " + newSet);

        return newSet;
    }

    private String selectWord(Set<String> inputSet) {
        List<String> wordList = new ArrayList<>(inputSet);
        Collections.sort(wordList);
        return wordList.get(0);
    }

    private Trie dictionary;
}
