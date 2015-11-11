package gdi.reader.db.bean;

import gdi.reader.db.entity.MusicNote;
import gdi.reader.db.handler.MusicNoteHandler;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MusicNoteHandlerBean implements MusicNoteHandler {

	@PersistenceContext
	EntityManager em;

	@Override
	public ArrayList<MusicNote> getAllNotes() {
		return (ArrayList<MusicNote>) em.createQuery("from MusicNote note").getResultList();
	}

	@Override
	public List<MusicNote> getAllNotesFromSequence(int sequence) {
		Query q = em.createQuery("select note from MusicNote note where note.sequence = :seq");
		q.setParameter("seq", sequence);
		ArrayList<MusicNote> notelist = (ArrayList<MusicNote>) q.getResultList();
		return notelist;
	}

	@Override
	public void saveNote(MusicNote note) {
		try {
			em.persist(note);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
