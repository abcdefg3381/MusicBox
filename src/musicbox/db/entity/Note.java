package musicbox.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import maggie.network.entity.Node;

/**
 * A note is a network node. It has pitch, duration in database and in/out
 * degree/strength as non-db fields.
 * 
 * @author LIU Xiaofan
 * 
 */
@Entity
@Table(name = "NOTE")
public class Note implements Node, Cloneable {
	private float duration;
	@Id
	@GeneratedValue
	private int id;
	@Transient
	private int inDegree = 0;

	@Transient
	private float inStrength = 0;
	@Transient
	private int outDegree = 0;
	@Transient
	private float outStrength = 0;
	private int pitch;

	public Note() {
		super();
	}

	public Note(int pitch, float duration) {
		super();
		this.pitch = pitch;
		this.duration = duration;
	}

	public Note(Note noteInfo) {
		this.id = noteInfo.id;
		this.pitch = noteInfo.pitch;
		this.duration = noteInfo.duration;
	}

	@Override
	public void addDegree(int i) {
		this.inDegree++;
		this.outDegree++;
	}

	@Override
	public void addInDegree() {
		this.inDegree++;
	}

	@Override
	public void addOutDegree() {
		this.outDegree++;
	}

	@Override
	public Note clone() {
		try {
			return (Note) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Note) {
			Note info = (Note) obj;
			if (info.getID() != 0) {
				return info.getID() == this.id;
			} else {
				return info.getPitch() == this.getPitch() && info.getDuration() == this.getDuration();
			}
		}
		return super.equals(obj);
	}

	@Override
	public int getDegree() {
		return inDegree + outDegree;
	}

	public float getDuration() {
		return duration;
	}

	@Override
	public Integer getID() {
		return id;
	}

	public int getInDegree() {
		return inDegree;
	}

	@Override
	public float getInStrength() {
		return inStrength;
	}

	@Override
	public String getName() {
		return getID() + "";
		// return this.getPitch() + "\t" + this.getDuration();
	}

	public int getOutDegree() {
		return outDegree;
	}

	@Override
	public float getOutStrength() {
		return outStrength;
	}

	public int getPitch() {
		return pitch;
	}

	@Override
	public float getStrength() {
		return inStrength + outStrength;
	}

	public void setDuration(float length) {
		this.duration = length;
	}

	public void setDuration(long startTick, long endTick, int resolution) {
		// one resolution means a quarter note
		this.duration = (int) ((endTick - startTick) / (resolution * 0.9f));
		float reminder = ((endTick - startTick) / (resolution * 0.9f)) % 1;

		// use maximum resolution to 0.25
		double divided = 0.125;
		double lamda = 0.25;
		reminder = ((int) ((reminder + divided) / lamda)) / (float) 4;

		// use 1/ 8 of quaternote plus tercet detection
		// double divided = 0.0625;
		// double lamda = 0.125;
		// if (reminder > 0.3 && reminder < 0.35) {
		// reminder = 1 / 3;
		// } else if (reminder > 0.64 && reminder < 0.7) {
		// reminder = 2 / 3;
		// } else {
		// reminder = ((int) ((reminder + divided) / lamda)) / (float) 8;
		// }

		setDuration(this.duration + reminder);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	@Override
	public void setInStrength(float inStrength) {
		this.inStrength = inStrength;
	}

	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}

	@Override
	public void setOutStrength(float outStrength) {
		this.outStrength = outStrength;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	@Override
	public String toString() {
		return this.getID() + "\t" + this.getOutStrength();
	}

}