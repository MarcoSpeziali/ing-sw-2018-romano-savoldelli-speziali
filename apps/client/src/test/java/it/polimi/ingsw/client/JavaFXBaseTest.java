package it.polimi.ingsw.client;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public abstract class JavaFXBaseTest {

    private static boolean jfxIsSetup;

    protected void doOnJavaFXThread(Runnable pRun) throws RuntimeException {
        if (!jfxIsSetup) {
            setupJavaFX();
            jfxIsSetup = true;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            pRun.run();
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    protected static void setupJavaFX() throws RuntimeException {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
