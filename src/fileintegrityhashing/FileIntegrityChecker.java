package fileintegrityhashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileIntegrityChecker extends Application{

	TextField dir_path = new TextField("");
	TextField ks_path = new TextField("");
	TextArea text_log = new TextArea();
	Integer dir_count = 0;
	Integer file_count = 0;
	BorderPane bPane = new BorderPane();
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//JavaFX design
		HBox  topbox = new HBox(10);
		VBox  centerbox =  new VBox(10);
		
		topbox.setAlignment(Pos.CENTER);
		centerbox.setAlignment(Pos.CENTER);
		
		Button hashFile = new Button("Calculate single file hash");
		Button hashDir = new Button("Calculate directory files hash");
		Button hashVerify = new Button("Verify directory files");
		
		hashHandler hashFileHandler = new hashHandler();
		hashDirHandler hashDirHandler = new hashDirHandler();
		hashVerifyHandler hashVerifyHandler = new hashVerifyHandler();
		
		hashFile.setOnAction(hashFileHandler);
		hashDir.setOnAction(hashDirHandler);
		hashVerify.setOnAction(hashVerifyHandler);
		
		
		dir_path.setPrefWidth(300);
		ks_path.setPrefWidth(300);
		text_log.setPrefSize(490, 840);
		topbox.setPadding(new Insets(10,10,10,10));
		centerbox.setPadding(new Insets(5,15,15,15));
		
		hashFile.setPrefWidth(300);
		hashDir.setPrefWidth(300);
		hashVerify.setPrefWidth(300);
		
		topbox.getChildren().addAll(dir_path, ks_path, hashFile, hashDir, hashVerify);
		centerbox.getChildren().add(text_log);
		
		bPane.setTop(topbox);
		bPane.setCenter(centerbox);
		
		primaryStage.setTitle("FileIntegrityChecker");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(bPane, 900, 900));
		primaryStage.show();
		
	}
	
	
		public static void main(String[] args) {
			launch(args);
		}
	
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

	
}

class hashHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		
	}
}

class hashDirHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		
	}
}

class hashVerifyHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		
	}
}









