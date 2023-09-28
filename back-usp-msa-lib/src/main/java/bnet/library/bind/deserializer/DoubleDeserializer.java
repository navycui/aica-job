package bnet.library.bind.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class DoubleDeserializer extends JsonDeserializer<Double> {

    public DoubleDeserializer() {
        super(Double.class);
    }

    @Override
    public Double deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toDouble(parser.getText());
    }
}
