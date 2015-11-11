/**
 * 
 */
package gdi.reader.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author GDI
 * 
 */
public abstract class ReportWriter {

	protected static PrintWriter out = null;

	public void closePrintWriter() {
		if (out != null) {
			out.close();
		}
		out = null;
	}

	public PrintWriter getPrintWriter() {
		return out;
	}

	public PrintWriter setPrintWriter(File file) {
		if (out == null) {
			try {
				FileWriter outFile;
				outFile = new FileWriter(file);
				out = new PrintWriter(outFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}
}
