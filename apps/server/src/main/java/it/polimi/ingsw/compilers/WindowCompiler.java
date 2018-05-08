package it.polimi.ingsw.compilers;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.io.XmlUtils;
import org.w3c.dom.Node;
import java.util.Arrays;
import java.util.Map;


public class WindowCompiler {

    private static final String WINDOW_NODE_NAME = "window";

    public static Window compile(Node node) {

        if (!node.getNodeName().equals(WINDOW_NODE_NAME)) {
            throw new IllegalArgumentException("The provided org.w3c.dom.Node must refer to an action, instead of a " + node.getNodeName());
        }

        Map<String, Object> windowInfo = XmlUtils.xmlToMap(node);

        int rows = Integer.parseInt((String) windowInfo.get("@rows"));
        int columns = Integer.parseInt((String) windowInfo.get("@columns"));
        String id = (String) windowInfo.get("@id");
        int difficulty = Integer.parseInt((String) windowInfo.get("@difficulty"));

        Map<String, Object>[] cellsMap = XmlUtils.getMapArray(windowInfo, "cell");

        Cell[] cellsArray = Arrays.stream(cellsMap).map(stringObjectMap -> {
            String shade = (String) stringObjectMap.get("@shade");
            String color = (String) stringObjectMap.get("@color");
            return new Cell(
                    shade == null ? 0 : Integer.parseInt(shade),
                    color == null ? null : GlassColor.fromString(color)
            );
        }).toArray(Cell[]::new);

        Cell[][] cells = new Cell[rows][columns];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(cellsArray, i * columns, cells[i], 0, columns);
        }
        return new Window(difficulty, rows, columns, id, null, cells);


    }
}

