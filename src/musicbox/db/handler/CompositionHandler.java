package musicbox.db.handler;

import java.util.List;

import musicbox.db.entity.Composition;

/**
 * Composition Handler is able to get composition titles and save compositions.
 * 
 * @author LIU Xiaofan
 * 
 */
public interface CompositionHandler {

	public Object[] getCompositionTitles();

	public void saveCompositions(List<Composition> compositions);
}
