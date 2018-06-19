package spieler.garry;

import java.util.ArrayList;
import java.util.Optional;

class Board {
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;

    private static final int[][] rateMatrix = new int[][] {
            {50,-1,5,2,2,5,-1,50},
            {-1,-10,1,1,1,1,-10,-1},
            {5,1,1,10,10,1,1,5},
            {2,1,10,0,0,10,1,2},
            {2,1,10,0,0,10,1,2},
            {5,1,1,10,10,1,1,5},
            {-1,-10,1,1,1,1,-10,-1},
            {50,-1,5,2,2,5,-1,50}
    };

    private final Color[][] board = new Color[ROWS][COLUMNS];

    private Optional<Integer> calculatedRating;

    public Board() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                this.board[row][column] = Color.EMPTY;
            }
        }

        this.board[3][3] = Color.WHITE;
        this.board[3][4] = Color.BLACK;
        this.board[4][3] = Color.BLACK;
        this.board[4][4] = Color.WHITE;

        this.calculatedRating = Optional.empty();
    }

    public Board(Board toCopy) {
        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(toCopy.board[row], 0, this.board[row], 0, COLUMNS);
        }

        this.calculatedRating = toCopy.calculatedRating;
    }

    public Color getField(int row, int column) {
        return board[row][column];
    }

    public void setField(Field field, Color color) {
        board[field.getRow()][field.getColumn()] = color;

        calculatedRating = Optional.empty();
    }

    public void setFields(ArrayList<Field> fields, Color color) {
        for (Field f : fields)
            setField(f, color);
    }

    public int getRating(Color player) {
        if (!calculatedRating.isPresent()) {
            int rating = 0;

            for (int row = 0; row < ROWS; row++) {
                for (int column = 0; column < COLUMNS; column++) {
                    Color fieldColor = board[row][column];

                    if (fieldColor != Color.EMPTY) {
                        int t = rateMatrix[row][column];

                        if (fieldColor != player) {
                            t = -t;
                        }

                        rating += t;
                    }
                }
            }

            calculatedRating = Optional.of(rating);
        }

        return calculatedRating.get();
    }

    public ArrayList<Field> getAffectedFields(Move move, Color color) {
        ArrayList<Field> affectedFields = new ArrayList<>();

        if (!move.getIsPassing()) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr != 0 || dc != 0) {
                        int row = move.getRow() + dr;
                        int column = move.getColumn() + dc;

                        if (row >= 0 && row < ROWS
                                && column >= 0 && column < COLUMNS) {
                            boolean foundOpponent = false;
                            boolean endsOnSelf = false;
                            int i;

                            for (i = 0; ; i++) {
                                int tr = row + i * dr;
                                int tc = column + i * dc;

                                if (tr >= 0 && tr < ROWS
                                        && tc >= 0 && tc < COLUMNS) {
                                    Color c = board[tr][tc];

                                    if (c == Color.EMPTY)
                                        break;
                                    else if (c == color) {
                                        endsOnSelf = true;
                                        break;
                                    }
                                    else {
                                        foundOpponent = true;
                                    }
                                }
                                else {
                                    break;
                                }
                            }

                            if (foundOpponent && endsOnSelf) {
                                for (i--; i >= 0; i--) {
                                    affectedFields.add(new Field(row + i * dr, column + i * dc));
                                }
                            }
                        }
                    }
                }
            }

            if (!affectedFields.isEmpty())
                affectedFields.add(new Field(move));
        }

        return affectedFields;
    }

    public void print() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                char toPrint;

                switch (board[row][column]) {
                    default:
                        toPrint = '-';
                        break;
                    case BLACK:
                        toPrint = '1';
                        break;
                    case WHITE:
                        toPrint = '0';
                        break;
                }

                System.out.print(toPrint + " ");
            }
            System.out.println();
        }
    }

}
