package gdi.reader.db.handler;

import gdi.reader.db.entity.AbstractMusicNote;

import java.util.ArrayList;

public interface AbstractMusicNoteHandler {

	public AbstractMusicNote findOrSaveAbstractMusicNote(AbstractMusicNote note);

	public int getAbstractMusicNoteNumber();

	public ArrayList<AbstractMusicNote> getAllAbstractMusicNotes();
}
