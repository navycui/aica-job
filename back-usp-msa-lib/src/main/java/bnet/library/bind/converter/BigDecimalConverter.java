package bnet.library.bind.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;

import bnet.library.util.CoreUtils;

public class BigDecimalConverter implements Converter<String, BigDecimal> {

	@Override
	public BigDecimal convert(String source) {
		return CoreUtils.string.toBigDecimal(source);
	}

}