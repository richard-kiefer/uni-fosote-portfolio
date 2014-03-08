package davs.searcher.program;

import java.io.IOException;

import davs.searcher.tools.IndexUtils;

/**
 * Since GUI was auto generated, this class is used to 
 * separate generated code from the event handling code.
 * 
 * @author davors
 *
 */

public class IndexNotFoundHelper
	{
		
		private String indexDir; //Location of the existing index 
		
		public IndexNotFoundHelper(){}
		
		
		/**
		 * 
		 * @return True if existing index is located
		 */
		public Boolean isIndexLocated()
			{
				FolderChooser fc = new FolderChooser();
				String path = fc.getPath("Select location of existing index");
				
				if(path != null)
					{
						boolean isPathFound=false;
						
						try
							{
								isPathFound = IndexUtils.indexExists(path);
							} catch (IOException e)
							{
								return false;
							}
						

						if(isPathFound)
							{
								setPathToIndex(path);
								return true;
							}
						else return false;
					}
				
				return null; //Cancel clicked
				
			}
		

		public String getPathToIndex()
			{
				return indexDir;
			}

		private void setPathToIndex(String pathToIndex)
			{
				this.indexDir = pathToIndex;
			}
		
	}
