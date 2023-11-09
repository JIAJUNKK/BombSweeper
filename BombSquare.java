import java.util.Random;

/**
 * The `BombSquare` class represents a square on the game board that may contain a bomb or be adjacent to one.
 * It extends the `GameSquare` class and provides functionality for revealing the square, checking for nearby bombs,
 * and handling the game logic related to bomb placement.
 */
public class BombSquare extends GameSquare {

    private boolean thisSquareHasBomb = false;
    public static final int MINE_PROBABILITY = 10;
    private int curX, curY, bombNum;
    private boolean revealed;

    private final int[][] directions = {
        {-1, 1},   {0, 1},   {1, 1},
        {-1, 0},             {1, 0},
        {-1, -1},  {0, -1},  {1, -1}
    };

    /**
     * Constructs a `BombSquare` object at the specified position on the game board.
     * @param x The X-coordinate of the square.
     * @param y The Y-coordinate of the square.
     * @param board The game board to which this square belongs.
     */
    public BombSquare(int x, int y, GameBoard board) {
        super(x, y, "images/blank.png", board);
        Random r = new Random();
        thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
    }

    /**
     * Handles the action when the square is clicked. It reveals the square and updates its image.
     * If the square contains a bomb, it sets the bomb image. Otherwise, it shows the number of nearby bombs.
     * If no nearby bombs are present, it recursively reveals adjacent squares.
     */
    @Override
    public void clicked() {
        if (thisSquareHasBomb) {
            setImage("images/bomb.png");
        } else {
            setImage("images/" + NumOfNearByBomb() + ".png");
            if (NumOfNearByBomb() == 0) {
                revealOthers();
            }
        }
    }

       /**
     * Recursively reveals other squares adjacent to this square if there are no nearby bombs.
     * It traverses in all eight possible directions from the current square, revealing and
     * recursively checking other squares.
     */
    private void revealOthers() {
        if (NumOfNearByBomb() != 0) {
            return;
        }
        for (int i[] : directions) {
            curX = xLocation + i[0];
            curY = yLocation + i[1];
            if (isWithinBoard(curX, curY)) {
                BombSquare bombSquare = (BombSquare) board.getSquareAt(curX, curY);
                if (bombSquare != null && !bombSquare.isRevealed()) {
                    bombSquare.setRevealed(true);
                    bombSquare.clicked();
                    bombSquare.revealOthers();
                }
            }
        }
    }

    /**
     * Checks if this square has been revealed.
     * @return `true` if the square has been revealed, `false` otherwise.
     */
    private boolean isRevealed() {
        return revealed;
    }

    /**
     * Checks if the given coordinates are within the boundaries of the game board.
     * @param x The X-coordinate to check.
     * @param y The Y-coordinate to check.
     * @return `true` if the coordinates are within the game board, `false` otherwise.
     */
    private boolean isWithinBoard(int x, int y) {
        return (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight());
    }

    /**
     * Counts the number of nearby bombs by examining adjacent squares.
     * @return The count of nearby bombs.
     */
    private int NumOfNearByBomb() {
        bombNum = 0;
        for (int i[] : directions) {
            curX = xLocation + i[0];
            curY = yLocation + i[1];
            if (isWithinBoard(curX, curY)) {
                BombSquare bombSquare = (BombSquare) board.getSquareAt(curX, curY);
                if (bombSquare != null && bombSquare.thisSquareHasBomb) {
                    bombNum += 1;
                }
            }
        }
        return bombNum;
    }

    /**
     * Sets the revealed state of the square.
     * @param revealed `true` to mark the square as revealed, `false` to hide it.
     */
    private void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
