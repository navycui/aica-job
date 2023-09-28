package aicluster.common.api.holiday.dto;

import java.util.ArrayList;
import java.util.List;

import aicluster.common.common.dto.HldySmmryDto;
import aicluster.common.common.entity.CmmtRestde;
import aicluster.common.common.entity.CmmtRestdeExcl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YearHolidaysDto {

	private List<HldySmmryDto> summary;
	private List<CmmtRestde> holidays;
	private List<CmmtRestdeExcl> exclHolidays;

	public List<HldySmmryDto> getSummary() {
		List<HldySmmryDto> summary = new ArrayList<>();
		if(this.summary != null) {
			summary.addAll(this.summary);
		}
		return summary;
	}

	public void setSummary(List<HldySmmryDto> summary) {
		this.summary = new ArrayList<>();
		if(summary != null) {
			this.summary.addAll(summary);
		}
	}
	
	public List<CmmtRestde> getHolidays() {
		List<CmmtRestde> holidays = new ArrayList<>();
		if(this.holidays != null) {
			holidays.addAll(this.holidays);
		}
		return holidays;
	}

	public void setHolidays(List<CmmtRestde> holidays) {
		this.holidays = new ArrayList<>();
		if(holidays != null) {
			this.holidays.addAll(holidays);
		}
	}
	
	public List<CmmtRestdeExcl> getExclHolidays() {
		List<CmmtRestdeExcl> exclHolidays = new ArrayList<>();
		if(this.exclHolidays != null) {
			exclHolidays.addAll(this.exclHolidays);
		}
		return exclHolidays;
	}

	public void setExclHolidays(List<CmmtRestdeExcl> exclHolidays) {
		this.exclHolidays = new ArrayList<>();
		if(exclHolidays != null) {
			this.exclHolidays.addAll(exclHolidays);
		}
	}
}
