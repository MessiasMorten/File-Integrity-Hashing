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
	static ArrayList<String> hashdirContents = new ArrayList<String>();
	static ArrayList<String> hashfromFileContents = new ArrayList<String>();
	static ArrayList<String> errorLogFromVerify = new ArrayList<String>();
	
	
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
	    	    hashdirContents.add(toInfect.getAbsolutePath());
	    	    hashdirContents.add(apache_sha256);
	    	    
	    	 }

	    public static void createHashFile() throws IOException {
	    	
	    	if (hashdirContents.size() > 0) {
	    		
	    		
	    		String filepath = hash_path.getText();
	    		BufferedWriter output = new BufferedWriter(new FileWriter(filepath));
				for (int i=0; i<hashdirContents.size(); i=i+2) {
					
					
					try {
						output.write(hashdirContents.get(i));

					output.write(",");
					output.write(hashdirContents.get(i+1));
					output.write(",");
					text_log.appendText("\nWrote file " + hashdirContents.get(i) + " to hashfile.");
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
	    
	    public static void readFromFileWithoutLogging() {
	    	
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
	    	
	    }
	    
	    public static void compareAndVerify() {
	    	
	    	//Calculates hashes from the given directory
	    	readAndCalculateDirectory();
	    	//Reads saved hashes from given hashfile
	    	readFromFileWithoutLogging();
	    	
	    	if (hashdirContents.size() != hashfromFileContents.size()) {
	    		text_log.appendText("\nInfo: Mismatch between directory and hashfile. Files have been added or removed since creation of hashfile.");
	    		text_log.appendText("\nHashDirContents: " + hashdirContents.size() + " HashFileContents: " + hashfromFileContents.size());
	    	}
	    	
	    	for (int i=0; i<(hashdirContents.size()+hashfromFileContents.size())/2; i = i+2) {
	    		
	    		String path1 = new String(hashdirContents.get(i));
	    		String path2 = new String(hashfromFileContents.get(i));
	    		
	    		if (!path1.equals(path2)) {
	    			text_log.appendText("\nInfo: Mismatch between directory and hashfile. Filepaths does not match." + hashdirContents.get(i) + " " + hashfromFileContents.get(i));	
	    		} else {
	    			
	    			String hash1 = new String(hashdirContents.get(i+1));
	    			String hash2 = new String(hashfromFileContents.get(i+1));
	    			
	    			
	    			if (!hash1.equals(hash2)) {
	    				text_log.appendText("\nInfo: Mismatch at: " + hashdirContents.get(i) + " Expected hash: " + hashdirContents.get(i+1) + " Got: " + hashfromFileContents.get(i+1));
	    			} else {
	    				text_log.appendText("\nSuccessfully verified " + hashdirContents.get(i) + " with the given hash: " + hashdirContents.get(i+1));
	    			}
	    		}
	    	}
	    	
	    	
	    	
	    }

	    public static void compareSingleFile() throws IOException {
	    	
	    	String path = dir_path.getText();
	    	File file = new File(path);
	    	infect(file);
	    	boolean found = false;
	    	String apache_sha256 = hashdirContents.get(1);
    	 	//Read from hash file
	    	readFromFileWithoutLogging();
    	 	
    	 	for (int i=1; i<hashfromFileContents.size(); i=i+2) {
    	 		String savedHash = new String(hashfromFileContents.get(i));
    	 		
    	 		if (savedHash.equals(apache_sha256)) {
    	 			found = true;
    	 			text_log.appendText("\nSuccessfully verified file " + path + " with the given hash: " + savedHash);
    	 			break;
    	 		}
    	 	}
	    	
    	 	if (found == false) {
    	 		text_log.appendText("\nFile verification failed. File has been altered or wrong hashfile provided.");
	
    	 	}
    	 	
	    }
	
	    
class hashHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
		try {
	    	hashdirContents.clear();
	    	hashfromFileContents.clear();
			FileIntegrityChecker.compareSingleFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class hashDirHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
    	hashdirContents.clear();
    	hashfromFileContents.clear();
		FileIntegrityChecker.readAndCalculateDirectory();
	}
}

class hashVerifyHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
    	hashdirContents.clear();
    	hashfromFileContents.clear();
		FileIntegrityChecker.compareAndVerify();
	}
}
	
class saveHashHandler implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent arg0) {
			try {
		    	hashdirContents.clear();
		    	hashfromFileContents.clear();
				FileIntegrityChecker.createHashFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
	









