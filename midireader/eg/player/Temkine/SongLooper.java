package eg.player.Temkine;

/*
 * Created on Oct 25, 2005
 */

/**
 * @author Mikhail Temkine This is a class for additional loopers
 */
public class SongLooper {
	public static class IncrementalLooper extends SongLooper {
		public IncrementalLooper(MIDIPlayer m) {
			super(m);
		}

		@Override
		public void nextSong() {
			int i = m.listbox.getSelectedIndex();
			m.playSong(i + 1);
		}
	}

	/**
	 * This particular one is no looper: the player stops after song ends
	 */
	public static class NormalLooper extends SongLooper {
		public NormalLooper(MIDIPlayer m) {
			super(m);
		}

		@Override
		public void nextSong() {
		}
	}

	public static class RandomLooper extends SongLooper {
		public RandomLooper(MIDIPlayer m) {
			super(m);
		}

		@Override
		public void nextSong() {
			int i = m.listbox.getSelectedIndex();
			int j = i;
			if (m.playlist.size() > 1) {
				while (i == j) {
					j = (int) (Math.random() * m.playlist.size());
				}
			}
			m.playSong(j);
		}
	}

	public static class RepeatLooper extends SongLooper {
		public RepeatLooper(MIDIPlayer m) {
			super(m);
		}

		@Override
		public void nextSong() {
			m.p.play();
		}
	}

	MIDIPlayer m;

	public SongLooper(MIDIPlayer m) {
		this.m = m;
	}

	public void nextSong() {
	}
}