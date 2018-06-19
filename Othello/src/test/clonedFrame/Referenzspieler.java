package test.clonedFrame;

import spieler.Farbe;
import spieler.OthelloSpieler;
import spieler.Zug;

import java.util.Vector;

public class Referenzspieler implements Cloneable, OthelloSpieler {
    private static final int ANZ_ZEILEN = 8;
    private static final int ANZ_SPALTEN = 8;
    private static int[][] bm = new int[ANZ_ZEILEN][ANZ_SPALTEN];
    private int[][] brett;
    private int computerspieler;
    private int anzahlPassen;
    private int suchtiefe;

    private void fuelleBewertungsmatrix() {
        bm[0][0] = 50;
        bm[0][1] = -1;
        bm[0][2] = 5;
        bm[0][3] = 2;
        bm[1][0] = -1;
        bm[1][1] = -10;
        bm[1][2] = 1;
        bm[1][3] = 1;
        bm[2][0] = 5;
        bm[2][1] = 1;
        bm[2][2] = 1;
        bm[2][3] = 1;
        bm[3][0] = 2;
        bm[3][1] = 1;
        bm[3][2] = 1;
        bm[3][3] = 0;

        int zeile;
        int spalte;
        for(zeile = 0; zeile < ANZ_ZEILEN / 2; ++zeile) {
            for(spalte = ANZ_SPALTEN / 2; spalte < ANZ_SPALTEN; ++spalte) {
                bm[zeile][spalte] = bm[zeile][ANZ_SPALTEN - spalte + 1];
            }
        }

        for(zeile = ANZ_ZEILEN / 2; zeile < ANZ_ZEILEN; ++zeile) {
            for(spalte = 0; spalte < ANZ_SPALTEN; ++spalte) {
                bm[zeile][spalte] = bm[ANZ_ZEILEN - zeile + 1][spalte];
            }
        }

    }

    public Referenzspieler() {
        this(6);
    }

    public Referenzspieler(int suchtiefe) {
        this.anzahlPassen = 0;
        fuelleBewertungsmatrix();
        this.brett = new int[ANZ_ZEILEN][ANZ_SPALTEN];
        this.suchtiefe = suchtiefe;
    }

    @Override
    public Referenzspieler clone() {
        Referenzspieler kopie = new Referenzspieler(this.suchtiefe);
        kopie.computerspieler = this.computerspieler;

        for(int zeile = 0; zeile < this.brett.length; ++zeile) {
            System.arraycopy(this.brett[zeile], 0, kopie.brett[zeile], 0, this.brett[0].length);
        }

        return kopie;
    }

    private void initialisiereBrett() {
        for(int zeile = 0; zeile < this.brett.length; ++zeile) {
            for(int spalte = 0; spalte < this.brett[0].length; ++spalte) {
                this.brett[zeile][spalte] = 0;
            }
        }

        this.brett[3][3] = 1;
        this.brett[3][4] = -1;
        this.brett[4][3] = -1;
        this.brett[4][4] = 1;
    }

    private Vector<ZugPriemer> berechneMoeglicheZuege(int spielerAmZug) {
        Vector<ZugPriemer> moeglicheZuege = new Vector<>();
        if (this.brett[0][0] == 0 && (this.legalerZug(0, 0, 0, 1, spielerAmZug) || this.legalerZug(0, 0, 1, 0, spielerAmZug) || this.legalerZug(0, 0, 1, 1, spielerAmZug))) {
            moeglicheZuege.add(new ZugPriemer(0, 0));
        }

        if (this.brett[0][7] == 0 && (this.legalerZug(0, 7, 0, -1, spielerAmZug) || this.legalerZug(0, 7, 1, 0, spielerAmZug) || this.legalerZug(0, 7, 1, -1, spielerAmZug))) {
            moeglicheZuege.add(new ZugPriemer(0, 7));
        }

        if (this.brett[7][0] == 0 && (this.legalerZug(7, 0, 0, 1, spielerAmZug) || this.legalerZug(7, 0, -1, 0, spielerAmZug) || this.legalerZug(7, 0, -1, 1, spielerAmZug))) {
            moeglicheZuege.add(new ZugPriemer(7, 0));
        }

        if (this.brett[7][7] == 0 && (this.legalerZug(7, 7, 0, -1, spielerAmZug) || this.legalerZug(7, 7, -1, 0, spielerAmZug) || this.legalerZug(7, 7, -1, -1, spielerAmZug))) {
            moeglicheZuege.add(new ZugPriemer(7, 7));
        }

        int zeile;
        for(zeile = 1; zeile < 7; ++zeile) {
            if (this.brett[0][zeile] == 0 && (this.legalerZug(0, zeile, 0, 1, spielerAmZug) || this.legalerZug(0, zeile, 1, 1, spielerAmZug) || this.legalerZug(0, zeile, 1, 0, spielerAmZug) || this.legalerZug(0, zeile, 1, -1, spielerAmZug) || this.legalerZug(0, zeile, 0, -1, spielerAmZug))) {
                moeglicheZuege.add(new ZugPriemer(0, zeile));
            }

            if (this.brett[7][zeile] == 0 && (this.legalerZug(7, zeile, 0, 1, spielerAmZug) || this.legalerZug(7, zeile, 0, -1, spielerAmZug) || this.legalerZug(7, zeile, -1, -1, spielerAmZug) || this.legalerZug(7, zeile, -1, 0, spielerAmZug) || this.legalerZug(7, zeile, -1, 1, spielerAmZug))) {
                moeglicheZuege.add(new ZugPriemer(7, zeile));
            }
        }

        for(zeile = 1; zeile < 7; ++zeile) {
            if (this.brett[zeile][0] == 0 && (this.legalerZug(zeile, 0, -1, 0, spielerAmZug) || this.legalerZug(zeile, 0, -1, 1, spielerAmZug) || this.legalerZug(zeile, 0, 0, 1, spielerAmZug) || this.legalerZug(zeile, 0, 1, 1, spielerAmZug) || this.legalerZug(zeile, 0, 1, 0, spielerAmZug))) {
                moeglicheZuege.add(new ZugPriemer(zeile, 0));
            }

            if (this.brett[zeile][7] == 0 && (this.legalerZug(zeile, 7, -1, 0, spielerAmZug) || this.legalerZug(zeile, 7, 1, 0, spielerAmZug) || this.legalerZug(zeile, 7, 1, -1, spielerAmZug) || this.legalerZug(zeile, 7, 0, -1, spielerAmZug) || this.legalerZug(zeile, 7, -1, -1, spielerAmZug))) {
                moeglicheZuege.add(new ZugPriemer(zeile, 7));
            }
        }

        for(zeile = 1; zeile < 7; ++zeile) {
            for(int spalte = 1; spalte < 7; ++spalte) {
                if (this.brett[zeile][spalte] == 0 && (this.legalerZug(zeile, spalte, -1, -1, spielerAmZug) || this.legalerZug(zeile, spalte, -1, 0, spielerAmZug) || this.legalerZug(zeile, spalte, -1, 1, spielerAmZug) || this.legalerZug(zeile, spalte, 0, -1, spielerAmZug) || this.legalerZug(zeile, spalte, 0, 1, spielerAmZug) || this.legalerZug(zeile, spalte, 1, -1, spielerAmZug) || this.legalerZug(zeile, spalte, 1, 0, spielerAmZug) || this.legalerZug(zeile, spalte, 1, 1, spielerAmZug))) {
                    moeglicheZuege.add(new ZugPriemer(zeile, spalte));
                }
            }
        }

        return moeglicheZuege;
    }

    private int bewertePosition() {
        int bewertung = 0;

        for(int zeile = 0; zeile < 8; ++zeile) {
            for(int spalte = 0; spalte < 8; ++spalte) {
                bewertung += this.brett[zeile][spalte] * this.computerspieler * bm[zeile][spalte];
            }
        }

        return bewertung;
    }

    private void ziehe(int spieler, ZugPriemer zug) {
        Vector<Integer> drehsteine = new Vector<>();

        int i;
        int drehpos;
        for(i = -1; i < 2; ++i) {
            for(drehpos = -1; drehpos < 2; ++drehpos) {
                if (i != 0 || drehpos != 0) {
                    Vector<Integer> neueSteine = this.berechneDrehsteine(zug.zeile, zug.spalte, i, drehpos, spieler);
                    if (neueSteine != null) {
                        drehsteine.addAll(neueSteine);
                    }
                }
            }
        }

        if (!drehsteine.isEmpty()) {
            this.brett[zug.zeile][zug.spalte] = spieler;

            for(i = 0; i < drehsteine.size(); ++i) {
                drehpos = drehsteine.get(i);
                this.brett[drehpos / 8][drehpos % 8] = spieler;
            }
        }

    }

    private ZugPriemer findeBestenZug(int tiefe, int spieler, int alpha, int beta) {
        Vector<ZugPriemer> moeglicheZuege = this.berechneMoeglicheZuege(spieler);
        int anzahlMoeglicheZuege = moeglicheZuege.size();
        if (anzahlMoeglicheZuege <= 0) {
            ZugPriemer ergebnis = new ZugPriemer(-1, -1);
            if (tiefe == 1) {
                ergebnis.bewertung = this.bewertePosition();
            } else {
                ergebnis.bewertung = this.findeBestenZug(tiefe - 1, spieler * -1, alpha, beta).bewertung;
            }

            return ergebnis;
        } else {
            int maxpos = -1;
            int minpos = -1;

            for(int i = 0; i < anzahlMoeglicheZuege; ++i) {
                Referenzspieler brettKopie = this.clone();
                brettKopie.ziehe(spieler, moeglicheZuege.get(i));
                if (tiefe == 1) {
                    moeglicheZuege.get(i).bewertung = brettKopie.bewertePosition();
                } else {
                    ZugPriemer besterZug = brettKopie.findeBestenZug(tiefe - 1, spieler * -1, alpha, beta);
                    moeglicheZuege.get(i).bewertung = besterZug.bewertung;
                    if (spieler == this.computerspieler && besterZug.bewertung > alpha) {
                        alpha = besterZug.bewertung;
                    }

                    if (spieler != this.computerspieler && besterZug.bewertung < beta) {
                        beta = besterZug.bewertung;
                    }
                }

                if (i == 0) {
                    maxpos = 0;
                    minpos = 0;
                } else {
                    int bewertung = moeglicheZuege.get(i).bewertung;
                    if (bewertung > moeglicheZuege.get(maxpos).bewertung) {
                        maxpos = i;
                    } else if (bewertung < moeglicheZuege.get(minpos).bewertung) {
                        minpos = i;
                    }
                }

                if (alpha >= beta) {
                    break;
                }
            }

            return spieler == this.computerspieler ? moeglicheZuege.get(maxpos) : moeglicheZuege.get(minpos);
        }
    }

    private boolean legalerZug(int zeile, int spalte, int dz, int ds, int spieler) {
        return this.berechneDrehsteine(zeile, spalte, dz, ds, spieler) != null;
    }

    private Vector<Integer> berechneDrehsteine(int zeile, int spalte, int dz, int ds, int spieler) {
        int gegenspieler = spieler * -1;
        int neueZeile = zeile + dz;
        int neueSpalte = spalte + ds;
        if (neueZeile >= 0 && neueZeile < 8 && neueSpalte >= 0 && neueSpalte < 8) {
            if (this.brett[neueZeile][neueSpalte] != gegenspieler) {
                return null;
            }

            Vector<Integer> drehsteine = new Vector<>();
            drehsteine.add(neueZeile * 8 + neueSpalte);
            neueZeile += dz;

            for(neueSpalte += ds; neueZeile < 8 && neueZeile >= 0 && neueSpalte < 8 && neueSpalte >= 0; neueSpalte += ds) {
                if (this.brett[neueZeile][neueSpalte] == spieler) {
                    return drehsteine;
                }

                if (this.brett[neueZeile][neueSpalte] == 0) {
                    return null;
                }

                drehsteine.add(neueZeile * 8 + neueSpalte);
                neueZeile += dz;
            }
        }

        return null;
    }

    private ZugPriemer computerzug(int suchtiefe) {
        ZugPriemer computerzug = this.findeBestenZug(suchtiefe, this.computerspieler, -1000, 1000);
        if (computerzug == null) {
            return null;
        } else if (computerzug.zeile == -1) {
            ++this.anzahlPassen;
            return computerzug;
        } else {
            this.anzahlPassen = 0;
            this.ziehe(this.computerspieler, computerzug);
            return computerzug;
        }
    }

    private void spielerzug(int zeile, int spalte) {
        Vector<ZugPriemer> moeglicheZuege = this.berechneMoeglicheZuege(-1 * this.computerspieler);
        ZugPriemer spielerzug = new ZugPriemer(zeile, spalte);
        if (moeglicheZuege.contains(spielerzug)) {
            if (spielerzug.zeile == -1) {
                ++this.anzahlPassen;
            } else {
                this.anzahlPassen = 0;
            }

            this.ziehe(-this.computerspieler, spielerzug);
        }
    }

    public Zug berechneZug(Zug vorherigerZug, long zeitWeiss, long zeitSchwarz) {
        if (vorherigerZug != null) {
            this.spielerzug(vorherigerZug.getZeile(), vorherigerZug.getSpalte());
        }

        ZugPriemer meinZug = this.computerzug(this.suchtiefe);
        return new Zug(meinZug.zeile, meinZug.spalte);
    }

    public void neuesSpiel(Farbe meineFarbe, int bedenkzeitInSekunden) {
        this.computerspieler = meineFarbe == Farbe.WEISS ? 1 : -1;

        this.initialisiereBrett();
    }

    public String meinName() {
        return "Referenzspieler(" + this.suchtiefe + ")";
    }
}
