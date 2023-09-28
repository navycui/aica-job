package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class IntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String text) {
        return CoreUtils.string.toInt(text);
	}

}
