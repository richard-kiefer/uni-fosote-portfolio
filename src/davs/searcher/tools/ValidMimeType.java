package davs.searcher.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * List of mime types that should be indexed.
 * 
 * @author davors
 *
 */
public class ValidMimeType
	{
		
		private ArrayList<String> validMimeTypes;
		
		public ValidMimeType()
			{
				validMimeTypes = new ArrayList<String>();
				
				Scanner scanner = null;
				
				try{
					
					scanner = new Scanner(new File("validMimeTypes"));
					

					while (scanner.hasNextLine())
						validMimeTypes.add(scanner.nextLine());
				}
				catch(FileNotFoundException e)
					{
						//let program crash, but inform user what's wrong first
						System.err.println("File validMimeTypes is not your working directory, so program will exit!");
						System.exit(0);
					}
				finally{scanner.close();}
			
			}
					

		public ArrayList<String> getValidMimeTypes()
			{
				return validMimeTypes;
			}
		
	}
