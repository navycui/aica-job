package bnet.library.bind.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

	public BooleanDeserializer() {
        super(Boolean.class);
    }

    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        String text = parser.getText();
        return CoreUtils.string.toBoolean(text);
    }
}
