package reversi.view;

import reversi.model.Board;
import reversi.model.CellPos;
import reversi.model.CellState;
import reversi.model.GameState;
import reversi.model.Move;
import reversi.model.Turn;

public class GameView {
	public static void showState(GameState state) {
		if(state.isGameover()) {
			System.out.println("ゲーム終了");
			System.out.println("黒: " + state.getBoard().countDarkDisks() + "枚");
			System.out.println("白: " + state.getBoard().countLightDisks() + "枚");
		} else {
			System.out.println((state.getMoveCount()) + "手目 " + (state.getTurn() == Turn.BLACK ? "黒" : "白") + "の番");
		}
		showBoard(state.getBoard());
	}

	public static void showBoard(Board board) {
		System.out.println("   a  b  c  d  e  f  g  h");
		System.out.println("  +-----------------------+");

		CellState[][] cells = board.getCells();
		for(int row = 1; row <= 8; row++) {
			System.out.print(row + " |");
			for(int col = 1; col <= 8; col++) {
				System.out.print(cells[row][col].getDisplayString());
				System.out.print("|");
			}
			System.out.println(" " + row);
			System.out.println("  +--+--+--+--+--+--+--+--+");
		}
		System.out.println("   a  b  c  d  e  f  g  h");
	}
}
