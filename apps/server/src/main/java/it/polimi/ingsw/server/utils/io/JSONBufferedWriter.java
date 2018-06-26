package it.polimi.ingsw.server.utils.io;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class JSONBufferedWriter extends BufferedWriter {
    
    private boolean autoFlush;
    
    /**
     * Creates a buffered character-output stream that uses a default-sized
     * output buffer.
     *
     * @param out A Writer
     * @param autoFlush whether the writer should auto-flush
     */
    public JSONBufferedWriter(Writer out, boolean autoFlush) {
        super(out);
        
        this.autoFlush = autoFlush;
    }
    
    /**
     * Creates a new buffered character-output stream that uses an output
     * buffer of the given size.
     *
     * @param out A Writer
     * @param sz  Output-buffer size, a positive integer
     * @param autoFlush whether the writer should auto-flush
     * @throws IllegalArgumentException If {@code sz <= 0}
     */
    public JSONBufferedWriter(Writer out, int sz, boolean autoFlush) {
        super(out, sz);
    
        this.autoFlush = autoFlush;
    }
    
    public void writeJSON(JSONObject json) throws IOException {
        super.write(json.toString());
        super.newLine();
        
        if (this.autoFlush) {
            super.flush();
        }
    }
}
