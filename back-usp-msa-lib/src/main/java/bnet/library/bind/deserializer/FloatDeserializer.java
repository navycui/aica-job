package bnet.library.bind.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class FloatDeserializer extends JsonDeserializer<Float> {

    public FloatDeserializer() {
        super(Float.class);
    }

    @Override
    public Float deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toFloat(parser.getText());
    }

}
