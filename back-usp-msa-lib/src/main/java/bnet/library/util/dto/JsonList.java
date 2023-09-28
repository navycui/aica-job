package bnet.library.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Swagger는 Json array를 응답으로 사용할 수 없다고 함.
 * 그래서, array를 다음과 같이 출력하기 위해 만든 객체
 * <pre>
 * 	{
 * 		list: [ ... ]
 * 	}
 * </pre>
 * @author patrick
 *
 * @param <T> Collection type
 */
@Data
@AllArgsConstructor
public class JsonList<T> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1708939558534847407L;

	private List<T> list;
	
	public List<T> getList() {
		List<T> list = new ArrayList<>();
		if(this.list != null) {
			list.addAll(this.list);
		}
		return list;
	}
	
	public void setList(List<T> list) {
		this.list = new ArrayList<>();
		if(list != null) {
			this.list.addAll(list);
		}
	}
}
