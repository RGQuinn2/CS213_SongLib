/*
 * Robert Quinn
 * Christopher Chopping
 */

package songlib.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML ListView<Song> songListView;
	private ObservableList<Song> songList;

	@FXML TextField songNameField;
	@FXML TextField songArtistField;
	@FXML TextField songAlbumField;
	@FXML TextField songYearField;
	
	@FXML Text inputFeedbackText;
	
	public void start(Stage primaryStage) {		
		// Creates an observable list, and connects it to the songListView
		songList = FXCollections.observableArrayList();
		songListView.setItems(songList);
		
		// loads song list from file
		loadSongListFromFile();
		
		// Sets the listener for the songListView
		songListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> selectSong());
		
		// Selects the first song in the list
		songListView.getSelectionModel().select(0);
	}
	
	// Clears all of the detail fields and sets the current selection to nothing, so a new song can be saved
	public void addSong(ActionEvent e) {
		songListView.getSelectionModel().select(-1);
		songNameField.clear();
		songArtistField.clear();
		songAlbumField.clear();
		songYearField.clear();
	}
	
	// Is called when the user presses the Delete button
	// Removes the currently selected song from the list, and saves the list
	public void deleteSong(ActionEvent e) {		
		// Checks to ensure there is a valid song selected, and then deletes it 
		int index = songListView.getSelectionModel().getSelectedIndex();
		if(index >= 0) {
			songList.remove(songListView.getSelectionModel().getSelectedIndex());
		}
		
		saveSongListToFile();
	}
	
	// Is called whenever the selection of the listView changes
	public void selectSong() {
		inputFeedbackText.setText("");
		
		songNameField.clear();
		songArtistField.clear();
		songAlbumField.clear();
		songYearField.clear();
		if(songListView.getSelectionModel().getSelectedIndex() != -1) {
			songNameField.setText(songListView.getSelectionModel().getSelectedItem().getName());
			songArtistField.setText(songListView.getSelectionModel().getSelectedItem().getArtist());
			songAlbumField.setText(songListView.getSelectionModel().getSelectedItem().getAlbum());
			songYearField.setText(songListView.getSelectionModel().getSelectedItem().getYear());
		}
	}
	
	// Called when the user presses the save button
	// Updates the details of the selected song, or saves a new song if no song was selected
	public void saveEdit() {
		Song song = songListView.getSelectionModel().getSelectedItem();
		
		String nameInput = songNameField.getText();
		String artistInput = songArtistField.getText();
		String albumInput = songAlbumField.getText();
		String yearInput = songYearField.getText();
		
		if(Song.formatValidString(nameInput).isBlank() || Song.formatValidString(artistInput).isBlank()) {
			inputFeedbackText.setFill(Color.RED);
			inputFeedbackText.setText("Failed to save! Must include valid name and artist!");
			return;
		}
		
		if(!CheckDuplicate(nameInput, artistInput, albumInput, yearInput, songListView.getSelectionModel().getSelectedIndex())) {
			if(song == null) {
				song = new Song(nameInput, artistInput, albumInput, yearInput);
				songList.add(song);
				songList.sort(null);
				songListView.getSelectionModel().select(songList.indexOf(song));
				
				saveSongListToFile();
				
				inputFeedbackText.setFill(Color.GREEN);
				inputFeedbackText.setText("Saved new song!");
				return;
			}
			else {
				song.setName(nameInput);
				song.setArtist(artistInput);
				song.setAlbum(albumInput);
				song.setYear(yearInput);
				songList.sort(null);
				
				saveSongListToFile();
				
				inputFeedbackText.setFill(Color.GREEN);
				inputFeedbackText.setText("Saved updated song details!");
				return;
			}
		}
		else {
			inputFeedbackText.setFill(Color.RED);
			inputFeedbackText.setText("Failed to save! Name/Artist combination already exists!");
			return;
		}
	}
	
	// Called when the user cancels their planned edit.
	public void cancelEdit() {
		selectSong();
	}
	
	// Checks to make sure there are no songs matching the provided details
	// ignoreIndex should be the index of the song currently being modified, to prevent it from detecting itself as a duplicate
	public boolean CheckDuplicate(String name, String artist, String album, String year, int ignoreIndex) {
		
		String checkString = name.strip() + " : " + artist.strip();
		for(Song song : songList) {
			if(song.toString().equalsIgnoreCase(checkString)) {
				if(songList.indexOf(song) != ignoreIndex) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Loads all songs from the songlist.txt file and populates the observed songList
	public void loadSongListFromFile() {
		try {
			File songListFile = new File("songlist.txt");
			Scanner songFileScanner;
			songFileScanner = new Scanner(songListFile);
			while(songFileScanner.hasNextLine()) {
				Song loadedSong = Song.fromString(songFileScanner.nextLine());
				songList.add(loadedSong);
			}
			songFileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Saves the entire current song list to the songlist.txt file
	public void saveSongListToFile() {
		try {
			File songListFile = new File("songlist.txt");
			FileWriter songListFileWriter = new FileWriter("songlist.txt");
			for(Song song : songList) {
				songListFileWriter.write(song.saveString() + '\n');
			}
			songListFileWriter.close();
			System.out.println("songlist.txt saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Song implements Comparable<Song>{
	private String name = "name";
	private String artist = "artist";
	private String album = "album";
	private String year = "0";
	
	public Song() { }
	public Song(String name, String artist) {
		setName(name);
		setArtist(artist);
	}
	public Song(String name, String artist, String album) {
		setName(name);
		setArtist(artist);
		setAlbum(album);
	}
	public Song(String name, String artist, String album, String year) {
		setName(name);
		setArtist(artist);
		setAlbum(album);
		setYear(year);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = formatValidString(name);
	}
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = formatValidString(artist);
	}
	
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = formatValidString(album);
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = formatValidString(year);
	}
	
	public String toString() {
		return name + " : " + artist;
	}
	
	// formats the given string into a valid format to be saved as a song
	public static String formatValidString(String stringToFormat) {
		return stringToFormat.replaceAll("name:|artist:|album:|year:|\\|", "").strip();
	}
	
	@Override
	public int compareTo(Song o) {
		String thisString = this.toString();
		String otherString = o.toString();
		return thisString.compareToIgnoreCase(otherString);
	}
	
	// Creates a string representation of the song from which it can be loaded later
	public String saveString() {
		return "name:" + name + "|artist:" + artist + "|album:" + album + "|year:" + year;
	}
	
	// Returns a song with the details extracted from the given string parameter
	public static Song fromString(String loadString) {
		// Declares empty string variables to hold information extracted from the input string
		String loadName = "";
		String loadArtist = "";
		String loadAlbum = "";
		String loadYear = "";
		
		// Pattern matches the input string to extract song details
		Pattern songStringPattern = Pattern.compile("(?:\\Qname:\\E)(.+)(?:\\Q|artist:\\E)(.+)(?:\\Q|album:\\E)(.*)(?:\\Q|year:\\E)(.*)", Pattern.CASE_INSENSITIVE);
		Matcher songStringMatcher = songStringPattern.matcher(loadString);
		
		// Captures details from the pattern matching and assigns them to variables
		if(songStringMatcher.find()) {
			loadName = songStringMatcher.group(1);
			loadArtist = songStringMatcher.group(2);
			loadAlbum = songStringMatcher.group(3);
			loadYear = songStringMatcher.group(4);
		}
		
		// Creates and returns a new song with the extracted details
		return new Song(loadName, loadArtist, loadAlbum, loadYear);
	}
}
