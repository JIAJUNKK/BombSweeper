import java.util.*;
public class BombSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;
	private int curX, curY, bombNum;
	private boolean revealed;

	private final int[][] directions = {
		{-1, 1},   {0, 1},   {1, 1},
		{-1, 0},             {1, 0},
		{-1, -1},  {0, -1},  {1, -1}
	};

	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png", board);
		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}

	public void clicked() {
		if (thisSquareHasBomb) {
			setImage("images/bomb.png");
			revealAllBombs();
		} else {
			setImage("images/" + NumOfNearByBomb() + ".png");
			if (NumOfNearByBomb() == 0) {
				revealOthers();
			} 
		}
	}

	private void revealAllBombs(){
		for (int x = 0; x < board.getWidth(); x++){
			for (int y = 0; y < board.getHeight(); y ++){
				BombSquare bsq = (BombSquare)board.getSquareAt(x, y);
				if (bsq != null && bsq.thisSquareHasBomb){
					bsq.setImage("images/bomb.png");
				}
			}
		}
	}

	private void revealOthers(){
		if (NumOfNearByBomb() != 0){return;}
		for (int i []: directions){
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

	private boolean isRevealed(){
		return revealed;
	}
	private boolean isWithinBoard(int x, int y){
		return (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight());
	}
	private int NumOfNearByBomb(){
		bombNum = 0;
		for (int i []: directions){
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
	
	private void setRevealed(boolean revealed){
		this.revealed = revealed;
	}
}
