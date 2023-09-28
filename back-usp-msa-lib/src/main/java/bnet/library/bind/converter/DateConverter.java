package bnet.library.bind.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
        return CoreUtils.string.toDate(source);
	}

}
