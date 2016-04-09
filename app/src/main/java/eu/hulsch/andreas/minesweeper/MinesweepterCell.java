package eu.hulsch.andreas.minesweeper;

/**
 * Created by murphy on 08.04.2016.
 */
public class MinesweepterCell
{
    private boolean covered;
    private boolean marked;
    private int mineCount; // -1 for a mine

    private int row;
    private int column;


    public MinesweepterCell(boolean marked, boolean covered, int mineCount, int row, int column)
    {
        this.marked = marked;
        this.covered = covered;
        this.mineCount = mineCount;

        this.row = row;
        this.column = column;
    }


    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
