package spieler.garry;

class MoveNode {
    private Move move;
    private MoveOptions children;
    private Board currentBoard;
    private int rating;

    public MoveNode(Move move, Board board) {
        this.move = move;
        this.currentBoard = board;
        this.children = null;
    }

    public Move getMove() {
        return move;
    }

    public MoveOptions getChildren() {
        return children;
    }

    public void setChildren(MoveOptions children) {
        this.children = children;

        if (this.children != null)
            this.currentBoard = null;
    }

    public Board getBoard() {
        return currentBoard;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
