package gdi.reader;

import java.io.IOException;

import org.hsqldb.Server;

public class StartDB {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		StartDB sdb = new StartDB();
		sdb.startDB();
		while (System.in.read() != 0) {
			sdb.stopDB();
			System.exit(0);
		}
	}

	private Server hsqldbServer = null;

	public void startDB() {
		hsqldbServer = new Server();
		hsqldbServer.setDatabasePath(0, "./resources/dbmodel/mr");
		hsqldbServer.setDatabaseName(0, "mr");

		hsqldbServer.start();
	}

	public void stopDB() {
		if (hsqldbServer != null) {
			hsqldbServer.stop();
			hsqldbServer = null;
		}
	}

}
