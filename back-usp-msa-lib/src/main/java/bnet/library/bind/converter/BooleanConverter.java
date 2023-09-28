package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class BooleanConverter implements Converter<String, Boolean> {

	@Override
	public Boolean convert(String source) {
		return CoreUtils.string.toBoolean(source);
	}

}
