package spieler.garry;

class Field {
    private int row;
    private int column;

    public Field(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Field(Move move) {
        this(move.getRow(), move.getColumn());
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
