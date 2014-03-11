package davs.searcher.tests;

import java.io.File;

public class TestUtils {

    /**
     * Deletes a directory with all its content.
     * 
     * Built upon http://www.rgagnon.com/javadetails/java-0483.html - thanks!
     * 
     * @param path File object denoting the directory to be deleted.
     * @return True if and only if the given directory is successfully deleted;
     *         false otherwise.
     */
    static public boolean deleteDirectory(File path) {
        if (!path.isDirectory()) {
            return false;
        }
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
    /**
     * @see deleteDirectory(File).
     * Wrapper function which accepts path to be a string.
     */
    static public boolean deleteDirectory(String path) {
        return deleteDirectory(new File(path));
    }

}
