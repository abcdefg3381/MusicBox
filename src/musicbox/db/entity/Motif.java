package musicbox.db.entity;

import java.util.ArrayList;
import java.util.List;

import maggie.network.entity.Node;

/**
 * Non db entity motif has a list of notes and its frequency.
 * 
 * @author LIU Xiaofan
 * 
 */
public class Motif implements Node {
	public static void main(String[] args) {
		Motif m1, m2;
		m1 = new Motif();
		m1.motif = new Note[] { new Note(20, 40), new Note(20, 20) };

		m2 = new Motif();
		m2.motif = new Note[] { new Note(20, 40), new Note(20, 20), new Note(20, 40) };

		System.out.println(m2.contains(m1));
	}

	private int frequency = 1;
	private int inDegree = 0;
	private float inStrength = 0;
	private Note[] motif = null;
	private int outDegree = 0;
	private float outStrength = 0;
	public List<Integer> positions = new ArrayList<Integer>();

	/**
	 * 
	 */
	public Motif() {
	}

	/**
	 * @param subList
	 */
	public Motif(List<Note> subList) {
		motif = subList.toArray(new Note[0]);
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

	/**
	 * @param obj
	 * @return
	 */
	public boolean contains(Motif obj) {
		boolean equal;
		if (getLength() > obj.getLength()) {
			int slength = obj.getLength();
			// window shift
			for (int i = 0; i <= getLength() - slength; i++) {
				equal = true;
				// compare each note
				for (int j = 0; j < slength; j++) {
					if (!motif[i + j].equals(obj.getMotif()[j])) {
						equal = false;
						break;
					}
				}
				if (equal) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Motif) {
			Motif objMotif = (Motif) obj;
			Note[] objNotes = objMotif.getMotif();
			if (objNotes.length != motif.length) {
				return false;
			}
			for (int i = 0; i < objNotes.length; i++) {
				if (!(objNotes[i].getPitch() == motif[i].getPitch() && objNotes[i].getDuration() == motif[i].getDuration())) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * @param obj
	 */
	public void exclude(Motif obj) {
		for (int i = 0; i < obj.positions.size(); i++) {
			int objPos = obj.positions.get(i);
			for (Integer pos : positions) {
				if (objPos > pos - obj.getLength() && objPos < pos + getLength()) {
					obj.positions.remove(obj.positions.get(i));
					i--;
					break;
				}
			}
		}
		// if (contains(obj)) {
		// for (int i = 0; i < obj.positions.size(); i++) {
		// int posObj = obj.positions.get(i);
		// for (Integer pos : positions) {
		// if ((pos - posObj) <= getLength() - obj.getLength()) {
		// obj.positions.remove(obj.positions.get(i));
		// i--;
		// break;
		// }
		// }
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getDegree()
	 */
	@Override
	public int getDegree() {
		return inDegree + outDegree;
	}

	public float getDuration() {
		float duration = 0;
		for (Note info : motif) {
			duration += info.getDuration();
		}
		return duration;
	}

	public int getFrequency() {
		return frequency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getId()
	 */
	@Override
	public Integer getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public synchronized float getInStrength() {
		return inStrength;
	}

	public int getLength() {
		return motif.length;
	}

	public Note[] getMotif() {
		return motif;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getName()
	 */
	@Override
	public String getName() {
		StringBuilder s = new StringBuilder();
		for (Note n : motif) {
			s.append(n.getID() + " ");
		}
		return s.toString();
	}

	@Override
	public synchronized float getOutStrength() {
		return outStrength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see maggie.common.entity.Node#getStrength()
	 */
	@Override
	public float getStrength() {
		return inStrength + outStrength;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public synchronized void setInStrength(float inStrength) {
		this.inStrength = inStrength;
	}

	public void setMotif(Note[] motif) {
		this.motif = motif;
	}

	@Override
	public synchronized void setOutStrength(float outStrength) {
		this.outStrength = outStrength;
	}

}