package bnet.library.excel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheet<T> implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 6535728636476760463L;
    private String title;
    private String sheetName;
    private String[] properties;
    private List<T> rows;
    private ExcelMergeRows headers;
    private ExcelMergeRows footers;
    private ExcelMergeRows mergeRegions;
    private Integer[] columnWidths = null;
    private String fontName = "맑은 고딕";

    public ExcelSheet() {

    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @param sheetName the sheetName to set
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * @return the properties
     */
    public String[] getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(String ... properties) {
        this.properties = properties;
    }
    /**
     * @return the rows
     */
    public List<? extends Object> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void addRow(T row) {
        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }
        this.rows.add(row);
    }

    public void addRows(List<T> rows) {
        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }
        this.rows.addAll(rows);
    }

    public void setHeaders(Object ... headers) {
        this.headers = new ExcelMergeRows();
        this.headers.addValues(headers);
    }
    public void setHeaders(ExcelMergeRows headers) {
        this.headers = headers;
    }
    public ExcelMergeRows getHeaders() {
        return this.headers;
    }

    public void setFooters(Object ... footers) {
        this.footers = new ExcelMergeRows();
        this.footers.addValues(footers);
    }
    public void setFooters(ExcelMergeRows footers) {
        this.footers = footers;
    }

    public ExcelMergeRows getFooters() {
        return footers;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public ExcelMergeRows getMergeRegions() {
        return mergeRegions;
    }

    public void setMergeRegions(ExcelMergeRows mergeRegions) {
        this.mergeRegions = mergeRegions;
    }

    public Integer[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(Integer[] columnWidths) {
        this.columnWidths = columnWidths;
    }
}
