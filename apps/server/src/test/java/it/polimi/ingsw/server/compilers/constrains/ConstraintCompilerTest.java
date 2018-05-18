package it.polimi.ingsw.server.compilers.constrains;

import it.polimi.ingsw.server.compilers.constraints.ConstraintCompiler;
import it.polimi.ingsw.server.compilers.constraints.MalformedConstraintException;
import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.constraints.Constraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

class ConstraintCompilerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "<constraint id=\"test\">1 == 1</constraint>",
            "<constraint id=\"test\"><![CDATA[12 > 1]]></constraint>",
            "<constraint id=\"test\"><![CDATA[13.12 <= 13.12]]></constraint>",
            "<constraint id=\"test\">.014 == 0.014</constraint>",
            "<constraint id=\"test\">\"val1\" != \"val2\"</constraint>",
            "<constraint id=\"test\"><![CDATA[\"val\" < \"val1\"]]></constraint>",
            "<constraint id=\"test\">red != yellow</constraint>"
    })
    void compileTrue(String constraintString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintString));
        Document doc = builder.parse(is);

        Constraint constraint = ConstraintCompiler.compile(doc.getDocumentElement());

        Assertions.assertNotNull(constraint);
        Assertions.assertEquals("test", constraint.getId());
        Assertions.assertTrue(constraint.evaluate(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<constraint id=\"test\">1 != 1</constraint>",
            "<constraint id=\"test\"><![CDATA[12 <= 1]]></constraint>",
            "<constraint id=\"test\"><![CDATA[13.12 > 13.12]]></constraint>",
            "<constraint id=\"test\">.014 != 0.014</constraint>",
            "<constraint id=\"test\">\"val1\" == \"val2\"</constraint>",
            "<constraint id=\"test\"><![CDATA[\"val\" >= \"val1\"]]></constraint>",
            "<constraint id=\"test\">green == purple</constraint>"
    })
    void compileFalse(String constraintString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintString));
        Document doc = builder.parse(is);

        Constraint constraint = ConstraintCompiler.compile(doc.getDocumentElement());

        Assertions.assertNotNull(constraint);
        Assertions.assertEquals("test", constraint.getId());
        Assertions.assertFalse(constraint.evaluate(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<constraint id=\"test\">1</constraint>",
            "<constraint id=\"test\"></constraint>",
            "<constraint id=\"test\"><![CDATA[13.12 >< 13.12]]></constraint>",
            "<constraint id=\"test\">.014 != </constraint>",
            "<constraint id=\"test\">\"val1\" \"val2\"</constraint>"
    })
    void testMalformedConstraintException(String constraintString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintString));
        Document doc = builder.parse(is);

        Assertions.assertThrows(MalformedConstraintException.class, () -> {
            ConstraintCompiler.compile(doc.getDocumentElement()).evaluate(Context.getSharedInstance());
        });
    }

    @Test
    void testIllegalArgumentException() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader("<cxd id=\"test\"></cxd>"));
        Document doc = builder.parse(is);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ConstraintCompiler.compile(doc.getDocumentElement());
        });
    }
}