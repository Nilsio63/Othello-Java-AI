package spieler.garry;

import java.util.ArrayList;

class MoveOptions extends ArrayList<MoveNode> {
    private Color color;

    public MoveOptions(Color color)
    {
        this(new Board(), color);
    }

    public MoveOptions(Board board, Color color) {
        this.color = color;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int column = 0; column < Board.COLUMNS; column++) {
                if (board.getField(row, column) == Color.EMPTY) {
                    ArrayList<Field> affectedFields = board.getAffectedFields(new Move(row, column), color);

                    if (!affectedFields.isEmpty()) {
                        Board b = new Board(board);

                        b.setFields(affectedFields, color);

                        add(new MoveNode(new Move(row, column), b));
                    }
                }
            }
        }

        if (isEmpty()) {
            add(new MoveNode(new Move(), new Board(board)));
        }
    }

    public MoveOptions takeMove(Move move) throws MoveException {
        for (MoveNode node : this) {
            if (node.getMove().equals(move)) {
                MoveOptions next = node.getChildren();

                if (next == null)
                    next = new MoveOptions(node.getBoard(), color.getOpponent());

                return next;
            }
        }

        throw new MoveException();
    }

    public MoveNode getBest(Color player) {
        if (this.color == player) {
            return getMax(player);
        }
        else
            return getMin(player);
    }

    private MoveNode getMax(Color player) {
        MoveNode result = null;

        for (MoveNode node : this) {
            if (node.getChildren() != null) {
                node.setRating(node.getChildren().getMin(player).getRating());
            }
            else {
                node.setRating(node.getBoard().getRating(player));
            }

            if (result == null
                    || result.getRating() < node.getRating()) {
                result = node;
            }
        }

        return result;
    }

    private MoveNode getMin(Color player) {
        MoveNode result = null;

        for (MoveNode node : this) {
            if (node.getChildren() != null) {
                node.setRating(node.getChildren().getMax(player).getRating());
            }
            else {
                node.setRating(node.getBoard().getRating(player));
            }

            if (result == null
                    || result.getRating() > node.getRating()) {
                result = node;
            }
        }

        return result;
    }

    public void recalculate(int depth) {
        if (depth <= 0)
            return;

        for (MoveNode node : this) {
            MoveOptions nextOptions = new MoveOptions(node.getBoard(), color.getOpponent());
            nextOptions.recalculate(depth - 1);
            node.setChildren(nextOptions);
        }
    }
}
