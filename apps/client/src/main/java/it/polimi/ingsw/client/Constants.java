package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.text.LocalizedString;

import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public class Constants {
    private Constants() {
    }

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
        IDRA_PUBLIC_KEY("public.der"),

        CELL_ONE("images/cells/cell_one.png"),
        CELL_TWO("images/cells/cell_two.png"),
        CELL_THREE("images/cells/cell_three.png"),
        CELL_FOUR("images/cells/cell_four.png"),
        CELL_FIVE("images/cells/cell_five.png"),
        CELL_SIX("images/cells/cell_six.png"),

        SIGN_UP_FXML("FXML/Window/SignUp.fxml"),
        SIGN_IN_FXML("FXML/Window/SignIn.fxml"),
        START_SCREEN_FXML("FXML/Window/StartScreen.fxml"),
        SETTINGS_FXML("FXML/Window/Settings.fxml"),
        LOBBY("FXML/Window/Lobby.fxml");

        public static final Set<Resources> ALL = Collections.unmodifiableSet(EnumSet.allOf(Resources.class));
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
            ClassLoader classLoader = SagradaGUI.class.getClassLoader();
            return it.polimi.ingsw.utils.io.Resources.getResource(classLoader, this.relativePath);
        }
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

    @SuppressWarnings("SpellCheckingInspection")
    public enum Locales {
        ENGLISH(Locale.ENGLISH),
        ITALIANO(Locale.ITALIAN);

        private final Locale locale;

        Locales(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return locale;
        }
    }

    public enum Protocols {
        SOCKETS,
        RMI
    }

    public static final class Strings {
        public static final String WELCOME_LABEL_TEXT = "welcome_label.text";

        public static final String CONNECTION_ERROR_TITLE = "connection_error.title";
        public static final String CONNECTION_ERROR_HEADER_TEXT = "connection_error.header_text";
        public static final String CONNECTION_ERROR_CONTENT_TEXT = "connection_error.content_text";

        public static final String SIGN_IN_ACCESS_DENIED_TITLE = "sign_in.access_denied.title";
        public static final String SIGN_IN_ACCESS_DENIED_HEADER_TEXT = "sign_in.access_denied.header_text";
        public static final String SIGN_IN_ACCESS_DENIED_CONTEXT_TEXT = "sign_in.access_denied.content_text";
        public static final String SIGN_IN_USERNAME_FIELD_PROMPT_TEXT = "sign_in.username_field.prompt_text";
        @SuppressWarnings("squid:S2068") // SonarLint = <3
        public static final String SIGN_IN_PASSWORD_FIELD_PROMPT_TEXT = "sign_in.password_field.prompt_text";
        public static final String SIGN_IN_REGISTER_LINK_TEXT = "sign_in.register_link.text";
        public static final String SIGN_IN_BACK_BUTTON_TEXT = "sign_in.back_button.text";
        public static final String SIGN_IN_SING_IN_NOW_LABEL_TEXT = "sign_in.sign_in_now_label.text";
        public static final String SIGN_IN_SIGN_IN_BUTTON_TEXT = "sign_in.sign_in_button.text";

        public static final String SETTINGS_TITLE = "settings.title";
        public static final String SETTINGS_SAVE_BUTTON_TEXT = "settings.save_button.text";
        public static final String SETTINGS_BACK_BUTTON_TEXT = "settings.back_button.text";
        public static final String SETTINGS_CONNECTION_TYPE_LABEL_TEXT = "settings.connection_type_label.text";
        public static final String SETTINGS_FULLSCREEN_TOGGLE_BUTTON_TEXT = "settings.full_screen_toggle_button.text";

        private Strings() {
        }

        public static String toLocalized(String string) {
            return new LocalizedString(string).toString();
        }
    }

}
