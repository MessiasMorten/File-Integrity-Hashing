package fileintegrityhashing;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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

	static TextField dir_path = new TextField("Directory path");
	TextField ks_path = new TextField("Keystore path");
	TextField ks_password = new TextField("password");
	TextField ks_alias = new TextField("alias");
	static TextField hash_path = new TextField("Save hash file path");
	static TextArea text_log = new TextArea();
	Integer dir_count = 0;
	Integer file_count = 0;
	BorderPane bPane = new BorderPane();
	static ArrayList<String> hashfileContents = new ArrayList<String>();
	static ArrayList<String> hashfromFileContents = new ArrayList<String>();
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//JavaFX design
		HBox  topbox = new HBox(10);
		HBox  bottombox = new HBox(10);
		VBox  centerbox =  new VBox(10);
		
		topbox.setAlignment(Pos.CENTER);
		centerbox.setAlignment(Pos.CENTER);
		
		Button verifyFile = new Button("Verify single file");
		Button hashDir = new Button("Generate hash file");
		Button verifyFiles = new Button("Verify directory files");
		Button saveHashFile = new Button("Save hash file to path");
		
		hashHandler hashFileHandler = new hashHandler();
		hashDirHandler hashDirHandler = new hashDirHandler();
		hashVerifyHandler hashVerifyHandler = new hashVerifyHandler();
		saveHashHandler saveHashHandler = new saveHashHandler();

		hashDir.setOnAction(hashDirHandler);
		verifyFile.setOnAction(hashFileHandler);
		verifyFiles.setOnAction(hashVerifyHandler);
		saveHashFile.setOnAction(saveHashHandler);
		
		
		dir_path.setPrefWidth(300);
		ks_path.setPrefWidth(300);
		text_log.setPrefSize(490, 840);
		topbox.setPadding(new Insets(10,10,10,10));
		bottombox.setPadding(new Insets(10,10,10,10));
		centerbox.setPadding(new Insets(5,15,15,15));
		
		hashDir.setPrefWidth(160);
		verifyFile.setPrefWidth(160);
		verifyFiles.setPrefWidth(160);
		saveHashFile.setPrefWidth(300);
		hash_path.setPrefWidth(300);
		
		bottombox.getChildren().addAll(hash_path, saveHashFile);
		topbox.getChildren().addAll(dir_path, ks_path, ks_password, ks_alias, hashDir, verifyFile, verifyFiles);
		centerbox.getChildren().add(text_log);
		
		bPane.setTop(topbox);
		bPane.setBottom(bottombox);
		bPane.setCenter(centerbox);
		
		primaryStage.setTitle("FileIntegrityChecker");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(bPane, 1250, 900));
		primaryStage.show();
		
	}
	
	
		public static void main(String[] args) {
			launch(args);
		}
	
	    public static void search(File directory){ 
		  File entry;            
		  text_log.appendText("\nStarting search of directory " + directory.getAbsolutePath());
		  		String contents[] = directory.list(); 
		  		if(contents == null) return;
		  for(int i=0; i<contents.length; i++){
			  entry = new File(directory,  contents[i]);
			  		if(contents[i].charAt(0) == '.') continue;
		  if (entry.isDirectory()){
			  search(entry);
		  } else {
				  infect(entry); 
		  		}
		  	}
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
	    		   System.out.println("You need to get a valid filepath, smartass.");
	    	   }
	    	   catch (IOException ex) {
	    	   }
	    	    Date date= new Date();
	    	    Timestamp ts = new Timestamp(date.getTime());
	    	    text_log.appendText("\n" +ts + " - Searched filename: " + toInfect.getAbsolutePath() + " Hash: "+apache_sha256);
	    	    hashfileContents.add(toInfect.getAbsolutePath());
	    	    hashfileContents.add(apache_sha256);
	    	    
	    	 }
	    
	    public static void testMethod() {
	    
	    Date date= new Date();
	    Timestamp ts = new Timestamp(date.getTime());
	    String text = dir_path.getText();
	    text_log.appendText(ts + " : " + text);
	    }

	    public static void writeToFile(String path, String hash) {
	    	
	    }

	    public static void createHashFile() throws IOException {
	    	
	    	if (hashfileContents.size() > 0) {
	    		
	    		
	    		String filepath = hash_path.getText();
	    		BufferedWriter output = new BufferedWriter(new FileWriter(filepath));
				for (int i=0; i<hashfileContents.size(); i=i+2) {
					
					
					try {
						output.write(hashfileContents.get(i));

					output.write(",");
					output.write(hashfileContents.get(i+1));
					output.write(",");
					text_log.appendText("\nWrote file " + hashfileContents.get(i) + " to hashfile.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
						}
					}
				text_log.appendText("\nHash file created in " + filepath);
				output.close();
				
	    		}	
	    	}
	    
	    public static void readAndCalculateDirectory() {
	    	String path = dir_path.getText();
	    	File file = new File(path);
	    	search(file);
	    }

	    public static void readFromFile() {
	    	
	    	String filepath = hash_path.getText();
	    	Scanner fileScan;
	    	
	    	try {
				fileScan = new Scanner(new File(filepath));
				fileScan.useDelimiter(",");
				while (fileScan.hasNext()) {
					hashfromFileContents.add(fileScan.next());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    for(int i=0; i<hashfromFileContents.size(); i++) {
	    	text_log.appendText("\nRead item from file: " + hashfromFileContents.get(i));
	    }
	    	
	    }
	    
class hashHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		//FileIntegrityChecker.testMethod();
		
	}
}

class hashDirHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		FileIntegrityChecker.readAndCalculateDirectory();
	}
}

class hashVerifyHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		FileIntegrityChecker.readFromFile();
	}
}
	
class saveHashHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
			try {
				FileIntegrityChecker.createHashFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
	









