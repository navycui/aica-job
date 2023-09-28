package bnet.library.excel.dto;

import java.util.ArrayList;
import java.util.List;

import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;

public class ExcelInputCells {
	private List<ExcelCellVO> list = new ArrayList<>();

	public ExcelCellVO add(String propertyName, String colName, int rowName) {
		if (rowName < 1) {
			throw new InvalidationException("rowName은 0보다 커야 합니다.");
		}
		if (string.isBlank(colName)) {
			throw new InvalidationException("colName을 입력하세요.");
		}
		colName = string.upperCase(colName);
		int colNum = 0;
		for (int i = colName.length()-1, k = 0; i > -1; i--, k++) {
			int b = (int)Math.pow(26 * 1.0, k * 1.0);
			char coln = colName.charAt(i);
			int col = (coln - 'A' + 1);
			colNum += b * col;
		}
		ExcelCellVO vo = new ExcelCellVO(propertyName, rowName-1, colNum-1);
		list.add(vo);
		return vo;
	}

	public List<ExcelCellVO> getList() {
		List<ExcelCellVO> list = new ArrayList<>();
		if(this.list != null) {
			list.addAll(this.list);
		}
		return list;
	}
	
	public void setList(List<ExcelCellVO> list) {
		this.list = new ArrayList<>();
		if(list != null) {
			this.list.addAll(list);
		}
	}
}
