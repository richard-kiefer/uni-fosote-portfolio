package davs.searcher.tools;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

public class IndexUtils
	{
		
		public static boolean indexExists(String searchFolderPath) throws IOException
			{
				String pathToIndex = getValidIndexPath(searchFolderPath);
				Directory tmp = new SimpleFSDirectory( new File(pathToIndex) );
				
				return IndexReader.indexExists(tmp);
			}
		
		//Index actually is not stored in indexDir, but a new hidden folder is created
		//where actual index files will be stored
		public static String getValidIndexPath(String original)
			{
				String sep = File.separator;
				return original + sep + "..index";
			}

	}
