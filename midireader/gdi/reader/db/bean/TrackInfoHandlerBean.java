package gdi.reader.db.bean;

import gdi.reader.db.entity.TrackInfo;
import gdi.reader.db.handler.TrackInfoHandler;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TrackInfoHandlerBean implements TrackInfoHandler {

	@PersistenceContext
	EntityManager em;

	@Override
	public ArrayList<TrackInfo> getAllTrackInfo() {
		return (ArrayList<TrackInfo>) em.createQuery("from TrackInfo note").getResultList();
	}

	@Override
	public TrackInfo saveTrackInfo(TrackInfo channel) {
		em.persist(channel);
		return channel;
	}

}
