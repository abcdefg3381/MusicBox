package musicbox.db.handler;

import java.util.List;

import musicbox.db.entity.Event;

/**
 * Event Handler saves a list of music event.
 * 
 * @author LIU Xiaofan
 * 
 */
public interface EventHandler {

	public void saveEvents(List<Event> eventList);
}
