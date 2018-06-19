package spieler.garry;

import java.util.Optional;

class MoveNode {
    private Move move;
    private Color color;
    private MoveOptions children;
    private Board currentBoard;

    public MoveNode(Move move, Color color, Board board) {
        this.move = move;
        this.color = color;
        this.currentBoard = board;
        this.children = null;
    }

    public Move getMove() {
        return move;
    }

    public MoveOptions getChildren() {
        if (children == null)
            recalculate(1);

        return children;
    }

    public int getRating(Color player) {
        if (children == null) {
            return currentBoard.getRating(player);
        }
        else {
            Optional<Integer> best = Optional.empty();

            for (MoveNode node : children) {
                int rating = node.getRating(player);

                if (node.color == player) {
                    if (!best.isPresent()
                            || best.get() < rating) {
                        best = Optional.of(rating);
                    }
                }
                else {
                    if (!best.isPresent()
                            || best.get() > rating) {
                        best = Optional.of(rating);
                    }
                }
            }

            return best.get();
        }
    }

    public void recalculate(int depth) {
        if (depth <= 0)
            return;

        if (children == null) {
            children = new MoveOptions(currentBoard, color.getOpponent());
        }

        currentBoard = null;

        for (MoveNode node : children) {
            node.recalculate(depth - 1);
        }
    }

}
