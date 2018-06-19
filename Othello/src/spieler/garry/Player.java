package spieler.garry;

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
        currentOptions = currentOptions.takeMove(move);
    }

    private Move getBest() {
        recalculate(player);

        return currentOptions.getBest(player).getMove();
    }

    private void recalculate(Color color) {
        currentOptions.recalculate(maxSearchDepth - 1);
    }
}
