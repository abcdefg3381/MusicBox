package musicbox.logic;

import java.awt.Desktop;
import java.io.File;

import javax.sound.midi.Sequence;

import musicbox.MainProgram;
import musicbox.db.entity.MusicalNetwork;
import musicbox.gui.viz.MIDIViewer;
import musicbox.logic.compose.Composer;
import musicbox.logic.dbc.DBConnector;
import musicbox.logic.form.NetworkFormer;
import musicbox.logic.stat.MyStatistics;
import musicbox.logic.util.midi.MIDIPlayer;
import musicbox.logic.util.midi.MIDIReader;
import musicbox.logic.util.midi.MIDIWriter;

import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;

/**
 * Main logic of client application.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MusicBoxLogic {
	private DBConnector dbc;

	/**
	 * Network from composed music
	 */
	private MusicalNetwork networkComposed;

	private NetworkFormer networkFormer;

	/**
	 * Network from original music compositions
	 */
	private MusicalNetwork networkOrigin;

	/**
	 * MIDI sequence for play back
	 */
	private Sequence sequence;

	public void bootstrapStart() {
		// Boot the JBoss Microcontainer with EJB3 settings, automatically
		// loads ejb3-interceptors-aop.xml and embedded-jboss-beans.xml
		EJB3StandaloneBootstrap.boot(null);

		// Deploy custom stateless beans (datasource, mostly)
		EJB3StandaloneBootstrap.deployXmlResource("META-INF/musicBox-beans.xml");

		// Deploy all EJBs found on classpath (slow, scans all)
		// EJB3StandaloneBootstrap.scanClasspath();

		// Deploy all EJBs found on classpath (fast, scans only build directory)
		// This is a relative location, matching the substring end of one of
		// java.class.path locations!
		// Print out System.getProperty("java.class.path") to understand this...
		EJB3StandaloneBootstrap.scanClasspath("MusicBox/build".replace("/", File.separator));
	}

	public void bootstrapStop() {
		// Shutdown EJB container
		EJB3StandaloneBootstrap.shutdown();
	}

	public void compose(int weight, int scale, int major, int lengthPerComp, int number) {
		networkComposed = new MusicalNetwork();
		networkComposed.setNodeList(Composer.compose(networkOrigin, Composer.RANDOMWALK));
		networkFormer = new NetworkFormer(networkComposed);
		networkFormer.formNetworkComposed();

		MainProgram.getInstance().getGUI().getVizComposedPanel().setNetwork(networkComposed);
	}

	@Deprecated
	public void composeWithMotif(boolean with, boolean wo) {
		if (with) {
			Composer.compose(networkOrigin, Composer.RANDOMOTIF);
		} else if (wo) {
			Composer.compose(networkOrigin, Composer.MOTIF);
		}
	}

	public boolean fetchNetworkOrigin(Object[] compositionTitles, int edgeType, int nodeType, boolean skyline) {
		// get network with notelist, eventlist and compositionlist.
		this.networkOrigin = getDbc().getNetwork(compositionTitles);

		networkFormer = new NetworkFormer(networkOrigin, skyline, nodeType, edgeType);
		networkFormer.formNetworkOrigin();

		MainProgram.getInstance().getGUI().getVizOriginPanel().setNetwork(networkOrigin);
		return true;
	}

	private MusicalNetwork fetchSingleComposition(Object selectedValue) {
		return getDbc().getNetwork(new Object[] { selectedValue });
	}

	public void findMotifs(String args) {
		File statFile = new File("mfinder/motif.txt");
		MyStatistics ms = new MyStatistics(networkOrigin, statFile);
		ms.getMotif(statFile, args);
	}

	public DBConnector getDbc() {
		if (dbc == null) {
			dbc = new DBConnector(MainProgram.getInstance().getInitialContext());
		}
		return dbc;
	}

	/**
	 * Network from composed music
	 */
	public MusicalNetwork getNetworkComposed() {
		return networkComposed;
	}

	public Object[] getNetworkList() {
		return getDbc().getNetworkList();
	}

	/**
	 * Network from original music compositions
	 */
	public MusicalNetwork getNetworkOrigin() {
		return networkOrigin;
	}

	public void getStatistics(MusicalNetwork network, String name) {
		File statFile = new File("report/" + name + "stat.txt");
		MyStatistics ms = new MyStatistics(network, statFile);
		ms.printReport();
		try {
			Desktop.getDesktop().open(statFile);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void importFile(File file) {
		MIDIReader midiReader = new MIDIReader();
		MusicalNetwork importedNetwork = midiReader.importFile(file, 72);
		getDbc().saveNetwork(importedNetwork);
	}

	/**
	 * @param selectedValue
	 */
	public void playMIDI(Object selectedValue) {
		MIDIPlayer.play(MIDIWriter.writeEventsToSequence(fetchSingleComposition(selectedValue).getEventList()));
	}

	public void reconnect() {
		getDbc().disconnect();
		dbc = null;
		bootstrapStop();
		bootstrapStart();
	}

	/**
	 * @param network
	 */
	public void saveMIDItoFile(MusicalNetwork network) {
		MIDIWriter.writeEventsToFile(network.getEventList(), network.getName());
	}

	public void tempMIDIPlay() {
		MIDIPlayer.play(sequence);
	}

	public void tempMIDISave() {
		MIDIWriter.write();
	}

	public void tempMIDIStop() {
		MIDIPlayer.stop();
	}

	public void vizMIDI(Object selectedValue) {
		new MIDIViewer(fetchSingleComposition(selectedValue));
	}
}
