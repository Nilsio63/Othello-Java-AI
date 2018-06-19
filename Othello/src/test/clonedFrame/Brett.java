package test.clonedFrame;

import spieler.Farbe;
import spieler.Zug;
import spieler.ZugException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Brett implements Cloneable {
    static int brettgroesse = 8;
    public Farbe[][] matrix;
    private static int[][] bm;

    static {
        bm = new int[brettgroesse][brettgroesse];
    }

    Brett() {
        this.matrix = new Farbe[brettgroesse][brettgroesse];
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
        for(zeile = 0; zeile < 4; ++zeile) {
            for(spalte = 4; spalte < 8; ++spalte) {
                bm[zeile][spalte] = bm[zeile][7 - spalte];
            }
        }

        for(zeile = 4; zeile < 8; ++zeile) {
            for(spalte = 0; spalte < 8; ++spalte) {
                bm[zeile][spalte] = bm[7 - zeile][spalte];
            }
        }

    }

    public void initialisiereBrett() {
        for(int zeile = 0; zeile < brettgroesse; ++zeile) {
            for(int spalte = 0; spalte < brettgroesse; ++spalte) {
                this.matrix[zeile][spalte] = Farbe.LEER;
            }
        }

        this.matrix[3][3] = Farbe.WEISS;
        this.matrix[3][4] = Farbe.SCHWARZ;
        this.matrix[4][3] = Farbe.SCHWARZ;
        this.matrix[4][4] = Farbe.WEISS;
    }

    public Brett clone() {
        Brett kopie = new Brett();

        for(int zeile = 0; zeile < brettgroesse; ++zeile) {
            for(int spalte = 0; spalte < brettgroesse; ++spalte) {
                kopie.matrix[zeile][spalte] = this.matrix[zeile][spalte];
            }
        }

        return kopie;
    }

    public void macheZug(Farbe farbe, Zug zug) throws ZugException {
        if (!zug.getPassen()) {
            List<Zug> drehsteine = this.berechneDrehsteine(farbe, zug, true);
            if (drehsteine.isEmpty()) {
                throw new ZugException("Zug(" + zug.getZeile() + "," + zug.getSpalte() + ") nicht möglich!");
            }

            this.matrix[zug.getZeile()][zug.getSpalte()] = farbe;

            Zug drehstein;
            for(Iterator var5 = drehsteine.iterator(); var5.hasNext(); this.matrix[drehstein.getZeile()][drehstein.getSpalte()] = farbe) {
                drehstein = (Zug)var5.next();
            }
        }

    }

    private List<Zug> berechneDrehsteine(Farbe farbe, Zug zug, boolean alleBerechnen) {
        ArrayList<Zug> drehsteine = new ArrayList();
        Farbe gfarbe = Farbe.WEISS;
        if (farbe == Farbe.WEISS) {
            gfarbe = Farbe.SCHWARZ;
        }

        for(int dz = -1; dz < 2; ++dz) {
            for(int ds = -1; ds < 2; ++ds) {
                if (dz != 0 || ds != 0) {
                    Zug naechstesFeld = new Zug(zug.getZeile() + dz, zug.getSpalte() + ds);
                    if (this.zugAufBrett(naechstesFeld)) {
                        ArrayList<Zug> moeglicheDrehsteine = new ArrayList();
                        if (this.matrix[naechstesFeld.getZeile()][naechstesFeld.getSpalte()] == gfarbe) {
                            moeglicheDrehsteine.add(naechstesFeld);

                            for(naechstesFeld = new Zug(naechstesFeld.getZeile() + dz, naechstesFeld.getSpalte() + ds); this.zugAufBrett(naechstesFeld); naechstesFeld = new Zug(naechstesFeld.getZeile() + dz, naechstesFeld.getSpalte() + ds)) {
                                if (this.matrix[naechstesFeld.getZeile()][naechstesFeld.getSpalte()] != gfarbe) {
                                    if (this.matrix[naechstesFeld.getZeile()][naechstesFeld.getSpalte()] == farbe) {
                                        drehsteine.addAll(moeglicheDrehsteine);
                                        if (!alleBerechnen) {
                                            return drehsteine;
                                        }
                                    }
                                    break;
                                }

                                moeglicheDrehsteine.add(naechstesFeld);
                            }
                        }
                    }
                }
            }
        }

        return drehsteine;
    }

    public List<Zug> moeglicheZuegeBerechnen(Farbe spieler) {
        ArrayList<Zug> moeglicheZuege = new ArrayList();

        for(int zeile = 0; zeile < brettgroesse; ++zeile) {
            for(int spalte = 0; spalte < brettgroesse; ++spalte) {
                if (this.matrix[zeile][spalte] == Farbe.LEER) {
                    Zug zug = new Zug(zeile, spalte);
                    if (this.berechneDrehsteine(spieler, zug, false).size() > 0) {
                        moeglicheZuege.add(zug);
                    }
                }
            }
        }

        if (moeglicheZuege.isEmpty()) {
            moeglicheZuege.add(new Zug(-1, -1));
        }

        return moeglicheZuege;
    }

    public int bewerte() {
        int summe = 0;

        for(int zeile = 0; zeile < brettgroesse; ++zeile) {
            for(int spalte = 0; spalte < brettgroesse; ++spalte) {
                Farbe feld = this.matrix[zeile][spalte];
                if (feld != Farbe.LEER) {
                    if (feld == Farbe.SCHWARZ) {
                        summe += bm[zeile][spalte];
                    } else {
                        summe -= bm[zeile][spalte];
                    }
                }
            }
        }

        return summe;
    }

    public boolean zugAufBrett(Zug zug) {
        return zug.getSpalte() >= 0 && zug.getSpalte() < brettgroesse && zug.getZeile() >= 0 && zug.getZeile() < brettgroesse;
    }

    public void druckeMatrix() {
        System.out.println("INTERN");
        System.out.println("  0 1 2 3 4 5 6 7");
        System.out.println("=================");

        for(int zeile = 0; zeile < brettgroesse; ++zeile) {
            System.out.print(zeile + ":");

            for(int spalte = 0; spalte < brettgroesse; ++spalte) {
                if (this.matrix[zeile][spalte] == Farbe.WEISS) {
                    System.out.print("O ");
                } else if (this.matrix[zeile][spalte] == Farbe.SCHWARZ) {
                    System.out.print("X ");
                } else {
                    System.out.print("  ");
                }
            }

            System.out.println();
        }

        System.out.println("ENDE");
    }
}
