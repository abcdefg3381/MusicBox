package gdi.reader.logic;

import gdi.reader.MIDIReader;
import gdi.reader.db.entity.AbstractMusicNote;
import gdi.reader.db.entity.Degree;
import gdi.reader.db.entity.Edge;
import gdi.reader.db.handler.AbstractMusicNoteHandler;
import gdi.reader.db.handler.EdgeHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

public class DDCCCounter extends ReportWriter {

	public static boolean countDataBaseToFile(File file) {
		DDCCCounter ddccc = new DDCCCounter(file);
		return ddccc.count();
	}

	private int[][] cdlls;

	private int count = 0;

	private int[] counts;

	private int[] degreeDistributions;

	private List<Degree> degreeList = new ArrayList<Degree>();

	private int[][] graphTable;

	private double l = 0;

	public DDCCCounter(File file) {
		super();
		setPrintWriter(file);
	}

	public boolean count() {

		int[] temp = NMZCounter.getStatistics();
		getPrintWriter().println("n = " + temp[0]);
		getPrintWriter().println("m = " + temp[1]);
		getPrintWriter().println("z = " + (float) temp[1] / temp[0]);
		try {
			// get abstract note list and form node list
			createNodeList();

			// insert degree node into node's degree list
			insertDegreeIntoNodeList();

			// mean distance shortest path
			getPrintWriter().println("mean distance: " + countMeanDistance());

			// System.out.println("finished dll, calculate cc");

			// count clustering coefficient
			float[] temp2 = countClusteringCoefficient();
			getPrintWriter().println("cc1 = " + temp2[0]);
			getPrintWriter().println("cc2 = " + temp2[1]);

			// count degree list
			countDegreeList();

			// count degree
			countDegree();

			// print degree list
			for (int count2 : counts) {
				getPrintWriter().println(count2);
			}
			closePrintWriter();
			return true;
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	private float[] countClusteringCoefficient() {

		Object[] dlList = degreeList.toArray();
		Degree dli;
		Degree dlj;
		Object[] degreei;

		int[][] tt = new int[dlList.length][2];

		// foreach DLi in DLL
		for (int i = 0; i < dlList.length; i++) {
			dli = (Degree) dlList[i];
			degreei = dli.getDegreeList().toArray();

			// foreach AMNj in DLi
			for (int j = 0; j < degreei.length; j++) {
				AbstractMusicNote amnj = (AbstractMusicNote) degreei[j];

				// foreach AMNk in DLi where k>j
				for (int k = j + 1; k < degreei.length; k++) {
					AbstractMusicNote amnk = (AbstractMusicNote) degreei[k];

					// if AMNk is in DLj triangle++ triple ++
					dlj = (Degree) dlList[amnj.getId() - 1];
					for (AbstractMusicNote amns : dlj.getDegreeList()) {
						if (amns.getId() == amnk.getId()) {
							tt[i][0]++;
							break;
						}
					}
					// else triple ++
					tt[i][1]++;

				}
			}
		}
		float c1 = 0, c2 = 0, t1 = 0, t2 = 0;
		for (int[] element : tt) {
			int triangle = element[0];
			int triple = element[1];
			if (triple != 0) {
				c1 += (float) triangle / triple;
			}
			t1 += triangle;
			t2 += triple;
		}
		c2 = t1 / t2;
		return new float[] { c1 / tt.length, c2 };
	}

	private void countDegree() {
		degreeDistributions = new int[cdlls.length];
		for (int k = 0; k < cdlls.length; k++) {
			degreeDistributions[k] = cdlls[k][1];
		}
		int biggest = 0;
		for (int i : degreeDistributions) {
			if (i > biggest) {
				biggest = i;
			}
		}
		counts = new int[biggest + 1];
		for (int degreeDistribution : degreeDistributions) {
			int k = degreeDistribution;
			counts[k]++;
		}
	}

	private void countDegreeList() {
		cdlls = new int[degreeList.size()][2];
		for (int i = 0; i < cdlls.length; i++) {
			int[] js = cdlls[i];
			js[0] = degreeList.get(i).getNode().getId();
			js[1] = degreeList.get(i).getDegreeList().size();
		}
	}

	private double countMeanDistance() {
		graphTable = new int[degreeList.size()][degreeList.size()];
		{
			for (int i = 0; i < graphTable.length; i++) {
				for (int j = 0; j < graphTable[i].length; j++) {
					if (i == j) {
						graphTable[i][j] = 0;
					} else {
						graphTable[i][j] = 512;
					}
				}
			}
		}
		for (Degree degree : degreeList) {
			for (AbstractMusicNote amn : degree.getDegreeList()) {
				graphTable[degree.getNode().getId() - 1][amn.getId() - 1] = 1;
			}
		}
		int i, j, k;

		/* Run Floyd's Algorithm */
		for (i = 0; i < graphTable.length; i++) {
			for (j = 0; j < graphTable.length; j++) {
				int[] subTable = graphTable[i];
				if (i != j) // skip over the current row
				{
					for (k = 0; k < subTable.length; k++) {
						if (k != i) // skip over the current column of
						// iteration
						{
							graphTable[j][k] = min(graphTable[j][k], graphTable[j][i] + graphTable[i][k]);
						}
					}
				}

			}
		}
		for (int[] element : graphTable) {
			for (int j1 = 0; j1 < element.length; j1++) {
				if (element[j1] != 0 && element[j1] != 512) {
					count++;
					l += element[j1];
				}
			}
		}
		return l / count;
	}

	private void createNodeList() {
		try {
			AbstractMusicNoteHandler abstractNoteHandler =
					(AbstractMusicNoteHandler) MIDIReader.getInstance().getInitialContext()
							.lookup("AbstractMusicNoteHandlerBean/local");

			for (AbstractMusicNote note : abstractNoteHandler.getAllAbstractMusicNotes()) {
				degreeList.add(new Degree(note));
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private void insertDegreeIntoNodeList() throws NamingException {
		try {
			EdgeHandler edgeHandler =
					(EdgeHandler) MIDIReader.getInstance().getInitialContext().lookup("EdgeHandlerBean/local");
			for (Edge edge : edgeHandler.getAllEdges()) {
				makeLink(edge.getPreAMN(), edge.getSucAMN());
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private void makeLink(AbstractMusicNote note, AbstractMusicNote note2) {
		// iterate the degreelistlist
		for (Degree degree : degreeList) {

			// note already exist in dl list
			if (degree.getNode().getId() == note.getId()) {

				// if found, no need to add once more
				for (AbstractMusicNote amn : degree.getDegreeList()) {
					if (amn.getId() == note2.getId()) {
						return;
					}
				}
				// otherwise add previous one
				degree.getDegreeList().add(note2);
				return;
			}
		}

		// note not yet in dl list, then add it
		degreeList.add(new Degree(note));
	}

	private int min(int i, int j) {
		return i > j ? j : i;
	}
}
