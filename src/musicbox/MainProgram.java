package musicbox;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import musicbox.gui.MusicBoxClientGUI;
import musicbox.logic.MusicBoxLogic;

/**
 * The main programme starts the application.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MainProgram {

	private static MainProgram instance;

	public static MainProgram getInstance() {
		if (instance == null) {
			instance = new MainProgram();
		}
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MusicBox starting...\nAuthor: LIU Xiaofan");
		long time = System.currentTimeMillis();
		getInstance().getLogic().bootstrapStart();
		getInstance().getGUI();
		System.out.println("MusicBox started in " + (System.currentTimeMillis() - time) + " ms.");
	}

	private MusicBoxClientGUI musicBoxClientGUI;

	private MusicBoxLogic musicBoxLogic;

	public void exit() {
		getInstance().getLogic().bootstrapStop();
		System.exit(0);
	}

	public MusicBoxClientGUI getGUI() {
		if (musicBoxClientGUI == null) {
			musicBoxClientGUI = new MusicBoxClientGUI();
			musicBoxClientGUI.fetchData();
		}
		return musicBoxClientGUI;
	}

	public InitialContext getInitialContext() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public MusicBoxLogic getLogic() {
		if (musicBoxLogic == null) {
			musicBoxLogic = new MusicBoxLogic();
		}
		return musicBoxLogic;
	}
}
