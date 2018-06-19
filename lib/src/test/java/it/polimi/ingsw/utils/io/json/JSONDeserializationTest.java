package it.polimi.ingsw.utils.io.json;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class JSONDeserializationTest {

    @Test
    void testDeserializationWithDesignatedConstructor() {
        JSONObject rootObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("i", 1);
        jsonObject.put("l", 12L);
        jsonObject.put("f", 12.5F);
        jsonObject.put("d", 15.789D);
        jsonObject.put("ia", new int[] {1, 2, 3, 4});
        jsonObject.put("b", false);
        jsonObject.put("e", TimeUnit.DAYS);
        rootObject.put("x", jsonObject);

        X x = JSONSerializable.deserialize(X.class, rootObject);

        Assertions.assertEquals(1, x.i);
        Assertions.assertEquals(12L, x.l);
        Assertions.assertEquals(12.5F, x.f);
        Assertions.assertEquals(15.789D, x.d);
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 4}, x.ia);
        Assertions.assertFalse(x.b);
        Assertions.assertEquals(TimeUnit.DAYS, x.e);
    }

    @Test
    void testDeserializationWithDefaultConstructor() {
        JSONObject rootObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("i", 1);
        jsonObject.put("l", 12L);
        jsonObject.put("f", 12.5F);
        jsonObject.put("d", 15.789D);
        jsonObject.put("ia", new int[] {1, 2, 3, 4});
        jsonObject.put("b", false);
        jsonObject.put("e", TimeUnit.DAYS);
        rootObject.put("y", jsonObject);

        Y y = JSONSerializable.deserialize(Y.class, rootObject);

        Assertions.assertEquals(1, y.i);
        Assertions.assertEquals(12L, y.l);
        Assertions.assertEquals(12.5F, y.f);
        Assertions.assertEquals(15.789D, y.d);
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 4}, y.ia);
        Assertions.assertFalse(y.b);
        Assertions.assertEquals(TimeUnit.DAYS, y.e);
    }

    @Test
    void testDeserializationWithDefaultConstructorAndOptionalRoot() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("i", 1);
        jsonObject.put("l", 12L);
        jsonObject.put("f", 12.5F);
        jsonObject.put("d", 15.789D);
        jsonObject.put("ia", new int[] {1, 2, 3, 4});
        jsonObject.put("b", false);
        jsonObject.put("e", TimeUnit.DAYS);

        Y y = JSONSerializable.deserialize(Y.class, jsonObject);

        Assertions.assertEquals(1, y.i);
        Assertions.assertEquals(12L, y.l);
        Assertions.assertEquals(12.5F, y.f);
        Assertions.assertEquals(15.789D, y.d);
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 4}, y.ia);
        Assertions.assertFalse(y.b);
        Assertions.assertEquals(TimeUnit.DAYS, y.e);
    }

    @Test
    void testNestedDeserializationWithDefaultConstructor() {
        JSONObject jsonObject = new JSONObject();
        JSONObject y = new JSONObject();
        y.put("i", 1);
        y.put("l", 12L);
        y.put("f", 12.5F);
        y.put("d", 15.789D);
        y.put("ia", new int[] {1, 2, 3, 4});
        y.put("b", false);
        y.put("e", TimeUnit.DAYS);
        jsonObject.put("y", y);

        JSONObject x1 = new JSONObject();
        x1.put("i", 1);
        x1.put("l", 12L);
        x1.put("f", 12.5F);
        x1.put("d", 15.789D);
        x1.putOpt("ia", null);
        x1.put("b", true);
        x1.put("e", TimeUnit.MILLISECONDS);

        JSONObject x2 = new JSONObject();
        x2.put("i", 1);
        x2.put("l", 12L);
        x2.put("f", 12.5F);
        x2.put("d", 15.789D);
        x2.putOpt("ia", null);
        x2.put("b", false);
        x2.put("e", TimeUnit.MICROSECONDS);

        JSONObject x1Root = new JSONObject();
        x1Root.put("x", x1);

        JSONObject x2Root = new JSONObject();
        x2Root.put("x", x2);

        jsonObject.put("xes", new JSONObject[] { x1Root, x2Root });

        Z z = JSONSerializable.deserialize(Z.class, jsonObject);

        Assertions.assertEquals(1, z.y.i);
        Assertions.assertEquals(12L, z.y.l);
        Assertions.assertEquals(12.5F, z.y.f);
        Assertions.assertEquals(15.789D, z.y.d);
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 4}, z.y.ia);
        Assertions.assertFalse(z.y.b);
        Assertions.assertEquals(TimeUnit.DAYS, z.y.e);

        Assertions.assertEquals(1, z.xes[0].i);
        Assertions.assertEquals(12L, z.xes[0].l);
        Assertions.assertEquals(12.5F, z.xes[0].f);
        Assertions.assertEquals(15.789D, z.xes[0].d);
        Assertions.assertNull(z.xes[0].ia);
        Assertions.assertTrue(z.xes[0].b);
        Assertions.assertEquals(TimeUnit.MILLISECONDS, z.xes[0].e);

        Assertions.assertEquals(1, z.xes[1].i);
        Assertions.assertEquals(12L, z.xes[1].l);
        Assertions.assertEquals(12.5F, z.xes[1].f);
        Assertions.assertEquals(15.789D, z.xes[1].d);
        Assertions.assertNull(z.xes[1].ia);
        Assertions.assertFalse(z.xes[1].b);
        Assertions.assertEquals(TimeUnit.MICROSECONDS, z.xes[1].e);
    }

    @Test
    void testNestedDeserializationWithDesignatedConstructor() {
        JSONObject jsonObject = new JSONObject();
        JSONObject y = new JSONObject();
        y.put("i", 1);
        y.put("l", 12L);
        y.put("f", 12.5F);
        y.put("d", 15.789D);
        y.put("ia", new int[] {1, 2, 3, 4});
        y.put("b", false);
        y.put("e", TimeUnit.DAYS);
        jsonObject.put("y", y);

        JSONObject x1 = new JSONObject();
        x1.put("i", 1);
        x1.put("l", 12L);
        x1.put("f", 12.5F);
        x1.put("d", 15.789D);
        x1.putOpt("ia", null);
        x1.put("b", true);
        x1.put("e", TimeUnit.MILLISECONDS);

        JSONObject x2 = new JSONObject();
        x2.put("i", 1);
        x2.put("l", 12L);
        x2.put("f", 12.5F);
        x2.put("d", 15.789D);
        x2.putOpt("ia", null);
        x2.put("b", false);
        x2.put("e", TimeUnit.MICROSECONDS);

        JSONObject x1Root = new JSONObject();
        x1Root.put("x", x1);

        JSONObject x2Root = new JSONObject();
        x2Root.put("x", x2);

        jsonObject.put("xes", new JSONObject[] { x1Root, x2Root });

        W w = JSONSerializable.deserialize(W.class, jsonObject);

        Assertions.assertEquals(1, w.y.i);
        Assertions.assertEquals(12L, w.y.l);
        Assertions.assertEquals(12.5F, w.y.f);
        Assertions.assertEquals(15.789D, w.y.d);
        Assertions.assertArrayEquals(new int[] {1, 2, 3, 4}, w.y.ia);
        Assertions.assertFalse(w.y.b);
        Assertions.assertEquals(TimeUnit.DAYS, w.y.e);

        Assertions.assertEquals(1, w.xes[0].i);
        Assertions.assertEquals(12L, w.xes[0].l);
        Assertions.assertEquals(12.5F, w.xes[0].f);
        Assertions.assertEquals(15.789D, w.xes[0].d);
        Assertions.assertNull(w.xes[0].ia);
        Assertions.assertTrue(w.xes[0].b);
        Assertions.assertEquals(TimeUnit.MILLISECONDS, w.xes[0].e);

        Assertions.assertEquals(1, w.xes[1].i);
        Assertions.assertEquals(12L, w.xes[1].l);
        Assertions.assertEquals(12.5F, w.xes[1].f);
        Assertions.assertEquals(15.789D, w.xes[1].d);
        Assertions.assertNull(w.xes[1].ia);
        Assertions.assertFalse(w.xes[1].b);
        Assertions.assertEquals(TimeUnit.MICROSECONDS, w.xes[1].e);
    }
}

@JSONRootElement("x")
class X implements JSONSerializable {

    private static final long serialVersionUID = 2682796955915149295L;

    public final int i;
    public final long l;
    public final float f;
    public final double d;
    public final int[] ia;
    public final boolean b;
    public final TimeUnit e;

    @JSONDesignatedConstructor
    X (
            @JSONElement("i") int i,
            @JSONElement("l") long l,
            @JSONElement("f") float f,
            @JSONElement("d") double d,
            @JSONElement("ia") int[] ia,
            @JSONElement("b") boolean b,
            @JSONElement("e") TimeUnit e
    ) {
        this.i = i;
        this.l = l;
        this.f = f;
        this.d = d;
        this.ia = ia;
        this.b = b;
        this.e = e;
    }
}

@JSONRootElement(value = "y", isOptional = true)
class Y implements JSONSerializable {

    private static final long serialVersionUID = 2682796955915149295L;

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
}

class Z implements JSONSerializable {

    private static final long serialVersionUID = 2682796955915149295L;

    @JSONElement("y")
    public Y y;
    @JSONElement("xes")
    public X[] xes;

    Z () {
    }
}

class W implements JSONSerializable {

    private static final long serialVersionUID = 2682796955915149295L;

    public Y y;
    public X[] xes;
    public Z[] zs;

    @JSONDesignatedConstructor
    W (
            @JSONElement("y") Y y,
            @JSONElement("xes") X[] xes,
            @JSONElement("zs") Z[] zs
    ) {
        this.y = y;
        this.xes = xes;
        this.zs = zs;
    }
}