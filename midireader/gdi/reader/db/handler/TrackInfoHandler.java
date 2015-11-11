package gdi.reader.db.handler;

import gdi.reader.db.entity.TrackInfo;

import java.util.ArrayList;

public interface TrackInfoHandler {

	public ArrayList<TrackInfo> getAllTrackInfo();

	public TrackInfo saveTrackInfo(TrackInfo channel);
}
