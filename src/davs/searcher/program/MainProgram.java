package davs.searcher.program;

import java.io.IOException;

import davs.searcher.gui.IndexNotFound;
import davs.searcher.gui.SearchScreen;
import davs.searcher.tools.IndexUtils;
import davs.searcher.tools.SpellCheck;

//Entry point to QuickSearch program
public class MainProgram {
    // Since spell checking is expensive operation, hold only one copy
    public static SpellCheck spellCheck;
    public static boolean isSpellCheckEnabled;

    public static void main(String[] args) {

        isSpellCheckEnabled = true;

        try {
            spellCheck = new SpellCheck();
        } catch (IOException e) {
            // If spellCheck is not initialized, don't even bother with
            // suggestions
            isSpellCheckEnabled = false;

            System.err.println("Spell checking is not supported");
            System.err
                    .println("Make sure file dict is in your working directory");
        }

        // First check if index is in current working directory
        String currentPath = System.getProperty("user.dir");

        boolean isIndexLocated = false;

        try {
            isIndexLocated = IndexUtils.indexExists(currentPath);
        } catch (IOException e) {
            isIndexLocated = false;
        }

        if (isIndexLocated)
            new SearchScreen(currentPath).setVisible(true);
        else
            new IndexNotFound().setVisible(true);

    }

}
