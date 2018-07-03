package it.polimi.ingsw.server.utils.io;

import it.polimi.ingsw.server.utils.ServerLogger;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

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
        String content = json.toString();

        ServerLogger.getLogger().log(Level.FINEST, "Sending data: {0}", content);

        super.write(content);
        super.newLine();
        
        if (this.autoFlush) {
            super.flush();
        }
    }
}
