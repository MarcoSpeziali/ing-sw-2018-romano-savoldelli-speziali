package it.polimi.ingsw.utils.io.json;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class JSONSerializationTest {

    @Test
    void testDeserializationFromFields() {
        A a = new A();
        a.bObj = new B();
        a.bObj.b = 21;
        a.e = TimeUnit.HOURS;

        JSONObject jsonObject = a.serialize();

        Assertions.assertEquals(0, jsonObject.getInt("i"));
        Assertions.assertEquals(0, jsonObject.getLong("l"));
        Assertions.assertEquals(0, jsonObject.getFloat("f"));
        Assertions.assertEquals(0, jsonObject.getDouble("d"));
        Assertions.assertFalse(jsonObject.has("ai"));
        Assertions.assertEquals(TimeUnit.HOURS, jsonObject.getEnum(TimeUnit.class, "e"));
        Assertions.assertEquals(21, jsonObject.getJSONObject("B").getInt("b"));
    }

    @Test
    void testDeserializationFromMethodsAndFields() {
        C c = new C();

        JSONObject jsonObject = c.serialize();

        Assertions.assertEquals(21, jsonObject.getJSONObject("b").getInt("b"));
    }
}

class A implements JSONSerializable {

    private static final long serialVersionUID = 231004061581200830L;

    @JSONElement("i")
    public int i;
    @JSONElement("l")
    public long l;
    @JSONElement("f")
    public float f;
    @JSONElement("d")
    public double d;
    @JSONElement("ia")
    public int[] ia;
    @JSONElement("b")
    public boolean b;
    @JSONElement("e")
    public TimeUnit e;
    @JSONElement("B")
    public B bObj;
}

class B implements JSONSerializable {
    private static final long serialVersionUID = -7371725573595893622L;

    @JSONElement("b")
    public int b;
}

class C implements JSONSerializable {

    private static final long serialVersionUID = 7514344880602229061L;

    @JSONElement("b")
    public B b = new B();

    @JSONElement("b")
    public B getB() {
        B b = new B();
        b.b = 21;

        return b;
    }
}