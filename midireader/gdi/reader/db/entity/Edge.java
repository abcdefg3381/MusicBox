/**
 * 
 */
package gdi.reader.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "EDGE")
public class Edge {

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	private AbstractMusicNote preAMN;
	@ManyToOne
	private AbstractMusicNote sucAMN;

	private int weight = 1;

	public Edge() {
		super();
	}

	public Edge(AbstractMusicNote preAMN, AbstractMusicNote sucAMN) {
		super();
		this.preAMN = preAMN;
		this.sucAMN = sucAMN;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Edge) {
			Edge edge = (Edge) obj;
			return edge.getPreAMN().getId() == this.getPreAMN().getId() && edge.getSucAMN().getId() == this.getSucAMN().getId();
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public AbstractMusicNote getPreAMN() {
		return preAMN;
	}

	public AbstractMusicNote getSucAMN() {
		return sucAMN;
	}

	public int getWeight() {
		return weight;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPreAMN(AbstractMusicNote preAMN) {
		this.preAMN = preAMN;
	}

	public void setSucAMN(AbstractMusicNote sucAMN) {
		this.sucAMN = sucAMN;
	}

	public void setWeight(int pitchChange) {
		this.weight = pitchChange;
	}
}