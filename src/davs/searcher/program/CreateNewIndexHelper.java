package davs.searcher.program;

import java.io.IOException;

import davs.searcher.tools.Indexer;

/**
 * Since GUI was auto generated, this class is used to 
 * separate generated code from the event handling code.
 * 
 * @author davors
 *
 */

public class CreateNewIndexHelper
	{
		
		String indexDir;
		String dataDir;
		
		public CreateNewIndexHelper(){}
		
		public CreateNewIndexHelper(String indexDir, String dataDir)
			{
				setIndexDir(indexDir);
				setDataDir(dataDir);
			}
		
		public boolean getIndexPath()
			{
				FolderChooser fc = new FolderChooser();
				String path = fc.getPath("Select folder where index will be stored");
				
				if(path != null && path.length()>0)
					{
						setIndexDir(path);
						return true;
					}
				
				return false; //Cancel clicked
			}
		
		
		public boolean getDataPath()
			{
				FolderChooser fc = new FolderChooser();
				String path = fc.getPath("Select folder which will be added to index");
				
				if(path != null && path.length()>0)
					{
						setDataDir(path);
						return true;
					}
				
				return false; //Cancel clicked
				
			}
		
		public boolean index(boolean indexFullContents)
			{
				try
					{
						Indexer index = new Indexer(getIndexDir());
						
						int result = index.indexDir(getDataDir(), false, indexFullContents);
						
						if( result == (-1) )
							return false;
						
					} catch (IOException e)
					{
						return false;
					}
				
					return true;
				
			}
		

		public String getIndexDir()
			{
				return indexDir;
			}

		private void setIndexDir(String indexDir)
			{
				this.indexDir = indexDir;
			}

		public String getDataDir()
			{
				return dataDir;
			}

		private void setDataDir(String dataDir)
			{
				this.dataDir = dataDir;
			}
		
	}
