package it.polimi.ingsw.server.compilers;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WindowCompiler {

    /**
     * The default path for the instructions directives.
     */
    private static final String WINDOWS_PATH = "windows.xml";

    private WindowCompiler() {}

    /**
     * Compiles all windows in the default file.
     * @return a {@link List} of {@link Window}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<Window> compileAll() throws IOException, SAXException, ParserConfigurationException {
        return compileAll(WINDOWS_PATH, true);
    }

    /**
     * Compiles all windows in the provided path.
     * @param path the path to the file containing the windows
     * @param isResource {@code true} if the path is relative to the resource folder
     * @return a {@link List} of {@link Window}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    public static List<Window> compileAll(String path, boolean isResource) throws ParserConfigurationException, SAXException, IOException {
        Map<String, Object>[] rawWindows = getRawWindows(path, isResource);

        // compiles every window and puts the result in a list
        List<Window> windows = Arrays.stream(rawWindows)
                .map(WindowCompiler::compile)
                .collect(Collectors.toList());

        // creates a map containing windowId: Window
        Map<String, Window> windowMap = windows.stream()
                .collect(Collectors.toMap(Window::getId, o -> o));

        // sets the sibling to every window
        for (int i = 0; i < rawWindows.length; i++) {
            // gets the current window
            Window window = windows.get(i);

            // gets the sibling id for the current window
            String windowSibling = (String) rawWindows[i].get(WindowNodes.WINDOW_SIBLING);

            // sets the sibling to the current window
            window.setSibling(windowMap.get(windowSibling));
        }

        // finally return the windows
        return windows;
    }

    /**
     * Compiles a window node into its corresponding {@link Window} class.
     * @param node the node to compile
     * @return an instance {@link Window}
     */
    public static Window compile(Node node) {
        // only window nodes are allowed here
        if (!node.getNodeName().equals(WindowNodes.WINDOW_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to a window, instead of a " + node.getNodeName());
        }

        // gets the window information by converting the node into a dictionary
        return compile(XMLUtils.xmlToMap(node));
    }

    /**
     * Compiles a window node into its corresponding {@link Window} class.
     * @param windowInfo the info of the window to compile
     * @return an instance {@link Window}
     */
    public static Window compile(Map<String, Object> windowInfo) {
        // gets the number of rows
        int rows = Integer.parseInt((String) windowInfo.get(WindowNodes.WINDOW_NUMBER_OF_ROWS));

        // gets the number of columns
        int columns = Integer.parseInt((String) windowInfo.get(WindowNodes.WINDOW_NUMBER_OF_COLUMNS));

        // gets the id of the window
        String id = (String) windowInfo.get(WindowNodes.WINDOW_ID);

        // gets the difficulty of the window
        int difficulty = Integer.parseInt((String) windowInfo.get(WindowNodes.WINDOW_DIFFICULTY));

        // gets the cells
        Map<String, Object>[] cellsMap = XMLUtils.getMapArray(windowInfo, WindowNodes.WINDOW_CELL);

        Cell[] cellsArray = Arrays.stream(cellsMap)
                .map(WindowCompiler::parseCell)
                .toArray(Cell[]::new);

        Cell[][] cells = new Cell[rows][columns];

        // converts the cells 1D array to a 2D array
        for (int i = 0; i < rows; i++) {
            System.arraycopy(cellsArray, i * columns, cells[i], 0, columns);
        }

        return new Window(
                difficulty,
                rows,
                columns,
                id,
                // the sibling is set to null because it is not known now
                null,
                cells
        );
    }

    /**
     * Parses a cell.
     * @param cellInfo the info of the cell to parse
     * @return an instance of {@link Cell} coherently to the provided cell info
     */
    private static Cell parseCell(Map<String, Object> cellInfo) {
        // gets the shade of the cell
        String shade = (String) cellInfo.get(WindowNodes.WINDOW_CELL_SHADE);

        // gets the color of the cell
        String color = (String) cellInfo.get(WindowNodes.WINDOW_CELL_COLOR);

        // creates the cell
        return new Cell(
                // the default value for the shade is 0
                shade == null ? 0 : Integer.parseInt(shade),
                // the default value for the color is null
                color == null ? null : GlassColor.fromString(color)
        );
    }

    /**
     * Returns an array of {@link Map} each one containing the information about a window.
     * @param path the path to the file containing the windows
     * @param isResource {@code true} if the path is relative to the resource folder
     * @return a {@link List} of {@link Window}
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parse errors occur
     * @throws ParserConfigurationException if a DocumentBuilder
     *      cannot be created which satisfies the configuration requested.
     */
    private static Map<String, Object>[] getRawWindows(String path, boolean isResource) throws IOException, SAXException, ParserConfigurationException {
        Node node;

        // if the path is relative to the resources folder then the class loader must be provided
        if (isResource) {
            node = XMLUtils.parseXmlFromResource(path, WindowCompiler.class.getClassLoader());
        }
        else {
            node = XMLUtils.parseXml(path);
        }

        // the document
        Map<String, Object> document = XMLUtils.xmlToMap(node);

        if (document.containsKey(WindowNodes.WINDOW_NODE_NAME)) {
            // the children of windows (window)
            return XMLUtils.getMapArrayAnyway(document, WindowNodes.WINDOW_NODE_NAME);
        }

        //noinspection unchecked
        return new Map[0];
    }

    /**
     * Holds the constants used while accessing the xml nodes and attributes.
     */
    private final class WindowNodes {
        public static final String WINDOW_NODE_NAME = "window";
        public static final String WINDOW_NUMBER_OF_ROWS = "@rows";
        public static final String WINDOW_NUMBER_OF_COLUMNS = "@columns";
        public static final String WINDOW_ID = "@id";
        public static final String WINDOW_DIFFICULTY = "@difficulty";
        public static final String WINDOW_SIBLING = "@sibling";
        public static final String WINDOW_NAME_KEY = "@name";
        public static final String WINDOW_CELL = "cell";
        public static final String WINDOW_CELL_SHADE = "@shade";
        public static final String WINDOW_CELL_COLOR = "@color";

        private WindowNodes() {}
    }
}

