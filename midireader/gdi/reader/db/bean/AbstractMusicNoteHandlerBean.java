package gdi.reader.db.bean;

import gdi.reader.db.entity.AbstractMusicNote;
import gdi.reader.db.handler.AbstractMusicNoteHandler;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AbstractMusicNoteHandlerBean implements AbstractMusicNoteHandler {

	@PersistenceContext
	EntityManager em;

	@Override
	public AbstractMusicNote findOrSaveAbstractMusicNote(AbstractMusicNote note) {
		AbstractMusicNote amn;
		try {
			Query q = em.createQuery("select c from AbstractMusicNote c where c.pitch = :pitch and c.length = :length");
			q.setParameter("pitch", note.getPitch());
			q.setParameter("length", note.getLength());
			amn = (AbstractMusicNote) q.getSingleResult();
		} catch (Exception e) {
			em.persist(note);
			return note;
		}
		return amn;
	}

	@Override
	public int getAbstractMusicNoteNumber() {
		return em.createQuery("from AbstractMusicNote note").getResultList().size();
	}

	@Override
	public ArrayList<AbstractMusicNote> getAllAbstractMusicNotes() {
		return (ArrayList<AbstractMusicNote>) em.createQuery("from AbstractMusicNote note").getResultList();
	}

}
