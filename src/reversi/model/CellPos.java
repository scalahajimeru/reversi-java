package reversi.model;

// immutable
public class CellPos {
	private int row;
	private int col;

	public CellPos() {
		row = 0;
		col = 0;
	}

	public CellPos(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public CellPos add(CellPos pos) {
		return new CellPos(row + pos.row, col + pos.col);
	}
}
