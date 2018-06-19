package test;

import java.util.ArrayList;
import java.util.List;

public class TestGame {
    public static void main(String[] args) {
        List<spieler.OthelloSpieler> players = new ArrayList<>();
        players.add(new spieler.garry.Spieler(3));
        players.add(new test.clonedFrame.Referenzspieler(3));
        new rahmen.OthelloArena(150, players, false);
    }

}
