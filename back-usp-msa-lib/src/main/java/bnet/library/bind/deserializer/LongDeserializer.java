package bnet.library.bind.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class LongDeserializer extends JsonDeserializer<Long> {

    public LongDeserializer() {
        super(Long.class);
    }

    @Override
    public Long deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toLong(parser.getText());
    }

}
