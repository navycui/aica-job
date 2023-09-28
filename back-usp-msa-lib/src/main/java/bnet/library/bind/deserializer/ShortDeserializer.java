package bnet.library.bind.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class ShortDeserializer extends JsonDeserializer<Short> {

    public ShortDeserializer() {
        super(Short.class);
    }

    @Override
    public Short deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toShort(parser.getText());
    }

}
