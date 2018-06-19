package spieler.garry;

import java.util.Optional;

class Player {
    private Color player;
    private long timeInMs;
    private int maxSearchDepth;

    private MoveOptions currentOptions;

    public Player(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
    }

    public Move calculateMove(Move previousMove, long timeWhite, long timeBlack)
            throws MoveException {
        if (currentOptions == null)
            currentOptions = new MoveOptions(previousMove != null ? player.getOpponent() : player);

        if (previousMove != null)
            takeMove(previousMove);

        Move best = getBest();
        takeMove(best);
        return best;
    }

    public void newGame(Color playerColor, int timeInSec) {
        this.player = playerColor;
        this.timeInMs = 1000L * timeInSec;

        this.currentOptions = null;
    }

    public String getName() {
        return "Garry";
    }

    private void takeMove(Move move)
            throws MoveException {
        for (MoveNode node : currentOptions) {
            if (node.getMove().equals(move)) {
                currentOptions = node.getChildren();

                return;
            }
        }

        throw new MoveException();
    }

    private Move getBest() {
        recalculate(player);

        MoveNode best = null;
        Optional<Integer> bestRating = Optional.empty();

        for (MoveNode node : this.currentOptions) {
            int rating = node.getRating(player);

            if (!bestRating.isPresent()
                    || bestRating.get() < rating) {
                bestRating = Optional.of(rating);
                best = node;
            }
        }

        return best.getMove();
    }

    private void recalculate(Color color) {
        for (MoveNode node : currentOptions) {
            node.recalculate(maxSearchDepth - 1);
        }
    }
}
