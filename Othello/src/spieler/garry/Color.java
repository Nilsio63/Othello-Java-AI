package spieler.garry;

enum Color  {
    BLACK,
    WHITE,
    EMPTY;

    Color getOpponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
