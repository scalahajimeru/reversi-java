package reversi.model;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private Turn turn;
    private CellPos putPos = new CellPos();
    private ArrayList<CellPos> flippedPosList = new ArrayList<CellPos>();

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public CellPos getPutPos() {
        return putPos;
    }

    public void setPutPos(CellPos putPos) {
        this.putPos = putPos;
    }

    public List<CellPos> getFlippedPosList() {
        return java.util.Collections.unmodifiableList(flippedPosList);
    }

    public void setFlippedPosList(List<CellPos> list) {
        flippedPosList = new ArrayList<>(list);
    }
}
