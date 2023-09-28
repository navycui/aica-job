package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class ShortConverter implements Converter<String, Short> {

	@Override
	public Short convert(String text) {
        return CoreUtils.string.toShort(text);
	}

}
