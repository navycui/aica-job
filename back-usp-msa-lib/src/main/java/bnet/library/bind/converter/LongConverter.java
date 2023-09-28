package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class LongConverter implements Converter<String, Long> {

	@Override
	public Long convert(String text) {
        return CoreUtils.string.toLong(text);
	}

}
