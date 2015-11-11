package musicbox.db.handlerbean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import musicbox.db.entity.Composition;
import musicbox.db.entity.Event;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.handler.NetworkHandler;

/**
 * Stateless bean implements {@link NetworkHandler}
 * 
 * @author LIU Xiaofan
 * 
 */
@Stateless
public class NetworkHandlerBean implements NetworkHandler {
	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public MusicalNetwork getNetwork(Object[] compositionTitles) {
		MusicalNetwork network = new MusicalNetwork();
		// 1. require for compositions
		StringBuilder query = new StringBuilder("from Composition composition where (");
		for (Object object : compositionTitles) {
			query.append(" composition.title = '" + (String) object + "' OR");
		}
		query.delete(query.lastIndexOf("OR"), query.length());
		query.append(")");

		network.setCompositions(em.createQuery(query.toString()).getResultList());

		// 2. require for events
		query = new StringBuilder("from Event event where (");
		for (Composition composition : network.getCompositions()) {
			query.append("event.composition.id = " + composition.getId() + " OR ");
		}
		query.delete(query.lastIndexOf("OR"), query.length());
		query.append(") order by event.startTick");

		List<Event> eventlist = em.createQuery(query.toString()).getResultList();

		network.setEventList(eventlist);

		return network;
	}

}
