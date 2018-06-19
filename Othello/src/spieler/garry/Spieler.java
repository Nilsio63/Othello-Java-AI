package spieler.garry;

import spieler.Farbe;
import spieler.OthelloSpieler;
import spieler.Zug;
import spieler.ZugException;

public class Spieler extends Player implements OthelloSpieler {
    public Spieler() {
        this(6);
    }

    public Spieler(int maxSuchtiefe) {
        super(maxSuchtiefe);
    }

    @Override
    public Zug berechneZug(Zug vorherigerZug, long zeitWeiss, long zeitSchwarz)
            throws ZugException {
        try {
            Move move = calculateMove(vorherigerZug != null ? new Move(vorherigerZug.getZeile(), vorherigerZug.getSpalte()) : null, zeitWeiss, zeitSchwarz);

            return new Zug(move.getRow(), move.getColumn());
        }
        catch (MoveException ex) {
            throw new ZugException("Dies ist kein gültiger Zug!");
        }
    }

    @Override
    public void neuesSpiel(Farbe farbe, int zeitInSek) {
        newGame(farbe == Farbe.WEISS ? Color.WHITE : Color.BLACK, zeitInSek);
    }

    @Override
    public String meinName() {
        return getName();
    }
}
