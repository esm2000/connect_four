import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Connect4GUI extends JComponent implements MouseListener {

	private int xBoard;
	private int yBoard; // the starting location of the board
	private int squareSize; // the size of a square on the board

	private String player1;
	private String player2; // name of players
	private int player1Score;
	private int player2Score; // number of wins

	private String message; // to be displayed in the window

	private int[][] pieces; // a 6*7 array containing info for pieces
							// 0: no piece on the square
							// 1: player1's piece on the square
							// 2: player2's piece on the square
	private boolean isPlayer1Turn; // indicate whose turn
	private int victoryType;// 0: game not over
							// 1: player1 wins
							// 2: player2 wins
							// 3: tie

	public Connect4GUI(String player1, String player2) {
		super();
		xBoard = 75;
		yBoard = 50;
		squareSize = 50; // the size of board would be 350 * 300
		this.player1 = player1;
		this.player2 = player2;
		player1Score = 0;
		player2Score = 0;
		message = "New Game! " + player1 + "'s turn:";
		pieces = new int[][] { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 } };
		addMouseListener(this); // connect this component with mouse
		isPlayer1Turn = true; // by default, player1 goes first.
		victoryType = 0;
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2D = (Graphics2D) g;

		// draw the game board
		drawBoard(g2D);

		// draw the pieces
		drawPieces(g2D);

		// draw the score board
		drawScoreBoard(g2D);

	}

	private void drawScoreBoard(Graphics2D g2D) {
		// First rectangle
		g2D.setColor(Color.YELLOW);
		g2D.fillRect(0, 400, 600, 50);

		// Second rectangle
		g2D.setColor(Color.CYAN);
		g2D.fillRect(0, 450, 600, 50);

		// Third rectangle
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(0, 500, 600, 50);

		// Draw texts
		g2D.setColor(Color.BLACK);
		g2D.setFont(new Font("Serif", Font.BOLD, 20));
		// First row
		g2D.drawString("Player 1: " + player1, 20, 430);
		g2D.drawString("Player 2: " + player2, 325, 430);
		// Second row
		g2D.drawString("Player 1 SCORE: " + player1Score, 20, 480);
		g2D.drawString("Player 2 SCORE: " + player2Score, 325, 480);
		// Third row
		g2D.drawString(message, 20, 530);
	}

	private void drawPieces(Graphics2D g2D) {

		// loop through the board,
		// if pieces[i][j] == 1, draw a red circle
		// if pieces[i][j] == 2, draw a blue circle
		int x = xBoard;
		int y = yBoard;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (pieces[i][j] == 1) {
					g2D.setColor(Color.RED);
					g2D.fillOval(x, y, squareSize, squareSize);
				}

				if (pieces[i][j] == 2) {
					g2D.setColor(Color.BLUE);
					g2D.fillOval(x, y, squareSize, squareSize);
				}

				x += squareSize;
			}
			x = xBoard;
			y += squareSize;
		}

	}

	private void drawBoard(Graphics2D g2D) {
		int x = xBoard;
		int y = yBoard;
		g2D.setColor(Color.BLUE);
		for (int i = 0; i < 6; i++) {// i is the row index
			for (int j = 0; j < 7; j++) { // j is the column index
				g2D.drawRect(x, y, squareSize, squareSize);

				// update x and y
				x += squareSize;
			}
			x = xBoard;
			y += squareSize;
		}
	}

	public int findNewRowIndex(int rowIndex, int colIndex) {

		boolean isEmpty;
		boolean hasPieceBelow;
		for (int i = 5; i >= 0; i--) {// check pieces[i][colIndex]
			isEmpty = (pieces[i][colIndex] == 0);
			if (i != 5) {
				hasPieceBelow = (pieces[i + 1][colIndex] == 1 || pieces[i + 1][colIndex] == 2);
			} else {// if i is 5, then it is already at the bottom.
				hasPieceBelow = true;
			}
			if (isEmpty && hasPieceBelow) {
				return i;
			}
		}
		return -1; // -1 indicates that the column is already full.
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		// extract the x and y coordinate of the click
		int x = evt.getX();
		int y = evt.getY();

		// TODO: End this method unless the mouse click is inside the board.
//		if (//condition to detect that the click is outside of the board){
//				return;
//	}

		// determine which square should be changed
		int rowIndex = (y - yBoard) / squareSize;
		int colIndex = (x - xBoard) / squareSize;

		rowIndex = findNewRowIndex(rowIndex, colIndex);

		// modify the corresponding number in pieces
		// TODO: prevent putting a piece on a non-empty square
		if (isPlayer1Turn) {
			pieces[rowIndex][colIndex] = 1;
			isPlayer1Turn = false; // next time it's player2's turn
		} else {
			pieces[rowIndex][colIndex] = 2;
			isPlayer1Turn = true; // next time it's palyer1's turn
		}

		// Detect victory
		detectVictory();

		// If the game ends, update scores
		updateScore();
		updateMessage();

		// repaint the interface
		repaint();

		// If the current game ends, ask user if they want another game.
		askIfContinue();

	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	private void detectVictory() {

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {

				// detect if there are four pieces of same color on a row

				if (j < 4) {
					if (pieces[i][j] == 1 && pieces[i][j + 1] == 1 && pieces[i][j + 2] == 1 && pieces[i][j + 3] == 1) {
						victoryType = 1;
					}
				}

				// detect if there are four pieces of same color in a column
				if (i < 3) {
					if (pieces[i][j] == 1 && pieces[i + 1][j] == 1 && pieces[i + 2][j] == 1 && pieces[i + 3][j] == 1) {
						victoryType = 1;
					}
				}

				// detect if the diagonal has four pieces of same color
				if (j < 4 && i > 2) {
					if (pieces[i][j] == 1 && pieces[i - 1][j + 1] == 1 && pieces[i - 2][j + 2] == 1
							&& pieces[i - 3][j + 3] == 1) {
						victoryType = 1;
					}
				}
				
				if (j > 2 && i > 2) {
					if (pieces[i][j] == 1 && pieces[i - 1][j - 1] == 1 && pieces[i - 2][j - 2] == 1
							&& pieces[i - 3][j - 3] == 1) {
						victoryType = 1;
					}
				}
				// Player 2:
				// detect if there are four pieces of same color on a row

				if (j < 4) {
					if (pieces[i][j] == 2 && pieces[i][j + 1] == 2 && pieces[i][j + 2] == 2 && pieces[i][j + 3] == 2) {
						victoryType = 2;
					}
				}

				// detect if there are four pieces of same color in a column
				if (i < 3) {
					if (pieces[i][j] == 2 && pieces[i + 1][j] == 2 && pieces[i + 2][j] == 2 && pieces[i + 3][j] == 2) {
						victoryType = 2;
					}
				}

				// detect if the diagonal has four pieces of same color
				if (j < 4 && i > 2) {
					if (pieces[i][j] == 1 && pieces[i - 1][j + 1] == 1 && pieces[i - 2][j + 2] == 1
							&& pieces[i - 3][j + 3] == 1) {
						victoryType = 2;
					}
				}
				
				if (j > 2 && i > 2) {
					if (pieces[i][j] == 2 && pieces[i - 1][j - 1] == 2 && pieces[i - 2][j - 2] == 2
							&& pieces[i - 3][j - 3] == 2) {
						victoryType = 2;
					}
				}
				
			}
		}

		boolean zeroFound = false;

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {

				if (pieces[i][j] == 0) {
					zeroFound = true;
				}

			}
		}

		if (zeroFound == false && victoryType == 0) {

			victoryType = 3;

		}
	}

	private void updateScore() {
		if (victoryType == 1) {
			player1Score++;
		} else if (victoryType == 2) {
			player2Score++;
		}
	}

	private void updateMessage() {
		if (victoryType == 1) {
			message = player1 + " wins!";
		} else if (victoryType == 2) {
			message = player2 + " wins!";
		} else if (victoryType == 0) {
			if (isPlayer1Turn) {
				message = player1 + "'s turn";
			} else {
				message = player2 + "'s turn";
			}
		} else if (victoryType == 3) {
			message = "It's a tie.";
		}
	}

	private void askIfContinue() {
		// Show a dialogbox if the game ends
		if (victoryType != 0) {
			int response = JOptionPane.showConfirmDialog(this, "Play again?", "Game Ended", JOptionPane.YES_NO_OPTION);
			if (response == 0) {// user clicks YES
				victoryType = 0;
				isPlayer1Turn = true;
				pieces = new int[][] { { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 } };
				message = "New Game! " + player1 + "'s turn";
				repaint();
			} else { // user clicks NO
				System.exit(0);
			}
		}
	}
}
