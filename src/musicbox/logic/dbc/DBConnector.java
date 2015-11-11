package musicbox.logic.dbc;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import musicbox.db.entity.Composition;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.handler.CompositionHandler;
import musicbox.db.handler.EventHandler;
import musicbox.db.handler.NetworkHandler;

/**
 * Connects to the handlers to get/save network(s)
 * 
 * @author LIU Xiaofan
 * 
 */
public class DBConnector {

	private CompositionHandler compositionHandler;

	private EventHandler eventHandler;
	private InitialContext ic;
	private NetworkHandler networkHandler;

	public DBConnector(InitialContext ic) {
		this.ic = ic;
		try {
			networkHandler = (NetworkHandler) ic.lookup("NetworkHandlerBean/local");
			compositionHandler = (CompositionHandler) ic.lookup("CompositionHandlerBean/local");
			eventHandler = (EventHandler) ic.lookup("EventHandlerBean/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			// TODO find all the bindings
			ic.unbind("NetworkHandlerBean/local");
			ic.unbind("CompositionHandlerBean/local");
			ic.unbind("EventHandlerBean/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Network constructed here contains both type of edges. However node
	 * degree, node strength are count only by co-occurrent edges.
	 * 
	 * @param CompositionTitles
	 * @param edgeType
	 * @param formNetwork
	 * @return
	 */
	public MusicalNetwork getNetwork(Object[] CompositionTitles) {
		// get network with event list
		MusicalNetwork network = networkHandler.getNetwork(CompositionTitles);
		// set title
		StringBuilder title = new StringBuilder();
		for (Composition comp : network.getCompositions()) {
			title.append(comp.getTitle() + " ");
		}
		network.setName(title.toString());
		return network;
	}

	public Object[] getNetworkList() {
		return compositionHandler.getCompositionTitles();
	}

	public boolean saveNetwork(MusicalNetwork importedNetwork) {
		compositionHandler.saveCompositions(importedNetwork.getCompositions());
		eventHandler.saveEvents(importedNetwork.getEventList());
		return true;
	}
}
