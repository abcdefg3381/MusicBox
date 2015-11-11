/**
 * 
 */
package musicbox.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class constructs an object to store music notes information. The
 * following information of the music notes are stored:
 * 
 * @author LIU Xiaofan
 * 
 */
@Entity
@Table(name = "EVENT")
public class Event {

	private int bar;

	@Transient
	private int channel;

	@ManyToOne(fetch = FetchType.EAGER)
	private Composition composition;

	private long endTick;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Note note;

	private long startTick;

	private float unit;

	@Transient
	private int velo;

	public Event() {
		super();
	}

	public Event(Event event) {
		this.id = event.id;
		this.note = new Note(event.note);
	}

	public Event(Note note, int bar, float unit) {
		this.note = note;
		this.bar = bar;
		this.unit = unit;
	}

	public int getBar() {
		return bar;
	}

	public int getChannel() {
		return channel;
	}

	public Composition getComposition() {
		return composition;
	}

	public long getEndTick() {
		return endTick;
	}

	public int getId() {
		return id;
	}

	public Note getNote() {
		return note;
	}

	public long getStartTick() {
		return startTick;
	}

	public float getUnit() {
		return unit;
	}

	public int getVelo() {
		return velo;
	}

	public void setBar(int bar) {
		this.bar = bar;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public void setEndTick(long endTick) {
		this.endTick = endTick;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public void setStartTick(long startTick) {
		this.startTick = startTick;
	}

	public void setUnit(float unit) {
		this.unit = unit;
	}

	public void setVelo(int velo) {
		this.velo = velo;
	}
}
