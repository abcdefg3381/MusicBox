package musicbox.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The composition entity has information about key, time signature, resolution
 * and title in database.<br>
 * It also has a array of rhythm of each bar which is transient to the DB.
 * 
 * @author LIU Xiaofan
 * 
 */
@Entity
@Table(name = "COMPOSITION")
public class Composition {
	@Id
	@GeneratedValue
	private int id;

	private int keySignature;

	private int resolution;

	@Transient
	private int[] rhythm;

	private int timeSignature;

	private String title;

	public Composition() {
		super();
	}

	public Composition(int resolution, String title) {
		super();
		this.resolution = resolution;
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Composition) {
			Composition c = (Composition) obj;
			return c.getId() == getId();
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public int getKeySignature() {
		return keySignature;
	}

	public int getResolution() {
		return resolution;
	}

	public int[] getRhythm() {
		return rhythm;
	}

	public int getTimeSignature() {
		return timeSignature;
	}

	public String getTitle() {
		return title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKeySignature(int keySignature) {
		this.keySignature = keySignature;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public void setRhythm(int[] rhythm) {
		this.rhythm = rhythm;
	}

	public void setTimeSignature(int timeSignature) {
		this.timeSignature = timeSignature;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
