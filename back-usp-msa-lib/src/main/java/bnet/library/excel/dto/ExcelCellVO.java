package bnet.library.excel.dto;

import java.io.Serializable;

public class ExcelCellVO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2531512370602209774L;
	private int rowNum;
	private int cellNum;
	private String propertyName;

	public ExcelCellVO(String propertyName, int rowNum, int cellNum) {
		this.rowNum = rowNum;
		this.cellNum = cellNum;
		this.propertyName = propertyName;
	}

	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getCellNum() {
		return cellNum;
	}
	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
