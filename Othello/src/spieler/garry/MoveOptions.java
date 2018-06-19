package spieler.garry;

import java.util.ArrayList;

class MoveOptions extends ArrayList<MoveNode> {
    public MoveOptions(Color color)
    {
        this(new Board(), color);
    }

    public MoveOptions(Board board, Color color) {
        for (int row = 0; row < Board.ROWS; row++) {
            for (int column = 0; column < Board.COLUMNS; column++) {
                if (board.getField(row, column) == Color.EMPTY) {
                    ArrayList<Field> affectedFields = board.getAffectedFields(new Move(row, column), color);

                    if (!affectedFields.isEmpty()) {
                        Board b = new Board(board);

                        b.setFields(affectedFields, color);

                        add(new MoveNode(new Move(row, column), color, b));
                    }
                }
            }
        }

        if (isEmpty()) {
            add(new MoveNode(new Move(), color, new Board(board)));
        }
    }
}
