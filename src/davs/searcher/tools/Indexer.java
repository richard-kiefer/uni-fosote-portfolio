package davs.searcher.tools;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;

public class Indexer {

    private IndexWriter indexWriter;

    public static Version currentVersion = Version.LUCENE_35;

    private Analyzer analyzer; // analyzer for index
    private ValidMimeType allowedFileTypes;
    private String indexDir; // path to index, i.e. where will I save index
    private String dataDir; // path to data to be indexed, i.e. what should I
                            // put in index

    /**
     * Create new instance of class responsible for index creation.
     * 
     * @param indexDir
     *            Folder where index will be stored
     * @throws IOException
     */
    public Indexer(String indexDir) throws IOException {
        // Index will always be stored in a secret folder named .index
        String finalDir = IndexUtils.getValidIndexPath(indexDir);

        File f = new File(finalDir);
        boolean b = true;

        if (!f.exists() || !f.isDirectory())
            b = (new File(finalDir)).mkdirs();
        else
            b = true;

        if (!b)
            throw new IOException();

        setIndexDir(finalDir);
        setAnalyzer(new StopAnalyzer(currentVersion)); // name1a.name2b_name3...
                                                       // => (to tokens) namea,
                                                       // nameb, name...

        allowedFileTypes = new ValidMimeType();

        Logger.getRootLogger().setLevel(Level.OFF);
    }

    /**
     * Method used to either create a new index or to append to existing index.
     * 
     * @param isAppendingIndex
     *            If true append to existing index, otherwise create new index
     */
    private IndexWriter getIndexWriter(boolean isAppendingIndex) {
        Directory indexLocation = null;

        try {
            indexLocation = FSDirectory.open(new File(getIndexDir()));

        } catch (IOException e) {
            return null;
        }

        IndexWriterConfig config = new IndexWriterConfig(currentVersion,
                getAnalyzer());

        if (isAppendingIndex)
            config.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
        else
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try {
            return new IndexWriter(indexLocation, config);

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * //TODO look into tracking hash checksums of each file to update specific
     * file
     * 
     * Perform actual indexing.
     * 
     * @param isAppendingIndex
     *            If true append to index, otherwise create new index
     * @param indexFullContents
     *            If true save full text in index
     * @return True if operation succeed
     */
    /**
     * 
     * @param dataDir
     * @param isAppendingIndex
     *            If true append to index, otherwise create new index
     * @param indexFullContents
     *            If true save full text in index
     * @return True if operation succeeded
     */
    public int indexDir(String dataDir, boolean isAppendingIndex,
            boolean indexFullContents) {

        setDataDir(dataDir);
        setIndexWriter(getIndexWriter(isAppendingIndex));

        Document document = null;
        Stack<File> files = getAllFiles(getDataDir());
        Tika extractText = new Tika();

        while (!files.isEmpty()) {
            File file2Index = files.pop();

            document = new Document();

            try {

                if (indexFullContents) {
                    Reader content = extractText.parse(file2Index);
                    document.add(new Field(IndexFieldTypes.fulltext, content));
                }

                // e.g. x.y will be searchable as x and y
                document.add(new Field(IndexFieldTypes.filename, file2Index
                        .getName(), Field.Store.YES,
                        Field.Index.ANALYZED_NO_NORMS));
                document.add(new Field(IndexFieldTypes.filepath, file2Index
                        .getCanonicalPath(), Field.Store.YES,
                        Field.Index.ANALYZED_NO_NORMS));

                getIndexWriter().addDocument(document);

            } catch (IOException e) {
                return -1;
            }

        }// end while loop

        // return number of documents indexed
        try {
            int tmp = indexWriter.numDocs();
            getIndexWriter().close();
            return tmp;
        } catch (IOException e) {
            return -1;
        }

    }

    /**
     * Return all files from given folder, and all sub-folders
     * 
     * @param path
     *            Root path of the folder
     * @return All files in root folder and all sub-folders
     */
    private Stack<File> getAllFiles(String path) {
        Stack<File> holdAllFiles = new Stack<File>();
        Stack<File> holdResults = new Stack<File>();

        holdAllFiles.push(new File(path));

        while (!holdAllFiles.isEmpty()) {
            File file = holdAllFiles.pop();

            if (file.isDirectory()) {
                for (File f : file.listFiles())
                    holdAllFiles.push(f);
            } else if (!file.isHidden() && file.canRead() && file.exists()
                    && isAcceptedFileFormat(file, new Tika()))
                holdResults.push(file);
        }

        return holdResults;
    }

    /**
     * Get file, and check if file's format is one we want.
     * 
     * @param file
     *            Detect format on this file
     * @param detectFileType
     *            Tika facade that performs file detection
     * @return True if file format is one in the list on ValidMimetypes
     */
    private boolean isAcceptedFileFormat(File file, Tika detectFileType) {
        String detectedFileFormat = null;

        try {
            detectedFileFormat = detectFileType.detect(file);

        } catch (IOException e) {
            return false;
        }

        for (String tmp : getAllowedFileTypes().getValidMimeTypes()) {
            if (tmp.equals(detectedFileFormat))
                return true;
        }

        return false;
    }

    // getters and setters...

    private Analyzer getAnalyzer() {
        return analyzer;
    }

    private void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    private String getIndexDir() {
        return indexDir;
    }

    private void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    private String getDataDir() {
        return dataDir;
    }

    private void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    private IndexWriter getIndexWriter() {
        return indexWriter;
    }

    private void setIndexWriter(IndexWriter writeIndex) {
        this.indexWriter = writeIndex;
    }

    private ValidMimeType getAllowedFileTypes() {
        return allowedFileTypes;
    }

}
