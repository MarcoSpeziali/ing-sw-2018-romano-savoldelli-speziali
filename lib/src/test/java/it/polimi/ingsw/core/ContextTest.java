package it.polimi.ingsw.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextTest {

    @BeforeEach
    void setUpOnce() {
        Context ctx = Context.getSharedInstance();
        ctx.put("test_key", 1);
    }

    @Test
    void testSnapshotCreation() {
        Context.Snapshot snapshot = Context.getSharedInstance().snapshot("abcd");
        snapshot.put("snapshot_test", 11);

        Assertions.assertEquals(1, snapshot.get("test_key"));
        Assertions.assertEquals(11, snapshot.get("snapshot_test"));

        Assertions.assertEquals(11, Context.getSharedInstance().get("abcd::snapshot_test"));

        snapshot.revert();

        Assertions.assertNull(Context.getSharedInstance().get("abcd::snapshot_test"));
    }

    @Test
    void testSubSnapshotCreation() {
        Context.Snapshot snapshot = Context.getSharedInstance().snapshot("abcd");
        snapshot.put("snapshot_test", 11);

        Assertions.assertEquals(1, snapshot.get("test_key"));
        Assertions.assertEquals(11, snapshot.get("snapshot_test"));

        Assertions.assertEquals(11, Context.getSharedInstance().get("abcd::snapshot_test"));

        Context.Snapshot subSnapshot = snapshot.snapshot("efgh");
        subSnapshot.put("sub_snapshot_test", 111);

        Assertions.assertEquals(1, subSnapshot.get("test_key"));
        Assertions.assertEquals(11, subSnapshot.get("snapshot_test"));
        Assertions.assertEquals(111, subSnapshot.get("sub_snapshot_test"));

        Assertions.assertEquals(111, Context.getSharedInstance().get("abcd::efgh::sub_snapshot_test"));

        subSnapshot.put("snapshot_test", 12);
        Assertions.assertEquals(12, subSnapshot.get("snapshot_test"));
        Assertions.assertEquals(12, Context.getSharedInstance().get("abcd::efgh::snapshot_test"));

        snapshot.revert();

        Assertions.assertNull(Context.getSharedInstance().get("abcd::snapshot_test"));
        Assertions.assertNull(Context.getSharedInstance().get("abcd::efgh::sub_snapshot_test"));
    }
}