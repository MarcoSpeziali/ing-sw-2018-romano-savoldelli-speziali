package it.polimi.ingsw.server;

import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings({"squid:S00116", "squid:S1170", "squid:S00101"})
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
        PUBLIC_CARDS("public-cards.xml"),
        PRIVATE_CARDS("private-cards.xml"),
        TOOL_CARDS("tool-cards.xml"),
        WINDOWS("windows.xml"),
        ACTIONS_DIRECTIVES("actions-directives.xml"),
        INSTRUCTIONS_DIRECTIVES("instructions-directives.xml"),
        PREDICATES_DIRECTIVES("predicates-directives.xml");

        private final String relativePath;

        @SuppressWarnings("squid:UnusedPrivateMethod")
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
            return classLoader.getResource(this.relativePath);
        }

        public static final Set<Resources> ALL = Collections.unmodifiableSet(EnumSet.allOf(Resources.class));
    }

    public enum ServerArgument {
        FORCE_COMPILATION("force-compilation"),
        LOG_LEVEL("log-level");

        private final String optionName;

        ServerArgument(String optionName) {
            this.optionName = optionName;
        }

        @Override
        public String toString() {
            return this.optionName;
        }
    }
}
