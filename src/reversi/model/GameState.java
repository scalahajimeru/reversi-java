package reversi.model;

import java.util.LinkedList;
import java.util.List;

/**
 * ゲームの状態管理を行います。
 */
public class GameState {

    Board board = new Board();
    Turn turn = Turn.BLACK;
    boolean gameover = false;
    int moveCount = 1;

    public static CellState turnToState(Turn turn) {
        return turn == Turn.WHITE ? CellState.WHITE : CellState.BLACK;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public void switchTurn() {
        turn = (turn == Turn.BLACK ? Turn.WHITE : Turn.BLACK);
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public void registerMove(int row, int col, List<CellPos> flippedCells) {
        Move move = new Move();
        move.setTurn(turn);
        move.setPutPos(new CellPos(row, col));
        move.setFlippedPosList(flippedCells);
//		undoBuffer.add(move);
        moveCount++;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void initialize() {
        board.initialize();
        turn = Turn.BLACK;
        gameover = false;
        moveCount = 1;
    }

}
