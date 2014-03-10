package davs.searcher.tools;

import static org.junit.Assert.*;

import org.junit.Test;

import davs.searcher.tools.Indexer;

import java.io.File;


public class IndexerTest {

    @Test (expected = java.io.IOException.class)
    public void testInvalidPathFailure() throws java.io.IOException {
        new Indexer("/not/existing/and/no/permission");
    }
    
    @Test
    public void testNonExistingPathCreation() throws Exception {
        String dirname = "test_data/_tmp/";
        File dir = new File(dirname);
        if (dir.exists()) {
            fail("Cannot create existing " + dirname + " test directory.");
        }

        new Indexer(dirname);

        if (!dir.exists()) {
            fail("Non-existing directory was not created.");
        } else {
            // success; clean up
            deleteDirectory(dir);
        }
    }
    
    
    // Helper function. Deletes a directory with all its content.
    // Thanks to http://www.rgagnon.com/javadetails/java-0483.html
    static public boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      }
}
