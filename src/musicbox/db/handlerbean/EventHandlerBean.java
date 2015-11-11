package musicbox.db.handlerbean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import musicbox.db.entity.Event;
import musicbox.db.entity.Note;
import musicbox.db.handler.EventHandler;

/**
 * Stateless bean implements {@link EventHandler}
 * 
 * @author LIU Xiaofan
 * 
 */
@Stateless
public class EventHandlerBean implements EventHandler {

	@PersistenceContext
	EntityManager em;

	public Event saveEvent(Event event) {
		em.persist(event);
		return event;
	}

	@Override
	public void saveEvents(List<Event> eventList) {
		for (Event event : eventList) {
			saveNote(event.getNote());
			saveEvent(event);
		}
	}

	@SuppressWarnings("unchecked")
	public Note saveNote(Note note) {
		try {
			Query q = em.createQuery("from Note note where note.pitch = :pitch and note.duration = :duration");
			q.setParameter("pitch", note.getPitch());
			q.setParameter("duration", note.getDuration());
			List result = q.getResultList();
			if (result.size() == 0) {
				em.persist(note);
			} else {
				note.setId(((Note) result.get(0)).getID());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return note;
	}
}
