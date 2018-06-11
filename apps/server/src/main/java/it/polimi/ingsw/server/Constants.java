package it.polimi.ingsw.server;

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

        CACHE_FOLDER(java.nio.file.Paths.get(
                Paths.PROJECT_FOLDER.absolutePath,
                "cache"
        ).toAbsolutePath().toString()),

        COMPILATION_FOLDER(java.nio.file.Paths.get(
                Paths.CACHE_FOLDER.absolutePath,
                "compilations"
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

        @SuppressWarnings("squid:UnusedPrivateMethod")
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
        PUBLIC_CARDS("cards/public-cards.xml"),
        PRIVATE_CARDS("cards/private-cards.xml"),
        TOOL_CARDS("cards/tool-cards.xml"),
        WINDOWS("windows.xml"),
        ACTIONS_DIRECTIVES("directives/actions-directives.xml"),
        INSTRUCTIONS_DIRECTIVES("directives/instructions-directives.xml"),
        PREDICATES_DIRECTIVES("directives/predicates-directives.xml"),
        DEFAULT_SETTINGS("default_settings.xml");

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
            ClassLoader classLoader = ServerApp.class.getClassLoader();
            return it.polimi.ingsw.utils.io.Resources.getResource(classLoader, this.relativePath);
        }

        public static final Set<Resources> ALL = Collections.unmodifiableSet(EnumSet.allOf(Resources.class));
    }

    public enum ServerArguments {
        FORCE_COMPILATION("force-compilation"),
        LOG_LEVEL("log-level");

        private final String optionName;

        ServerArguments(String optionName) {
            this.optionName = optionName;
        }

        @Override
        public String toString() {
            return this.optionName;
        }
    }

    public enum Threads {
        ROOT("root"),
        SOCKET_LISTENER("socket-listener"),
        CLIENT_HANDLER("client-handler"),
        CLIENT_INPUT_HANDLER("client-input-listener");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        Threads(String name) {
            this.name = name;
        }
    }
    
    public enum LockTargets {
        LOBBY
    }
}
