package reversi.model;

public enum CellState {
	BLANK,
	OUTER,
	BLACK,
	WHITE;

	public String getDisplayString() {
		switch(this) {
		case BLANK: return "  ";
		case OUTER: return "＃";
		case WHITE: return "○";
		case BLACK:  return "●";
		default:    return "";
		}
	}

	public CellState getReverseColor() {
		switch(this) {
		case WHITE: return BLACK;
		case BLACK:  return WHITE;
		default:    return this;
		}
	}
}
