package davs.searcher.tools;

import static org.junit.Assert.*;

import org.junit.Test;


public class IndexerTest {

    @Test (expected = java.io.IOException.class)
    public void testIndexerPreconditionCheck() throws java.io.IOException {
        new davs.searcher.tools.Indexer("/not/existing/and/no/permission");
    }

}
