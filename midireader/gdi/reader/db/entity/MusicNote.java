/**
 * 
 */
package gdi.reader.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * This class constructs an object to store music notes information. The
 * following information of the music notes are stored:
 * 
 * Previous Note * # Pitch * Pitch Difference with previous one * # Start Time
 * End Time Length * Velocity Track number Channel
 * 
 * The fields with a * mark are to be stored into the database.
 * 
 * @author GDI
 * 
 */

@Entity
@Table(name = "MUSIC_NOTES")
public class MusicNote {

	@ManyToOne
	private AbstractMusicNote amn;

	private int channel;
	private long endTick;

	@Id
	@GeneratedValue
	private int id;
	private int sequence;

	private long startTick;

	@ManyToOne(fetch = FetchType.EAGER)
	private TrackInfo trackInfo;

	private int velo;

	public MusicNote() {
		super();
	}

	public MusicNote(long startTick, long endTick, AbstractMusicNote amn, int velo, TrackInfo trackInfo) {
		super();
		this.startTick = startTick;
		this.endTick = endTick;
		this.amn = amn;
		this.velo = velo;
		this.trackInfo = trackInfo;
	}

	public AbstractMusicNote getAmn() {
		return amn;
	}

	public int getChannel() {
		return channel;
	}

	public long getEndTick() {
		return endTick;
	}

	public int getId() {
		return id;
	}

	// public TrackInfo getTrackInfo() {
	// return trackInfo;
	// }
	//
	// public void setTrackInfo(TrackInfo trackInfo) {
	// this.trackInfo = trackInfo;
	// }

	public int getSequence() {
		return sequence;
	}

	public long getStartTick() {
		return startTick;
	}

	public TrackInfo getTrackInfo() {
		return trackInfo;
	}

	public int getVelo() {
		return velo;
	}

	public void setAmn(AbstractMusicNote amn) {
		this.amn = amn;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setEndTick(long endTick) {
		this.endTick = endTick;
	}

	public void setEndTick(long endTick, int resolution) {
		this.endTick = endTick;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setStartTick(long startTick) {
		this.startTick = startTick;
	}

	public void setTrackInfo(TrackInfo trackInfo) {
		this.trackInfo = trackInfo;
	}

	public void setVelo(int velo) {
		this.velo = velo;
	}
}
