package gdi.reader.db.handler;

import gdi.reader.db.entity.MusicNote;

import java.util.ArrayList;
import java.util.List;

public interface MusicNoteHandler {

	public ArrayList<MusicNote> getAllNotes();

	public List<MusicNote> getAllNotesFromSequence(int sequence);

	public void saveNote(MusicNote note);
}
