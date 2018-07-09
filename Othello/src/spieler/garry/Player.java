package spieler.garry;

class Player {
    private Color player;
    private long timeInMs;
    private int maxSearchDepth;
    private int currentSearchDepth;
    private int stepCounter;

    private MoveOptions currentOptions;

    public Player(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
    }

    public Move calculateMove(Move previousMove, long timeWhite, long timeBlack)
            throws MoveException {
        stepCounter++;

        long ownTime = player == Color.WHITE ? timeWhite : timeBlack;

        if (ownTime > 0) {
            double stepProgress = (double) stepCounter / 31;
            double timeProgress = (double) ownTime / timeInMs;

            if (timeProgress > stepProgress)
            {
                if (currentSearchDepth > 1)
                    currentSearchDepth--;
            }
            else if (timeProgress * 2 < stepProgress)
            {
                if (currentSearchDepth < maxSearchDepth)
                    currentSearchDepth++;
            }
        }

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
        this.currentSearchDepth = 1;
        this.stepCounter = 0;

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
        currentOptions.recalculate(currentSearchDepth - 1);

        return currentOptions.getBest().getMove();
    }
}
