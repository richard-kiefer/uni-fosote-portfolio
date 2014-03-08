package davs.searcher.tools;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class MasterTest
	{
		Indexer writeIndex;
		String rootDir = "test_data";
		String indexDir = "index_location";
		String subDir1 = "test_data/subdir1";
		String subDir2 = "test_data/subdir1/subdir2";
		
		
		String indexingFailed = "Indexing failed!";
		String searchFailed = "Search failed!";
		String pathMissMatch = "File path does not match";
		String termNotFound = "Term not found";


		@Before
		public void setUp() throws Exception
			{
				writeIndex = new Indexer(indexDir);
			}
		
		
		@Test
		public void testIndexCreation()
			{
				int docsIndexed;
				
				//Perform actual indexing, create initial index
				docsIndexed = writeIndex.indexDir(subDir2, false, false);
				assertEquals(indexingFailed, 1, docsIndexed);
				
				//Append to existing index, and create new one was false
				docsIndexed = writeIndex.indexDir(subDir1, true, false);
				//existing one document + 3 new ones as if isAppendingIndex
				assertEquals(indexingFailed, 4, docsIndexed);
				
				//Now delete index, and create new one
				docsIndexed = writeIndex.indexDir(rootDir, false, false);
				assertEquals(indexingFailed, 4, docsIndexed);

			}
		
		
		@Test
		public void testSearch() throws IOException
			{
				Searcher search = new Searcher(indexDir);
				
				Queue<String> tmp = search.getSearchTerms("file", 10);
				assertEquals(searchFailed, 2, tmp.size());
				
				tmp = search.getSearchTerms("abc", 10);
				assertEquals(searchFailed, 1, tmp.size());
				
				tmp = search.getSearchTerms("test", 10);
				assertEquals(searchFailed, 1, tmp.size());
				
				String expected = System.getProperty("user.dir") + "/test_data/subdir1/Test.pdf";
				assertEquals(pathMissMatch ,expected, tmp.poll());
				
				search.close();
			}
		
		
		@Test
		public void testFullText() throws IOException
			{
				writeIndex = new Indexer(indexDir);
				int docsIndexed;
				
				docsIndexed = writeIndex.indexDir(subDir2, false, true);
				assertEquals(indexingFailed, 1, docsIndexed);
				
				
				Searcher search = new Searcher(indexDir);
				Queue<String> tmp = search.getSearchTerms("+cat +dog", 10);
				
				String expected = System.getProperty("user.dir") + "/test_data/subdir1/subdir2/file1.file1.pdf";
				assertEquals(termNotFound , expected, tmp.poll());
				
				
				search.close();
			}
		
		
		@Test
		public void testSpellCheck() throws IOException
			{
				SpellCheck sp = new SpellCheck();
				String[] results = sp.getSpellSuggestions("Lanux",3);
				
				
				if(results != null && results.length > 0)
					assertEquals("Linux ", results[0]);//One space always added at the end
				else fail("Linux not returned");
				
				results = sp.getSpellSuggestions("houze Lanux milleduim",3);
				
				if(results != null && results.length > 0)
					{
						assertEquals("house Linux millennium ", results[0]);
						assertEquals("houri Lanny millennial ", results[1]);
					}
				else fail("Multi suggestion does not word");
			}
		
		
	}
