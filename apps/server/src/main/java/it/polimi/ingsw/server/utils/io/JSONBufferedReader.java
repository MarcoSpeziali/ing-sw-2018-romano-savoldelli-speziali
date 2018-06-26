package it.polimi.ingsw.server.utils.io;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class JSONBufferedReader extends BufferedReader {
    /**
     * Creates a buffering character-input stream that uses an input buffer of
     * the specified size.
     *
     * @param in A Reader
     * @param sz Input-buffer size
     * @throws IllegalArgumentException If {@code sz <= 0}
     */
    public JSONBufferedReader(Reader in, int sz) {
        super(in, sz);
    }
    
    /**
     * Creates a buffering character-input stream that uses a default-sized
     * input buffer.
     *
     * @param in A Reader
     */
    public JSONBufferedReader(Reader in) {
        super(in);
    }
    
    public JSONObject readJSON() throws IOException {
        return new JSONObject(super.readLine());
    }
}
