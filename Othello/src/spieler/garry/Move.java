package spieler.garry;

class Move {
    private int row;
    private int column;

    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Move() {
        this(-1, -1);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean getIsPassing() {
        return row == -1
                && column == -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Move) {
            Move m = (Move)obj;

            return this.row == m.row
                    && this.column == m.column;
        }

        return false;
    }

    @Override
    public String toString() {
        return getIsPassing() ? "Passing!" : new String[] {"A","B","C","D","E","F","G","H"}[column] + (row + 1);
    }
}
