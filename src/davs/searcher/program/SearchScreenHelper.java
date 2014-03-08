package davs.searcher.program;

import java.io.IOException;
import java.util.Queue;

import javax.swing.JOptionPane;

import davs.searcher.tools.Indexer;
import davs.searcher.tools.Searcher;

/**
 * Since GUI was auto generated, this class is used to 
 * separate generated code from the event handling code.
 * 
 * @author davors
 *
 */

public class SearchScreenHelper
	{
		private String currentIndexPath; //Path to current index
		private boolean indexFullContents;
		private Searcher search;
		
		public SearchScreenHelper(String indexDir) throws IOException
			{
				setCurrentIndexPath(indexDir);
				setSearch(new Searcher( getCurrentIndexPath() ));
			}
		
		private int indexFullContents()
			{
				int indexFull = JOptionPane.showConfirmDialog(null, "Index full content of a file(s) ?");
				
				if(indexFull == JOptionPane.YES_OPTION)
					return 0;
				else if(indexFull == JOptionPane.NO_OPTION)
					return 1;
				else return -1;
				
			}
		
		public boolean appendToIndex(String indexDir)
			{
				setCurrentIndexPath(indexDir);
				FolderChooser fc = new FolderChooser();
				
				String dataDir = fc.getPath("Select files to add to index");
				
				
				if(dataDir != null && dataDir.length()>0)
					{
						
						int fullText = indexFullContents();
						
						if(fullText == -1)
							return false;
						
						if(fullText == 0)
							setIndexedFullContents(true);
						else setIndexedFullContents(false);
						
						try
							{
								Indexer tmp = new Indexer(getCurrentIndexPath());
								tmp.indexDir(dataDir, true, isIndexedFullContents());
							} catch (IOException e)
							{
								return false;
							}
						
						return true;
					}
				
				return false; //Cancel clicked
				
			}
		
		public Queue<String> search(String term)
			{
				
				Queue<String> results = null;
				
				try
					{
						//Return only first 200 hits, no need for more
						results = getSearch().getSearchTerms(term, 200);
						
					} catch (IOException e)
					{
						return null;
					}
					
					if(results == null)
						return null;
					else return results;
			}

		
		public String getCurrentIndexPath()
			{
				return currentIndexPath;
			}

		public void setCurrentIndexPath(String currentIndex)
			{
				this.currentIndexPath = currentIndex;
			}

		private boolean isIndexedFullContents()
			{
				return indexFullContents;
			}

		private void setIndexedFullContents(boolean indexFullContents)
			{
				this.indexFullContents = indexFullContents;
			}
		
		private Searcher getSearch()
			{
				return search;
			}

		private void setSearch(Searcher search)
			{
				this.search = search;
			}

	}
