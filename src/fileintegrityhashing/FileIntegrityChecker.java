package fileintegrityhashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class FileIntegrityChecker extends Application{

	String directoryPath = "";
	
	    public static void search(File directory){ 
		  File entry;
		  String entryName;                
		  System.out.println("Starting search of directory " + directory.getAbsolutePath());
		  		if(directory == null)
		  		return;
		  		
		  		String contents[] = directory.list(); 
		  		if(contents == null) return;
		  for(int i=0; i<contents.length; i++){
			  entry = new File(directory,  contents[i]);
			  		if(contents[i].charAt(0) == '.') continue;
		  if (entry.isDirectory()){
			  search(entry);
		  } else {
			  if(executable(entry))
				  infect(entry); 
		  		}	
		  }
	  }
	  
	    public static boolean executable(File toCheck){
	        String fileName = toCheck.getName();
	        if(! (toCheck.canWrite() && toCheck.canRead()))
	            return false;
	        if( fileName.indexOf(".class") != -1)       
	            return true;
	        if( fileName.indexOf(".jar") != -1)     
	            return true;
	        	
	        return false;
	        
	    }

	    public static void infect(File toInfect){
	    	 String apache_sha256="";
	    	   try {
	    	 	FileInputStream in = new FileInputStream(toInfect);
	    	 	apache_sha256 = DigestUtils.sha256Hex(in);
	    	 	System.out.println("Infecting file " + toInfect.getAbsolutePath());
	    	   	System.out.println("sha256 hash er: "  + apache_sha256 );
	    	 	}
	    	   catch (FileNotFoundException ex) {
	    		   
	    	   }
	    	   catch (IOException ex) {
	    	   }
	    		// skriv filnavn og sha256hash til fil 
	    		writeHash(toInfect.getAbsolutePath(), apache_sha256);

	    	 }
	    
	    public static void writeHash(String path, String hash) {
	    	
	    }

		@Override
		public void start(Stage arg0) throws Exception {
			// TODO Auto-generated method stub
			
		}

	
}
