package gdi.reader.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ABSTRACT_MUSIC_NOTE")
public class AbstractMusicNote {

	@Id
	@GeneratedValue
	private int id;

	private float length;

	private int pitch;

	public AbstractMusicNote() {
		super();
	}

	public AbstractMusicNote(int pitch, float length) {
		super();
		this.pitch = pitch;
		this.length = length;
	}

	public int getId() {
		return id;
	}

	public float getLength() {
		return length;
	}

	public int getPitch() {
		return pitch;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public void setLength(long startTick, long endTick, int resolution) {
		// one resolution means a quater note
		this.length = (int) ((endTick - startTick) / (float) resolution);
		float reminder = ((endTick - startTick) / (float) resolution) % 1;

		// use 16 or 8 intervals to seperate the reminder.

		// use 16
		double divided = 0.03125;
		double lamda = 0.0625;
		reminder = ((int) ((reminder + divided) / lamda)) / (float) 16;

		// use 8
		// double divided = 0.0625;
		// double lamda = 0.125;
		// reminder = ((int) ((reminder + divided) / lamda)) / (float) 8;

		setLength(this.length + reminder);
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
}
