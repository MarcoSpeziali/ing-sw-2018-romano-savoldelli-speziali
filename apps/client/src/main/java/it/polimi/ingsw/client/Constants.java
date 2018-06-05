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
        RESOURCE_NAME("relative_path_to_resource");

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
        CLI_MODE("cli-mode");

        private final String optionName;

        ClientArguments(String optionName) {
            this.optionName = optionName;
        }

        @Override
        public String toString() {
            return this.optionName;
        }
    }

    public enum Threads {
        SOCKET_LISTENER("socket-listener");

        private final String name;

        public String getName() {
            return name;
        }

        Threads(String name) {
            this.name = name;
        }
    }
}
