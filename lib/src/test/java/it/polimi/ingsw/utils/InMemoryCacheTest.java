package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryCacheTest {

    @Test
    void testAddAndGet() {
        InMemoryCache<String, Float> cache = new InMemoryCache<>();
        cache.add("test", 15F);

        Assertions.assertNotNull(cache.get("test"));
        Assertions.assertEquals(15F, cache.get("test").floatValue());

        Assertions.assertNull(cache.get("test2"));
        Assertions.assertEquals(15.7F, cache.getOrDefault("test2", 15.7F).floatValue());
    }

    @Test
    void testContains() {
        InMemoryCache<String, Float> cache = new InMemoryCache<>();
        cache.add("test", 15F);

        Assertions.assertTrue(cache.contains("test"));
        Assertions.assertFalse(cache.contains("test2"));
    }

    @Test
    void testRemoveAndGet() {
        InMemoryCache<String, Float> cache = new InMemoryCache<>();
        cache.add("test", 15F);

        Assertions.assertTrue(cache.contains("test"));

        cache.remove("test");

        Assertions.assertFalse(cache.contains("test"));
    }

    @Test
    void testInvalidate() {
        InMemoryCache<String, Float> cache = new InMemoryCache<>();
        cache.add("test", 15F);
        cache.add("test2", 16F);
        cache.add("test3", 17F);
        cache.add("test4", 18F);
        cache.add("test5", 19F);

        Assertions.assertTrue(cache.contains("test"));
        Assertions.assertTrue(cache.contains("test2"));
        Assertions.assertTrue(cache.contains("test3"));
        Assertions.assertTrue(cache.contains("test4"));
        Assertions.assertTrue(cache.contains("test5"));

        cache.invalidate();

        Assertions.assertFalse(cache.contains("test"));
        Assertions.assertFalse(cache.contains("test2"));
        Assertions.assertFalse(cache.contains("test3"));
        Assertions.assertFalse(cache.contains("test4"));
        Assertions.assertFalse(cache.contains("test5"));
    }
}