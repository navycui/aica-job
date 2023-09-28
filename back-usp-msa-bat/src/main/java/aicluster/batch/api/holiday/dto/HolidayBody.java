package aicluster.batch.api.holiday.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="body")
public class HolidayBody {

	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	private List<HolidayItem> items;

	private Integer numOfRows;
	private Integer pageNo;
	private Long totalCount;
	
	public List<HolidayItem> getItems() {
		List<HolidayItem> items = new ArrayList<>();
		if(this.items != null) {
			items.addAll(this.items);
		}
		return items;
	}
	
	public void setItems(List<HolidayItem> items) {
		this.items = new ArrayList<>();
		if(items != null) {
			this.items.addAll(items);
		}
	}
}
