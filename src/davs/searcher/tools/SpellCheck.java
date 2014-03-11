package davs.searcher.tools;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class SpellCheck {
    public static int numberOfSuggestions = 3; // Display only first 3 spell
                                               // suggestions

    private SpellChecker spellCheck;

    public SpellCheck() throws IOException {
        // put spell checker in the memory
        Directory directory = new RAMDirectory();
        setSpellCheck(new SpellChecker(directory));
        getSpellCheck().indexDictionary(
                new PlainTextDictionary(new File("./dict")));// dict is
                                                             // dictionary file
        getSpellCheck().setStringDistance(new LevensteinDistance());

        // dict from http://wordlist.sourceforge.net/
    }

    /**
     * Take word and return list of similar words
     * 
     * @param word
     * @return
     */
    public String[] getSpellSuggestions(String word, int numOfSuggestions) {

        try {
            // If user enters multiple words in a string(e.g. "cat dog"), it is
            // necessary to break it
            // into multiple string and get suggestions for each word(e.g.
            // "cat", "dog")
            String[] potentialSuggestions = splitString(word);

            if (potentialSuggestions == null
                    || !(potentialSuggestions.length > 0))
                return null;

            // How many return set there is need for?
            String[] results = new String[numOfSuggestions];

            // Now quickly initialize results array
            for (int i = 0; i < results.length; i++) {
                results[i] = "";
            }

            // Cycle through each split word (e.g. cycle through cat first, then
            // through dog...)
            for (int i = 0; i < potentialSuggestions.length; i++) {
                // Get suggestion for each split word
                String[] tmp = getSpellCheck().suggestSimilar(
                        potentialSuggestions[i], numberOfSuggestions);

                // Add each returned suggestion for split word to the results
                if (tmp != null && tmp.length > 0) {

                    for (int j = 0; (j < tmp.length && j < results.length); j++) {
                        tmp[j] = tmp[j].replaceAll("^\\s+", ""); // Remove
                                                                 // leading
                                                                 // whitespace
                        results[j] = results[j] + tmp[j] + " ";
                    }
                }

            }

            if (results != null && results.length > 0)
                return results;

        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private String[] splitString(String str) {
        return str.split("\\s+");
    }

    private SpellChecker getSpellCheck() {
        return spellCheck;
    }

    private void setSpellCheck(SpellChecker spellCheck) {
        this.spellCheck = spellCheck;
    }

}
