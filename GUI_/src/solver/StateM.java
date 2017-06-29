package solver;

import search.Position;

public class StateM {

	private Position player;
	private char[][] leveldata;

	public StateM(Position player, char[][] leveldata) {
		this.player = player;
		this.leveldata = leveldata;
	}

	public char[][] getLeveldata() {
		return leveldata;
	}

	public void setLeveldata(char[][] leveldata) {
		this.leveldata = leveldata;
	}

	public Position getPlayer() {
		return player;
	}

	public void setPlayer(Position player) {
		this.player = player;
	}

	@Override
	public String toString() {

		String str = "";

		for (int i = 0; i < this.leveldata.length; i++)
			for (int j = 0; j < this.leveldata[i].length; j++) {
				if (j == this.leveldata[i].length - 1) {
					str += "\n";
				} else
					str += this.leveldata[i][j];
			}

		return str;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		StateM state = (StateM) obj;

		for (int i = 0; i < this.leveldata.length; i++)
			for (int j = 0; j < this.leveldata[i].length; j++) {
				if (this.leveldata[i][j] != state.leveldata[i][j]) {
					return false;

				}
			}
		return true;
	}
}
