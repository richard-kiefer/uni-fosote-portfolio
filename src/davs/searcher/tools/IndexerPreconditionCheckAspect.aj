package davs.searcher.tools;

import java.io.File;
import java.io.IOException;

public aspect IndexerPreconditionCheckAspect {

    pointcut setIndexDirCut(String finalDir) : 
        (call (private void Indexer.setIndexDir(String)))
        && args(finalDir);
    
    before(String finalDir) throws IOException: setIndexDirCut(finalDir) {
       
        boolean b = true;
        File f = new File(finalDir);

        if (!f.exists() || !f.isDirectory()) {
            b = (new File(finalDir)).mkdirs();
        }
        else {
            b = true;
        }

        if (!b) {
            throw new IOException();
        }
        
    }
}
