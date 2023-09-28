package bnet.library.excel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelWorkbook implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -4035232669238150711L;
    private String filename;
    private List<ExcelSheet<?>> sheets;

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    /**
     * @return the sheets
     */
    public List<ExcelSheet<?>> getSheets() {
    	List<ExcelSheet<?>> sheets = new ArrayList<>();
    	if(this.sheets != null) {
    		sheets.addAll(this.sheets);
    	}
        return sheets;
    }
    /**
     * @param sheets the sheets to set
     */
    public void setSheets(List<ExcelSheet<?>> sheets) {
        this.sheets = new ArrayList<>();
        if(sheets != null) {
        	this.sheets.addAll(sheets);
        }
    }

    public void addSheet(ExcelSheet<?> sheet) {
        if (sheets == null) {
            sheets = new ArrayList<>();
        }
        sheets.add(sheet);
    }
}
