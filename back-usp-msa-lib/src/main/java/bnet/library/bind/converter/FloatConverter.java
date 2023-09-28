package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class FloatConverter implements Converter<String, Float> {

	@Override
	public Float convert(String text) {
        return CoreUtils.string.toFloat(text);
	}

}
