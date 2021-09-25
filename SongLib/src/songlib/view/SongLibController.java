package songlib.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML ListView<Song> songListView;
	private ObservableList<Song> songList;
	
	public void start(Stage primaryStage) {
		// Creates an observable list, and connects it to the songListView
		songList = FXCollections.observableArrayList(new Song("Song1", "Artist1"));
		songListView.setItems(songList);
		
		// Selects the first song in the list
		songListView.getSelectionModel().select(0);
		
		// Sets the listener for the songListView
		songListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> selectSong());
	}
	
	public void addSong(ActionEvent e) {
		System.out.println("addSong called");
	}
	public void deleteSong(ActionEvent e) {
		System.out.println("deleteSong called");
		
		// Checks to ensure there is a valid song selected, and then deletes it 
		int index = songListView.getSelectionModel().getSelectedIndex();
		if(index >= 0) {
			songList.remove(songListView.getSelectionModel().getSelectedIndex());
		}
	}
	
	public void selectSong() {
		System.out.println("selectSong called: " + songListView.getSelectionModel().getSelectedIndex());
	}
}

class Song {
	private String name = "name";
	private String artist = "artist";
	private String album = "album";
	private int year = 0;
	
	public Song() { }
	public Song(String name) {
		setName(name);
	}
	public Song(String name, String artist) {
		setName(name);
		setArtist(artist);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getArtist() {
		return name;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return name;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		if(year < 0) {
			year = year * (-1);
		}
		this.year = year;
	}
	
	public String toString() {
		return name + " : " + artist;
	}
}
