package it.polimi.ingsw.server.compilers;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.utils.io.XMLUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class WindowCompilerTest {

    @Test
    void testOneCompilation() throws ParserConfigurationException, SAXException, IOException {
        String windowXmlString =
                "<window rows=\"4\" columns=\"5\" id=\"kaleidoscopic_dream\" sibling=\"firmitas\" name=\"windows.kaleidoscopic_dream\" difficulty=\"4\">" +
                        "    <cell color=\"yellow\" />" +
                        "    <cell color=\"blue\" />" +
                        "    <cell />" +
                        "    <cell />" +
                        "    <cell shade=\"1\" />" +

                        "    <cell color=\"green\" />" +
                        "    <cell />" +
                        "    <cell shade=\"5\"/>" +
                        "    <cell />" +
                        "    <cell shade=\"4\"/>" +

                        "    <cell shade=\"3\"/>" +
                        "    <cell />" +
                        "    <cell color=\"red\"/>" +
                        "    <cell />" +
                        "    <cell color =\"green\"/>" +

                        "    <cell shade=\"2\"/>" +
                        "    <cell />" +
                        "    <cell />" +
                        "    <cell color=\"blue\"/>" +
                        "    <cell color=\"yellow\"/>" +
                        "</window>";

        Window window = WindowCompiler.compile(XMLUtils.parseXmlString(windowXmlString));
        Assertions.assertEquals("kaleidoscopic_dream", window.getId());
        Assertions.assertNull(window.getSibling());

        testKaleidoscopicDream(window);
    }

    @Test
    void testTwoCompilation() throws ParserConfigurationException, SAXException, IOException {
        List<Window> windows = WindowCompiler.compileAll();
        Assertions.assertEquals(2, windows.size());

        Assertions.assertSame(windows.get(1), windows.get(0).getSibling());
        Assertions.assertSame(windows.get(0), windows.get(1).getSibling());

        testKaleidoscopicDream(windows.get(0));
        testVirmitas(windows.get(1));
    }

    void testKaleidoscopicDream(Window window) {
        Assertions.assertEquals(4, window.getDifficulty());

        Cell[][] cells = window.getCells();
        Assertions.assertNotNull(cells);
        Assertions.assertEquals(4, cells.length);
        Arrays.stream(cells)
                .forEach(row -> Assertions.assertEquals(5, row.length));

        Assertions.assertEquals(GlassColor.YELLOW, cells[0][0].getColor());
        Assertions.assertEquals(0, cells[0][0].getShade().intValue());
        Assertions.assertEquals(GlassColor.BLUE, cells[0][1].getColor());
        Assertions.assertEquals(0, cells[0][1].getShade().intValue());
        Assertions.assertNull(cells[0][2].getColor());
        Assertions.assertEquals(0, cells[0][2].getShade().intValue());
        Assertions.assertNull(cells[0][3].getColor());
        Assertions.assertEquals(0, cells[0][3].getShade().intValue());
        Assertions.assertNull(cells[0][4].getColor());
        Assertions.assertEquals(1, cells[0][4].getShade().intValue());

        Assertions.assertEquals(GlassColor.GREEN, cells[1][0].getColor());
        Assertions.assertEquals(0, cells[1][0].getShade().intValue());
        Assertions.assertNull(cells[1][1].getColor());
        Assertions.assertEquals(0, cells[1][1].getShade().intValue());
        Assertions.assertNull(cells[1][2].getColor());
        Assertions.assertEquals(5, cells[1][2].getShade().intValue());
        Assertions.assertNull(cells[1][3].getColor());
        Assertions.assertEquals(0, cells[1][3].getShade().intValue());
        Assertions.assertNull(cells[1][4].getColor());
        Assertions.assertEquals(4, cells[1][4].getShade().intValue());

        Assertions.assertNull(cells[2][0].getColor());
        Assertions.assertEquals(3, cells[2][0].getShade().intValue());
        Assertions.assertNull(cells[2][1].getColor());
        Assertions.assertEquals(0, cells[2][1].getShade().intValue());
        Assertions.assertEquals(GlassColor.RED, cells[2][2].getColor());
        Assertions.assertEquals(0, cells[2][2].getShade().intValue());
        Assertions.assertNull(cells[2][3].getColor());
        Assertions.assertEquals(0, cells[2][3].getShade().intValue());
        Assertions.assertEquals(GlassColor.GREEN, cells[2][4].getColor());
        Assertions.assertEquals(0, cells[2][4].getShade().intValue());

        Assertions.assertNull(cells[3][0].getColor());
        Assertions.assertEquals(2, cells[3][0].getShade().intValue());
        Assertions.assertNull(cells[3][1].getColor());
        Assertions.assertEquals(0, cells[3][1].getShade().intValue());
        Assertions.assertNull(cells[3][2].getColor());
        Assertions.assertEquals(0, cells[3][2].getShade().intValue());
        Assertions.assertEquals(GlassColor.BLUE, cells[3][3].getColor());
        Assertions.assertEquals(0, cells[3][3].getShade().intValue());
        Assertions.assertEquals(GlassColor.YELLOW, cells[3][4].getColor());
        Assertions.assertEquals(0, cells[3][4].getShade().intValue());
    }

    void testVirmitas(Window window) {
        Assertions.assertEquals(5, window.getDifficulty());

        Cell[][] cells = window.getCells();
        Assertions.assertNotNull(cells);
        Assertions.assertEquals(4, cells.length);
        Arrays.stream(cells)
                .forEach(row -> Assertions.assertEquals(5, row.length));

        Assertions.assertEquals(GlassColor.PURPLE, cells[0][0].getColor());
        Assertions.assertEquals(0, cells[0][0].getShade().intValue());
        Assertions.assertNull(cells[0][1].getColor());
        Assertions.assertEquals(6, cells[0][1].getShade().intValue());
        Assertions.assertNull(cells[0][2].getColor());
        Assertions.assertEquals(0, cells[0][2].getShade().intValue());
        Assertions.assertNull(cells[0][3].getColor());
        Assertions.assertEquals(0, cells[0][3].getShade().intValue());
        Assertions.assertNull(cells[0][4].getColor());
        Assertions.assertEquals(3, cells[0][4].getShade().intValue());

        Assertions.assertNull(cells[1][0].getColor());
        Assertions.assertEquals(5, cells[1][0].getShade().intValue());
        Assertions.assertEquals(GlassColor.PURPLE, cells[1][1].getColor());
        Assertions.assertEquals(0, cells[1][1].getShade().intValue());
        Assertions.assertNull(cells[1][2].getColor());
        Assertions.assertEquals(3, cells[1][2].getShade().intValue());
        Assertions.assertNull(cells[1][3].getColor());
        Assertions.assertEquals(0, cells[1][3].getShade().intValue());
        Assertions.assertNull(cells[1][4].getColor());
        Assertions.assertEquals(0, cells[1][4].getShade().intValue());

        Assertions.assertNull(cells[2][0].getColor());
        Assertions.assertEquals(0, cells[2][0].getShade().intValue());
        Assertions.assertNull(cells[2][1].getColor());
        Assertions.assertEquals(2, cells[2][1].getShade().intValue());
        Assertions.assertEquals(GlassColor.PURPLE, cells[2][2].getColor());
        Assertions.assertEquals(0, cells[2][2].getShade().intValue());
        Assertions.assertNull(cells[2][3].getColor());
        Assertions.assertEquals(1, cells[2][3].getShade().intValue());
        Assertions.assertNull(cells[2][4].getColor());
        Assertions.assertEquals(0, cells[2][4].getShade().intValue());

        Assertions.assertNull(cells[3][0].getColor());
        Assertions.assertEquals(0, cells[3][0].getShade().intValue());
        Assertions.assertNull(cells[3][1].getColor());
        Assertions.assertEquals(1, cells[3][1].getShade().intValue());
        Assertions.assertNull(cells[3][2].getColor());
        Assertions.assertEquals(5, cells[3][2].getShade().intValue());
        Assertions.assertEquals(GlassColor.PURPLE, cells[3][3].getColor());
        Assertions.assertEquals(0, cells[3][3].getShade().intValue());
        Assertions.assertNull(cells[3][4].getColor());
        Assertions.assertEquals(4, cells[3][4].getShade().intValue());
    }
}