package gdi.reader.db.bean;

import gdi.reader.db.entity.AbstractMusicNote;
import gdi.reader.db.entity.Edge;
import gdi.reader.db.handler.EdgeHandler;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EdgeHandlerBean implements EdgeHandler {

	@PersistenceContext
	EntityManager em;

	@Override
	public ArrayList<Edge> getAllEdges() {
		return (ArrayList<Edge>) em.createQuery("from Edge edge").getResultList();
	}

	@Override
	public int getEdgeNumber() {
		return em.createQuery("from Edge edge").getResultList().size();
	}

	@Override
	public List<Edge> getEdgeWithPreAMN(int oldID) {
		try {
			Query q = em.createQuery("select amn from AbstractMusicNote amn where amn.id = :id");
			q.setParameter("id", oldID);
			AbstractMusicNote amn = (AbstractMusicNote) q.getSingleResult();

			q = em.createQuery("from Edge edge where edge.preAMN = :preAMN");
			q.setParameter("preAMN", amn);
			List<Edge> edgeList = q.getResultList();
			return edgeList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void saveEdge(Edge edge) {
		em.persist(edge);
	}
}
