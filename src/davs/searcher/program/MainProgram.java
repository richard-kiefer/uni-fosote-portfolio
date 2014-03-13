package davs.searcher.program;

import java.io.IOException;

import davs.searcher.gui.IndexNotFound;
import davs.searcher.gui.SearchScreen;
import davs.searcher.tools.IndexUtils;
import davs.searcher.tools.SpellCheck;

//Entry point to QuickSearch program
public class MainProgram {

    public static void main(String[] args) {

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
