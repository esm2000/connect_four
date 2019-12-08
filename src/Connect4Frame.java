
import javax.swing.*;

public class Connect4Frame {

	public static void main(String[] args) {

		JFrame TicTacToeBoard = new JFrame("C o N n E c T   f O u R");

		TicTacToeBoard.setSize(500, 580);

		TicTacToeBoard.setResizable(false);

		TicTacToeBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Connect4GUI board = new Connect4GUI("P1", "P2");

		TicTacToeBoard.add(board);

		TicTacToeBoard.setVisible(true);
	}

}
