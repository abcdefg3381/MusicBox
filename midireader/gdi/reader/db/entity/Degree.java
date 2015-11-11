/**
 * 
 */
package gdi.reader.db.entity;

import java.util.ArrayList;
import java.util.List;

public class Degree {
	private List<AbstractMusicNote> degreeList = new ArrayList<AbstractMusicNote>();
	private AbstractMusicNote node;

	public Degree(AbstractMusicNote node) {
		super();
		this.node = node;
	}

	public List<AbstractMusicNote> getDegreeList() {
		return degreeList;
	}

	public AbstractMusicNote getNode() {
		return node;
	}

	public void setDegreeList(List<AbstractMusicNote> degreeList) {
		this.degreeList = degreeList;
	}

	public void setNode(AbstractMusicNote node) {
		this.node = node;
	}

	@Override
	public String toString() {
		StringBuffer printList = new StringBuffer();
		for (AbstractMusicNote note : degreeList) {
			printList.append(note.getId() + "\t");
		}
		return printList.toString();
	}

}