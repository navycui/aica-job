package bnet.library.bind.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    public BigDecimalDeserializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toBigDecimal(parser.getText());
    }
}
