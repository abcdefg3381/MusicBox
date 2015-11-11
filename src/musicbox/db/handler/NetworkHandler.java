package musicbox.db.handler;

import musicbox.db.entity.MusicalNetwork;

/**
 * Network Handler is able to get a network with given compositions (in title)
 * 
 * @author LIU Xiaofan
 * 
 */
public interface NetworkHandler {

	MusicalNetwork getNetwork(Object[] compositionTitles);
}
