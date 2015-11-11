package gdi.reader.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TRACK_INFO")
public class TrackInfo {

	public static int CLASSIC = 0;

	public static int FOLK = 2;

	public static int POP = 1;

	private String author;

	private String collectionName;

	private int genre;

	@Id
	@GeneratedValue
	private int id;

	private int sequence;

	private int track;

	public TrackInfo() {
		super();
	}

	public TrackInfo(int sequence, int track, String author, String collectionName, int genre) {
		super();
		this.sequence = sequence;
		this.track = track;
		this.author = author;
		this.collectionName = collectionName;
		this.genre = genre;
	}

	public boolean equals(TrackInfo info) {
		return info.getSequence() == this.getSequence() && info.getTrack() == this.getTrack();
	}

	public String getAuthor() {
		return author;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public int getGenre() {
		return genre;
	}

	public int getId() {
		return id;
	}

	public int getSequence() {
		return sequence;
	}

	public int getTrack() {
		return track;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setTrack(int track) {
		this.track = track;
	}
}
