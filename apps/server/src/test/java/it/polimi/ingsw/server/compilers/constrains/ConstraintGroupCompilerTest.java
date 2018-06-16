package it.polimi.ingsw.server.compilers.constrains;

import it.polimi.ingsw.server.compilers.constraints.ConstraintGroupCompiler;
import it.polimi.ingsw.server.constraints.Constraint;
import it.polimi.ingsw.server.constraints.ConstraintGroup;
import it.polimi.ingsw.server.constraints.EvaluableConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

class ConstraintGroupCompilerTest {

    @Test
    void testSimpleTrue() throws ParserConfigurationException, IOException, SAXException {
        String constraintGroupString =
                "<constraint-group id=\"test_cg\">\n" +
                        "    <constraint>2 == 2</constraint>\n" +
                        "    <constraint><![CDATA[10.13 > 10.129999]]></constraint>\n" +
                        "    <constraint>null == null</constraint>\n" +
                        "    <constraint><![CDATA[\"test_val_a\" < \"test_val_b\"]]></constraint>\n" +
                        "</constraint-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintGroupString));
        Document doc = builder.parse(is);

        ConstraintGroup constraintGroup = ConstraintGroupCompiler.compile(doc.getDocumentElement());

        Assertions.assertNotNull(constraintGroup);
        Assertions.assertEquals("test_cg", constraintGroup.getId());

        List<EvaluableConstraint> innerConstraints = constraintGroup.getConstraints();
        Assertions.assertEquals(4, innerConstraints.size());

        for (EvaluableConstraint innerConstraint : innerConstraints) {
            Assertions.assertEquals(Constraint.class, innerConstraint.getClass());
            Assertions.assertNull(innerConstraint.getId());
        }

        Assertions.assertTrue(constraintGroup.evaluate(null));
    }

    @Test
    void testSimpleFalse() throws ParserConfigurationException, IOException, SAXException {
        String constraintGroupString =
                "<constraint-group id=\"test_cg\">\n" +
                        "    <constraint>2 == 2</constraint>\n" +
                        "    <constraint><![CDATA[10.13 > 10.129999]]></constraint>\n" +
                        "    <constraint>null != null</constraint>\n" +
                        "    <constraint><![CDATA[\"test_val_a\" < \"test_val_b\"]]></constraint>\n" +
                        "</constraint-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintGroupString));
        Document doc = builder.parse(is);

        ConstraintGroup constraintGroup = ConstraintGroupCompiler.compile(doc.getDocumentElement());

        Assertions.assertNotNull(constraintGroup);
        Assertions.assertEquals("test_cg", constraintGroup.getId());

        List<EvaluableConstraint> innerConstraints = constraintGroup.getConstraints();
        Assertions.assertEquals(4, innerConstraints.size());

        for (EvaluableConstraint innerConstraint : innerConstraints) {
            Assertions.assertEquals(Constraint.class, innerConstraint.getClass());
            Assertions.assertNull(innerConstraint.getId());
        }

        Assertions.assertFalse(constraintGroup.evaluate(null));
    }

    @Test
    void testComplex() throws ParserConfigurationException, IOException, SAXException {
        String constraintGroupString =
                "<constraint-group id=\"test_cg2\">\n" +
                        "    <constraint>2 == 2</constraint>\n" +
                        "    <constraint><![CDATA[10.13 > 10.129999]]></constraint>\n" +
                        "    <constraint>null == null</constraint>\n" +
                        "    <constraint><![CDATA[\"test_val_a\" < \"test_val_b\"]]></constraint>\n" +
                        "    <constraint-group>\n" +
                        "        <constraint><![CDATA[2 < 2.125]]></constraint>\n" +
                        "        <constraint-group>\n" +
                        "            <constraint>\"test_val_a\" != \"test_val_b\"</constraint>\n" +
                        "        </constraint-group>\n" +
                        "    </constraint-group>\n" +
                        "</constraint-group>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(constraintGroupString));
        Document doc = builder.parse(is);

        ConstraintGroup constraintGroup = ConstraintGroupCompiler.compile(doc.getDocumentElement());

        Assertions.assertNotNull(constraintGroup);
        Assertions.assertEquals("test_cg2", constraintGroup.getId());

        List<EvaluableConstraint> innerConstraints = constraintGroup.getConstraints();
        Assertions.assertEquals(5, innerConstraints.size());

        for (int i = 0; i < innerConstraints.size() - 1; i++) {
            EvaluableConstraint innerConstraint = innerConstraints.get(i);
            Assertions.assertEquals(Constraint.class, innerConstraint.getClass());
            Assertions.assertNull(innerConstraint.getId());
        }

        Assertions.assertEquals(ConstraintGroup.class, innerConstraints.get(4).getClass());
        Assertions.assertNull(innerConstraints.get(4).getId());

        List<EvaluableConstraint> innerInnerConstraints = ((ConstraintGroup) innerConstraints.get(4)).getConstraints();
        Assertions.assertEquals(2, innerInnerConstraints.size());

        Assertions.assertEquals(Constraint.class, innerInnerConstraints.get(0).getClass());
        Assertions.assertNull(innerInnerConstraints.get(0).getId());

        Assertions.assertEquals(ConstraintGroup.class, innerInnerConstraints.get(1).getClass());
        Assertions.assertNull(innerInnerConstraints.get(1).getId());

        List<EvaluableConstraint> innerInnerInnerConstraints = ((ConstraintGroup) innerInnerConstraints.get(1)).getConstraints();
        Assertions.assertEquals(1, innerInnerInnerConstraints.size());

        Assertions.assertEquals(Constraint.class, innerInnerInnerConstraints.get(0).getClass());
        Assertions.assertNull(innerInnerInnerConstraints.get(0).getId());

        Assertions.assertTrue(constraintGroup.evaluate(null));
    }

    @Test
    @SuppressWarnings("Duplicates")
    void testIllegalArgumentException() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader("<cxd id=\"test\"></cxd>"));
        Document doc = builder.parse(is);

        Assertions.assertThrows(IllegalArgumentException.class, () -> ConstraintGroupCompiler.compile(doc.getDocumentElement()));
    }
}