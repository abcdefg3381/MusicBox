package musicbox.db.handlerbean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import musicbox.db.entity.Composition;
import musicbox.db.handler.CompositionHandler;

/**
 * Stateless bean implements {@link CompositionHandler}
 * 
 * @author LIU Xiaofan
 * 
 */
@Stateless
public class CompositionHandlerBean implements CompositionHandler {

	@PersistenceContext
	EntityManager em;

	@Override
	public Object[] getCompositionTitles() {
		return em.createQuery("select DISTINCT title from Composition").getResultList().toArray();
	}

	@Override
	public void saveCompositions(List<Composition> compositions) {
		for (Composition composition : compositions) {
			em.persist(composition);
		}
	}

}
