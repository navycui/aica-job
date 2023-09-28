package bnet.library.excel.dto;

import java.io.Serializable;

public class ExcelMergeRange implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -3464880803858687550L;
    private int beginRow = 0;
    private int endRow = 0;
    private int beginColumn = 0;
    private int endColumn = 0;

    public ExcelMergeRange() {

    }

    public ExcelMergeRange(int beginRow, int endRow, int beginColumn, int endColumn) {
        this.beginRow = beginRow;
        this.endRow = endRow;
        this.beginColumn = beginColumn;
        this.endColumn = endColumn;
    }

    public int getBeginRow() {
        return beginRow;
    }
    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }
    public int getEndRow() {
        return endRow;
    }
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }
    public int getBeginColumn() {
        return beginColumn;
    }
    public void setBeginColumn(int beginColumn) {
        this.beginColumn = beginColumn;
    }
    public int getEndColumn() {
        return endColumn;
    }
    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }
}
