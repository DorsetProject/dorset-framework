package edu.jhuapl.dorset.agents;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import edu.jhuapl.dorset.ResponseStatus.Code;

public class ResponseCodeJsonAdapter extends TypeAdapter<Code> {

    @Override
    public Code read(JsonReader reader) throws IOException {
        return Code.fromValue(reader.nextInt());
    }

    @Override
    public void write(JsonWriter writer, Code code) throws IOException {
        writer.value(code.getValue());
    }

}
