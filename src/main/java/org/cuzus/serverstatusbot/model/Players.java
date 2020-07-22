package org.cuzus.serverstatusbot.model;

import java.util.Collection;
import java.util.HashMap;

import org.cuzus.serverstatusbot.model.Player;

public class Players {
  private static final long serialVersionUID = 1L;

  private HashMap<Integer, Player> backing;

  public Players() {
    backing = new HashMap<Integer, Player>();
  }

  public Players(int capacity) {
    backing = new HashMap<Integer, Player>(capacity);
  }

  public Player get(int index) {
    Player player = backing.get(index);

    if (player == null) {
      player = new Player(index);
      backing.put(index, player);
    }

    return player;
  }

  public Collection<Player> getPlayers() {
    return backing.values();
  }
}