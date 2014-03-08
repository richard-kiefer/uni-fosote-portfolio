package davs.searcher.program;

import java.io.File;
import javax.swing.JFileChooser;

public class FolderChooser
	{
		public FolderChooser(){}
		
		public String getPath(String title)
			{
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setDialogTitle(title);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				
				int userSelection = fc.showOpenDialog(null);
				
				if(userSelection == JFileChooser.APPROVE_OPTION)
					return fc.getSelectedFile().getAbsolutePath();
				else return null; //User clicked cancel			
			}

	}
