package bnet.library.bind.converter;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class DoubleConverter implements Converter<String, Double> {

	@Override
	public Double convert(String source) {
        return CoreUtils.string.toDouble(source);
	}

}
