package bnet.library.bind.deserializer;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import bnet.library.util.CoreUtils;

public class DateDeserializer extends JsonDeserializer<Date> {

    public DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext dc) throws IOException, JsonProcessingException {
        return CoreUtils.string.toDate(parser.getText());
    }

}