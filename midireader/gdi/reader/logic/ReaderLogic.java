package gdi.reader.logic;

import gdi.reader.MIDIReader;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.hsqldb.Server;
import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;

public class ReaderLogic {
	private static Sequencer sm_sequencer = null;

	private static Synthesizer sm_synthesizer = null;

	private File[] files;

	private Server hsqldbServer = null;

	public void composeSamplePiece() {
		// compose according to edge weight
		// Composer composer = new Composer();
		// composer.compose();
		// compose according to note degree
		Composer2 composer2 = new Composer2();
		composer2.compose();
	}

	public boolean getStatistics() {

		JFileChooser fc = new JFileChooser("./report/");
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".txt");
			}

			@Override
			public String getDescription() {
				return ".txt file";
			}
		});
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File acceptFile =
					fc.getSelectedFile().getName().toLowerCase().endsWith(".txt") ? fc.getSelectedFile() : new File(fc
							.getSelectedFile().getAbsolutePath() + ".txt");
			if (DDCCCounter.countDataBaseToFile(acceptFile)) {
				try {
					Desktop.getDesktop().open(acceptFile);
					return true;
				} catch (IOException e) {
				}
			}
			return false;
		} else {
			return true;
		}
	}

	public void importFile(String author, String collectionName, int genre) {
		MIDIReader.getInstance().getGUI().setFileImporter(new FileImporter(files, author, collectionName, genre));
	}

	public void playTempMIDI() {
		/*
		 * We read in the MIDI file to a Sequence object. This object is set at
		 * the Sequencer later.
		 */
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(new File("temp.mid"));
		} catch (InvalidMidiDataException e) {
			/*
			 * In case of an exception, we dump the exception including the
			 * stack trace to the console. Then, we exit the program.
			 */
			e.printStackTrace();
			return;
		} catch (IOException e) {
			/*
			 * In case of an exception, we dump the exception including the
			 * stack trace to the console. Then, we exit the program.
			 */
			JOptionPane.showMessageDialog(null, "No Music Composed!", "File Not Found", JOptionPane.WARNING_MESSAGE);
			return;
		}

		/*
		 * Now, we need a Sequencer to play the sequence. Here, we simply
		 * request the default sequencer.
		 */
		try {
			sm_sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}
		if (sm_sequencer == null) {
			return;
		}

		/*
		 * There is a bug in the Sun jdk1.3/1.4. It prevents correct termination
		 * of the VM. So we have to exit ourselves. To accomplish this, we
		 * register a Listener to the Sequencer. It is called when there are
		 * "meta" events. Meta event 47 is end of track.
		 * 
		 * Thanks to Espen Riskedal for finding this trick.
		 */
		sm_sequencer.addMetaEventListener(new MetaEventListener() {
			@Override
			public void meta(MetaMessage event) {
				if (event.getType() == 47) {
					sm_sequencer.close();
					if (sm_synthesizer != null) {
						sm_synthesizer.close();
					}
					return;
				}
			}
		});

		/*
		 * The Sequencer is still a dead object. We have to open() it to become
		 * live. This is necessary to allocate some ressources in the native
		 * part.
		 */
		try {
			sm_sequencer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * Next step is to tell the Sequencer which Sequence it has to play. In
		 * this case, we set it as the Sequence object created above.
		 */
		try {
			sm_sequencer.setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * Now, we set up the destinations the Sequence should be played on.
		 * Here, we try to use the default synthesizer. With some Java Sound
		 * implementations (Sun jdk1.3/1.4 and others derived from this
		 * codebase), the default sequencer and the default synthesizer are
		 * combined in one object. We test for this condition, and if it's true,
		 * nothing more has to be done. With other implementations (namely
		 * Tritonus), sequencers and synthesizers are always seperate objects.
		 * In this case, we have to set up a link between the two objects
		 * manually.
		 * 
		 * By the way, you should never rely on sequencers being synthesizers,
		 * too; this is a highly non- portable programming style. You should be
		 * able to rely on the other case working. Alas, it is only partly true
		 * for the Sun jdk1.3/1.4.
		 */
		if (!(sm_sequencer instanceof Synthesizer)) {
			/*
			 * We try to get the default synthesizer, open() it and chain it to
			 * the sequencer with a Transmitter-Receiver pair.
			 */
			try {
				sm_synthesizer = MidiSystem.getSynthesizer();
				sm_synthesizer.open();
				Receiver synthReceiver = sm_synthesizer.getReceiver();
				Transmitter seqTransmitter = sm_sequencer.getTransmitter();
				seqTransmitter.setReceiver(synthReceiver);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Now, we can start over.
		 */
		sm_sequencer.start();
	}

	public void saveTempMIDI() {
		JFileChooser fc = new JFileChooser("./midi files/");
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".mid") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return ".mid file";
			}
		});
		int returnVal = fc.showOpenDialog(MIDIReader.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = fc.getSelectedFile();
			File temp = new File("temp.mid");
			try {
				InputStream in = new FileInputStream(temp);
				OutputStream out = new FileOutputStream(saveFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int setImportFile() {
		JFileChooser fc = new JFileChooser("./midi files/");
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".mid") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return ".mid file";
			}
		});
		fc.setMultiSelectionEnabled(true);
		int returnVal = fc.showOpenDialog(MIDIReader.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			files = fc.getSelectedFiles();
			return files.length;
		} else {
			return 0;
		}
	}

	public void shutDown() {
		stopBootstrap();
		System.exit(0);
	}

	public void startBootstrap() {
		startDB();

		// Boot the JBoss Microcontainer with EJB3 settings, automatically
		// loads ejb3-interceptors-aop.xml and embedded-jboss-beans.xml
		EJB3StandaloneBootstrap.boot(null);

		// Deploy custom stateless beans (datasource, mostly)
		EJB3StandaloneBootstrap.deployXmlResource("META-INF/midireader-beans.xml");

		// Deploy all EJBs found on classpath (slow, scans all)
		// EJB3StandaloneBootstrap.scanClasspath();

		// Deploy all EJBs found on classpath (fast, scans only build directory)
		// This is a relative location, matching the substring end of one of
		// java.class.path locations!
		// Print out System.getProperty("java.class.path") to understand this...
		EJB3StandaloneBootstrap.scanClasspath("MIDIReader-1.0/build".replace("/", File.separator));
	}

	public void startDB() {
		hsqldbServer = new Server();
		hsqldbServer.setSilent(true);
		hsqldbServer.setDatabasePath(0, "./resources/dbmodel/mr");
		hsqldbServer.setDatabaseName(0, "mr");

		hsqldbServer.start();
	}

	public void stopBootstrap() {
		// Shutdown EJB container
		EJB3StandaloneBootstrap.shutdown();
		stopDB();
	}

	public void stopDB() {
		if (hsqldbServer != null) {
			hsqldbServer.stop();
			hsqldbServer = null;
		}
	}

	public void stopTempMIDI() {
		sm_sequencer.stop();
	}
}
