package it.polimi.ingsw.client;

import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Constants {
    private Constants() {}

    public enum Paths {
        PROJECT_FOLDER(java.nio.file.Paths.get(
                System.getProperty("user.home"),
                ".sagrada"
        ).toAbsolutePath().toString()),

        LOG_FOLDER(java.nio.file.Paths.get(
                Paths.PROJECT_FOLDER.absolutePath,
                "logs"
        ).toAbsolutePath().toString()),

        SETTINGS_PATH(java.nio.file.Paths.get(
                Paths.PROJECT_FOLDER.absolutePath,
                "settings.xml"
        ).toAbsolutePath().toString()),

        SSH_KEY_PATH(java.nio.file.Paths.get(
                System.getProperty("user.home"),
                ".ssh",
                "id_rsa"
        ).toAbsolutePath().toString());

        private final String absolutePath;

        Paths(String absolutePath) {
            this.absolutePath = absolutePath;
        }

        /**
         * @return the absolute path
         */
        public String getAbsolutePath() {
            return this.absolutePath;
        }
    }

    public enum Resources {
        DEFAULT_SETTINGS("default_settings.xml"),
        IDRA_PUBLIC_KEY("idra_rsa.der"),
        CELL_ONE("cells/cell_one.png"),
        CELL_TWO("cells/cell_two.png"),
        CELL_THREE("cells/cell_three.png"),
        CELL_FOUR("cells/cell_four.png"),
        CELL_FIVE("cells/cell_five.png"),
        CELL_SIX("cells/cell_six.png"),
        SIGN_UP_FXML("FXML/SignUp.fxml"),
        SIGN_IN_FXML("FXML/SignIn.fxml"),
        START_SCREEN_FXML("FXML/StartScreen.fxml");

        private final String relativePath;

        Resources(String relativePath) {
            this.relativePath = relativePath;
        }

        /**
         * @return the relative path of the resource
         */
        public String getRelativePath() {
            return relativePath;
        }

        /**
         * @return the {@link URL} that locates the resource or {@code null} if the resource could not be located
         */
        public URL getURL() {
            ClassLoader classLoader = ClientApp.class.getClassLoader();
            return it.polimi.ingsw.utils.io.Resources.getResource(classLoader, this.relativePath);
        }

        public static final Set<Resources> ALL = Collections.unmodifiableSet(EnumSet.allOf(Resources.class));
    }

    public enum ClientArguments {
        CLI_MODE("cli");

        private final String optionName;

        ClientArguments(String optionName) {
            this.optionName = optionName;
        }

        @Override
        public String toString() {
            return this.optionName;
        }
    }
}
