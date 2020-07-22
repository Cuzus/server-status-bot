package org.cuzus.serverstatusbot.model;

public class Player {
	public String name;
	public int kills;
	public float secondsConnected;
	private int index;
	public int ping;
	public int team;
	public int score;
	public int deaths;

	public String mesh;
	public String skin;
	public String face;
	public String ngStats;

	Player(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public int getKills() {
		return kills;
	}

	public float getSecondsConnected() {
		return secondsConnected;
	}

	public int getIndex() {
		return index;
	}

	public int getPing() {
		return ping;
	}

	public int getTeam() {
		return team;
	}

	public int getScore() {
		return score;
	}

	public int getDeaths() {
		return deaths;
	}

	public String getMesh() {
		return mesh;
	}

	public String getSkin() {
		return skin;
	}

	public String getFace() {
		return face;
	}

	public String getNgStats() {
		return ngStats;
	}
}