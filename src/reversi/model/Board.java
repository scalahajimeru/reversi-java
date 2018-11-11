package reversi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 盤面の状態管理を行います。
 */
public class Board {
    // 盤面の状態
    // 盤の終端の一個外側にもマスを用意し、盤面の外側を表現する。
    private CellState[][] cells = new CellState[10][10];

    public Board() {
        initialize();
    }

    public CellState[][] getCells() {
        return cells;
    }

    public void initialize() {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col] = CellState.BLANK;
            }
        }

        // 外周はOuterCellとする
        for (int col = 0; col < 10; col++) {
            cells[0][col] = CellState.OUTER;
            cells[9][col] = CellState.OUTER;
        }
        for (int row = 1; row <= 8; row++) {
            cells[row][0] = CellState.OUTER;
            cells[row][9] = CellState.OUTER;
        }

        cells[4][4] = CellState.WHITE;
        cells[4][5] = CellState.BLACK;
        cells[5][4] = CellState.BLACK;
        cells[5][5] = CellState.WHITE;
    }

    public boolean isInRange(int row, int col) {
        // 盤面の範囲内か確認
        if (row < 1 || 8 < row || col < 1 || 8 < col) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isInRangeWithOuterCell(int row, int col) {
        // 盤面の範囲内か確認
        if (row < 0 || 9 < row || col < 0 || 9 < col) {
            return false;
        } else {
            return true;
        }
    }

    public CellState getCell(int row, int col) {
        if (isInRangeWithOuterCell(row, col)) {
            return cells[row][col];
        } else {
            return null;
        }
    }

    public void setCell(int row, int col, CellState cellState) {
        if (isInRange(row, col)) {
            cells[row][col] = cellState;
        } else {
            throw new IllegalArgumentException(
                    String.format("Cell index is out of range. : (%d, %d)", row, col));
        }
    }

    public boolean hasBlankCell() {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if (cells[row][col] == CellState.BLANK) {
                    return true;
                }
            }
        }

        return false;
    }

    public int countDarkDisks() {
        int darkdiskCount = 0;
        for (int row_ = 1; row_ <= 8; row_++) {
            for (int col_ = 1; col_ <= 8; col_++) {
                if (cells[row_][col_] == CellState.BLACK) {
                    darkdiskCount++;
                }
            }
        }
        return darkdiskCount;
    }

    public int countLightDisks() {
        return 64 - countDarkDisks();
    }

    public void flip(List<CellPos> cells) {
        for (CellPos pos : cells) {
            this.cells[pos.getRow()][pos.getCol()] = this.cells[pos.getRow()][pos.getCol()].getReverseColor();
        }
    }

    // 石の探索方向
    private CellPos[] searchDirections = {
            new CellPos(0, 1),
            new CellPos(1, 1),
            new CellPos(1, 0),
            new CellPos(1, -1),
            new CellPos(0, -1),
            new CellPos(-1, -1),
            new CellPos(-1, 0),
            new CellPos(-1, 1),
    };

    /**
     * 位置(row, col)にdiskで指定する色の石を置く時に反転する石を検索します。
     *
     * @param pos    石を置く位置
     * @param myDisk 置こうとする石
     * @return 反転する石のリスト
     */
    public List<CellPos> selectFlippableCells(CellPos pos, CellState myDisk) {
        ArrayList<CellPos> flippableCells = new ArrayList<CellPos>();
        // 各方向(全8方向)について
        for (CellPos dir : searchDirections) {
            // 反転可能な石を探索する
            List<CellPos> flippableCellsInDir = this.scanCells(new ArrayList<CellPos>(),
                    myDisk, pos.add(dir), dir);
            flippableCells.addAll(flippableCellsInDir);
        }
        return flippableCells;
    }

    /**
     * 指定の1方向について反転可能な石を探索する
     *
     * @param foundCells 検索結果が入るリスト
     * @param myColor    置こうとする石の色
     * @param pos        探索する位置
     * @param dir        探索を進める方向
     * @return 探索結果のリスト。反転可能な石がなければサイズ0のリスト。
     */
    private List<CellPos> scanCells(List<CellPos> foundCells,
                                    CellState myColor, CellPos pos, CellPos dir) {
        CellPos curPos = new CellPos(pos.getRow(), pos.getCol());
        while (true) {
            if (cells[curPos.getRow()][curPos.getCol()] == myColor.getReverseColor()) {
                foundCells.add(curPos);
                curPos = curPos.add(dir);
                continue;
            } else {
                if (cells[curPos.getRow()][curPos.getCol()] == myColor) {
                    return foundCells;
                } else {
                    return new ArrayList<CellPos>();
                }
            }
        }
    }

    // selectFilppableCellsInDirectionの再帰処理版
    private List<CellPos> scanCells_rec(List<CellPos> foundCells,
                                        CellState myColor, CellPos pos, CellPos dir) {
        if (cells[pos.getRow()][pos.getCol()] == myColor.getReverseColor()) {
            foundCells.add(pos);
            return scanCells_rec(foundCells, myColor, pos.add(dir), dir);
        } else {
            if (cells[pos.getRow()][pos.getCol()] == myColor) {
                return foundCells;
            } else {
                return new ArrayList<CellPos>();
            }
        }
    }

}
