package bnet.library.excel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelMergeRows implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -3125570969594307802L;
    private List<ExcelMergeRange> mergeRanges = new ArrayList<>();
    private List<Object[]> values = new ArrayList<>();

    public void addValues(Object ... values) {
        if (this.values.size() > 0) {
            if (this.values.get(0).length != values.length) {
                throw new RuntimeException("Values array의 size는 모두 동일해야 합니다.");
            }
        }
        this.values.add(values);
    }
    public void clearValues() {
        this.values.clear();
    }
    public List<Object[]> getValues() {
		List<Object[]> values = new ArrayList<>();
		if(this.values != null) {
			values.addAll(this.values);
		}
		return values;
	}
	public void setValues(List<Object[]> values) {
		this.values = new ArrayList<>();
		if(values != null) {
			this.values.addAll(values);
		}
	}
    public void addMergeRange(int beginRow, int endRow, int beginColumn, int endColumn) {
        this.mergeRanges.add(new ExcelMergeRange(beginRow, endRow, beginColumn, endColumn));
    }
    public void clearMergedColumns() {
        this.mergeRanges.clear();
    }
    public List<ExcelMergeRange> getMergeRanges() {
		List<ExcelMergeRange> mergeRanges = new ArrayList<>();
		if(this.mergeRanges != null) {
			mergeRanges.addAll(this.mergeRanges);	
		}
		return mergeRanges;
	}
	public void setMergeRanges(List<ExcelMergeRange> mergeRanges) {
		this.mergeRanges = new ArrayList<>();
		if(mergeRanges != null) {
			this.mergeRanges.addAll(mergeRanges);
		}
	}
}
