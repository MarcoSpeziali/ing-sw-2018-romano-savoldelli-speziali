package it.polimi.ingsw.server;

import java.net.URISyntaxException;
import java.net.URL;
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
        WINDOWS("window.xml"),
        ACTIONS_DIRECTIVES("directives/actions-directives.xml"),
        INSTRUCTIONS_DIRECTIVES("directives/instructions-directives.xml"),
        PREDICATES_DIRECTIVES("directives/predicates-directives.xml");

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
         * @return the absolute path of the resource or {@code null} if the resource could not be located
         */
        public String getAbsolutePath() {
            ClassLoader classLoader = Resources.class.getClassLoader();
            URL url = classLoader.getResource(this.relativePath);

            if (url == null) {
                return null;
            }

            try {
                return url.toURI().getPath();
            } catch (URISyntaxException e) {
                return null;
            }
        }

        public static final Set<Resources> ALL = EnumSet.allOf(Resources.class);
    }
}
