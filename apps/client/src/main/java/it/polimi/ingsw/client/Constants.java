package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.scene.input.DataFormat;

import java.net.URL;
import java.util.*;

public class Constants {
    private Constants() {
    }

    public static DataFormat iDieFormat = new DataFormat("IDIE");

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

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public enum Resources {
        IDRA_PUBLIC_KEY("public.der"),

        DICE_CURSOR("cursors/dice.gif"),
        STOP_CURSOR("cursors/stop.gif"),
        WARNING_CURSOR("cursors/warning.gif"),

        CELL_1("images/cells/cell_one.png"),
        CELL_2("images/cells/cell_two.png"),
        CELL_3("images/cells/cell_three.png"),
        CELL_4("images/cells/cell_four.png"),
        CELL_5("images/cells/cell_five.png"),
        CELL_6("images/cells/cell_six.png"),

        DIE_1("images/dice/dice_one.png"),
        DIE_2("images/dice/dice_two.png"),
        DIE_3("images/dice/dice_three.png"),
        DIE_4("images/dice/dice_four.png"),
        DIE_5("images/dice/dice_five.png"),
        DIE_6("images/dice/dice_six.png"),

        SIGN_UP_FXML("FXML/Window/SignUp.fxml"),
        SIGN_IN_FXML("FXML/Window/SignIn.fxml"),
        START_SCREEN_FXML("FXML/Window/StartScreen.fxml"),
        SETTINGS_FXML("FXML/Window/Settings.fxml"),
        LOBBY_FXML("FXML/Window/Lobby.fxml"),
        MATCH_FXML("FXML/Window/Match.fxml"),
        DIE_VIEW_FXML("FXML/DieView.fxml"),
        CELL_VIEW_FXML("FXML/CellView.fxml"),
        WINDOW_VIEW_FXML("FXML/WindowView.fxml"),
        DRAFTPOOL_VIEW_FXML("FXML/DraftPoolView.fxml"),
        ROUNDTRACK_VIEW_FXML("FXML/RoundTrackView.fxml"),
        TOOL_CARD_VIEW_FXML("FXML/ToolCardView.fxml"),
        OBJECTIVE_CARD_VIEW_FXML("FXML/ObjectiveCardView.fxml"),
        RESULTS_FXML("FXML/Window/Results.fxml"),

        TOOL_CARD_GROZING_PLIERS("images/toolCards/grozing_pliers.jpg"),
        TOOL_CARD_EGLOMISE_BRUSH("images/toolCards/eglomise_brush.jpg"),
        TOOL_CARD_COPPER_FOIL_BURNISHER("images/toolCards/copper_foil_burnisher.jpg"),
        TOOL_CARD_LATHEKIN("images/toolCards/lathekin.jpg"),
        TOOL_CARD_LENS_CUTTER("images/toolCards/lens_cutter.jpg"),
        TOOL_CARD_FLUX_BRUSH("images/toolCards/flux_brush.jpg"),
        TOOL_CARD_GLAZING_HAMMER("images/toolCards/glazing_hammer.jpg"),
        TOOL_CARD_RUNNING_PLIERS("images/toolCards/running_pliers.jpg"),
        TOOL_CARD_CORK_BACKED_STRAINGHTEDGE("images/toolCards/cork_backed_strainghtedge.jpg"),
        TOOL_CARD_GRINDING_STONE("images/toolCards/grinding_stone.jpg"),
        TOOL_CARD_FLUX_REMOVER("images/toolCards/flux_remover.jpg"),
        TOOL_CARD_TAP_WHEEL("images/toolCards/tap_wheel.jpg"),

        OBJECTIVE_CARD_BLUE_SHADE("images/objectiveCards/blue_shade.jpg"),
        OBJECTIVE_CARD_GREEN_SHADE("images/objectiveCards/green_shade.jpg"),
        OBJECTIVE_CARD_PURPLE_SHADE("images/objectiveCards/purple_shade.jpg"),
        OBJECTIVE_CARD_RED_SHADE("images/objectiveCards/red_shade.jpg"),
        OBJECTIVE_CARD_YELLOW_SHADE("images/objectiveCards/yellow_shade.jpg"),

        OBJECTIVE_CARD_COLOR_DIAGONALS("images/objectiveCards/color_diagonals.jpg"),

        OBJECTIVE_CARD_COLOR_VARIETY("images/objectiveCards/color_variety.jpg"),
        OBJECTIVE_CARD_SHADE_VARIETY("images/objectiveCards/shade_variety.jpg"),

        OBJECTIVE_CARD_ROW_COLOR_VARIETY("images/objectiveCards/row_color_variety.jpg"),
        OBJECTIVE_CARD_COLUMN_COLOR_VARIETY("images/objectiveCards/column_color_variety.jpg"),

        OBJECTIVE_CARD_ROW_SHADE_VARIETY("images/objectiveCards/row_shade_variety.jpg"),
        OBJECTIVE_CARD_COLUMN_SHADE_VARIETY("images/objectiveCards/column_shade_variety.jpg"),

        OBJECTIVE_CARD_LIGHT_SHADES("images/objectiveCards/light_shades.jpg"),
        OBJECTIVE_CARD_MEDIUM_SHADES("images/objectiveCards/medium_shades.jpg"),
        OBJECTIVE_CARD_DEEP_SHADES("images/objectiveCards/deep_shades.jpg");

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
        DEFAULT(Locale.ENGLISH),
        ENGLISH(Locale.ENGLISH),
        ITALIANO(Locale.ITALIAN);

        private final Locale locale;

        Locales(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            if (this.locale == null) {
                return Locale.getDefault();
            }

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
        public static final String SIGN_IN_ALREADY_EXISTS_TITLE = "sign_in.already_exists.title";
        public static final String SIGN_IN_ALREADY_EXISTS_CONTEXT_TEXT = "sign_in.already_exists.content_text";
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
        public static final String SETTINGS_LANGUAGE_LABEL_TEXT = "settings.language_label.text";

        public static final String LOBBY_TITLE = "lobby.title";
        public static final String LOBBY_SECONDS_TEXT_LABEL_TEXT = "lobby.seconds_label_text.text";
        public static final String LOBBY_WAITING_FOR_PLAYERS_LABEL_TEXT = "lobby.waiting_for_players_label.text";
        public static final String LOBBY_BACK_BUTTON_TEXT = "lobby.back_button.text";

        public static final String SIGN_UP_ALREADY_EXISTS_TITLE = "sign_up.already_exists.title";
        public static final String SIGN_UP_ALREADY_EXISTS_HEADER_TEXT = "sign_up.already_exists.header_text";
        public static final String SIGN_UP_ALREADY_EXISTS_CONTENT_TEXT = "sign_up.already_exists.content_text";
        public static final String SIGN_UP_CREDENTIALS_PROPERTIES_ERROR_TITLE = "sign_up.credentials_properties_error.title";
        public static final String SIGN_UP_CREDENTIAL_PROPERTIES_ERROR_CONTENT_TEXT = "sign_up.credential_properties_error.content_text";
        public static final String SIGN_UP_MATCH_FAILED_TITLE = "sign_up.match_failed.title";
        public static final String SIGN_UP_MATCH_FAILED_CONTENT_TEXT = "sign_up.match_failed.content_text";
        public static final String SIGN_UP_USER_PROPERTIES_ERROR_TITLE = "sign_up.user_properties_error.title";
        public static final String SIGN_UP_USER_PROPERTIES_ERROR_CONTENT_TEXT = "sign_up.user_properties_error.content_text";

        public static final String START_SCREEN_PLAY_BUTTON_TEXT = "start_screen.play_button.text";
        public static final String START_SCREEN_SETTINGS_BUTTON_TEXT = "start_screen.settings_button.text";
        public static final String START_SCREEN_EXIT_BUTTON_TEXT = "start_screen.exit_button.text";

        public static final String TOOL_CARD_GROZING_PLIERS_TITLE = "tool_card.grozing_pliers.title";
        public static final String TOOL_CARD_GROZING_PLIERS_EFFECT = "tool_card.grozing_pliers.effect";
        public static final String TOOL_CARD_EGLOMISE_BRUSH_TITLE = "tool_card.eglomise_brush.title";
        public static final String TOOL_CARD_EGLOMISE_BRUSH_EFFECT = "tool_card.eglomise_brush.effect";
        public static final String TOOL_CARD_COPPER_FOIL_BURNISHER_TITLE = "tool_card.copper_foil_burnisher.title";
        public static final String TOOL_CARD_COPPER_FOIL_BURNISHER_EFFECT = "tool_card.copper_foil_burnisher.effect";
        public static final String TOOL_CARD_LATHEKIN_TITLE = "tool_card.lathekin.title";
        public static final String TOOL_CARD_LATHEKIN_EFFECT = "tool_card.lathekin.effect";
        public static final String TOOL_CARD_LENS_CUTTER_TITLE = "tool_card.lens_cutter.title";
        public static final String TOOL_CARD_LENS_CUTTER_EFFECT = "tool_card.lens_cutter.effect";
        public static final String TOOL_CARD_FLUX_BRUSH_TITLE = "tool_card.flux_brush.title";
        public static final String TOOL_CARD_FLUX_BRUSH_EFFECT = "tool_card.flux_brush.effect";
        public static final String TOOL_CARD_GLAZING_HAMMER_TITLE = "tool_card.glazing_hammer.title";
        public static final String TOOL_CARD_GLAZING_HAMMER_EFFECT = "tool_card.glazing_hammer.effect";
        public static final String TOOL_CARD_RUNNING_PLIERS_TITLE = "tool_card.running_pliers.title";
        public static final String TOOL_CARD_RUNNING_PLIERS_EFFECT = "tool_card.running_pliers.effect";
        public static final String TOOL_CARD_CORK_BACKED_STRAINGHTEDGE_TITLE = "tool_card.cork_backed_strainghtedge.title";
        public static final String TOOL_CARD_CORK_BACKED_STRAINGHTEDGE_EFFECT = "tool_card.cork_backed_strainghtedge.effect";
        public static final String TOOL_CARD_GRINDING_STONE_TITLE = "tool_card.grinding_stone.title";
        public static final String TOOL_CARD_GRINDING_STONE_EFFECT = "tool_card.grinding_stone.effect";
        public static final String TOOL_CARD_FLUX_REMOVER_TITLE = "tool_card.flux_remover.title";
        public static final String TOOL_CARD_FLUX_REMOVER_EFFECT = "tool_card.flux_remover.effect";
        public static final String TOOL_CARD_TAP_WHEEL_TITLE = "tool_card.tap_wheel.title";
        public static final String TOOL_CARD_TAP_WHEEL_EFFECT = "tool_card.tap_wheel.effect";
        public static final String TOOL_CARD_BAG_TEXT = "tool_card.bag.text";
        public static final String TOOL_CARD_DRAFT = "tool_card.draft.text";

        public static final String OBJECTIVE_CARD_BLUE_SHADE_TITLE = "objective_card.blue_shade.title";
        public static final String OBJECTIVE_CARD_BLUE_SHADE_DESCRIPTION = "objective_card.blue_shade.description";
        public static final String OBJECTIVE_CARD_COLOR_DIAGONALS_TITLE = "objective_card.color_diagonals.title";
        public static final String OBJECTIVE_CARD_COLOR_DIAGONALS_DESCRIPTION = "objective_card.color_diagonals.description";
        public static final String OBJECTIVE_CARD_COLOR_VARIETY_TITLE = "objective_card.color_variety.title";
        public static final String OBJECTIVE_CARD_COLOR_VARIETY_DESCRIPTION = "objective_card.color_variety.description";
        public static final String OBJECTIVE_CARD_COLUMN_SHADE_VARIETY_TITLE = "objective_card.column_color_variety.title";
        public static final String OBJECTIVE_CARD_COLUMN_SHADE_VARIETY_DESCRIPTION = "objective_card.column_color_variety.description";
        public static final String OBJECTIVE_CARD_COLUMN_COLOR_VARIETY_TITLE = "objective_card.column_shade_variety.title";
        public static final String OBJECTIVE_CARD_COLUMN_COLOR_VARIETY_DESCRIPTION = "objective_card.column_shade_variety.description";
        public static final String OBJECTIVE_CARD_DEEP_SHADES_TITLE = "objective_card.deep_shades.title";
        public static final String OBJECTIVE_CARD_DEEP_SHADES_DESCRIPTION = "objective_card.deep_shades.description";
        public static final String OBJECTIVE_CARD_GREEN_SHADE_TITLE = "objective_card.green_shade.title";
        public static final String OBJECTIVE_CARD_GREEN_SHADE_DESCRIPTION = "objective_card.green_shade.description";
        public static final String OBJECTIVE_CARD_LIGHT_SHADES_TITLE = "objective_card.light_shades.title";
        public static final String OBJECTIVE_CARD_LIGHT_SHADES_DESCRIPTION = "objective_card.light_shades.description";
        public static final String OBJECTIVE_CARD_MEDIUM_SHADES_TITLE = "objective_card.medium_shades.title";
        public static final String OBJECTIVE_CARD_MEDIUM_SHADES_DESCRIPTION = "objective_card.medium_shades.description";
        public static final String OBJECTIVE_CARD_PURPLE_SHADE_TITLE = "objective_card.purple_shade.title";
        public static final String OBJECTIVE_CARD_PURPLE_SHADE_DESCRIPTION = "objective_card.purple_shade.description";
        public static final String OBJECTIVE_CARD_RED_SHADE_TITLE = "objective_card.red_shade.title";
        public static final String OBJECTIVE_CARD_RED_SHADE_DESCRIPTION = "objective_card.red_shade.description";
        public static final String OBJECTIVE_CARD_ROW_COLOR_VARIETY_TITLE = "objective_card.row_color_variety.title";
        public static final String OBJECTIVE_CARD_ROW_COLOR_VARIETY_DESCRIPTION = "objective_card.row_color_variety.description";
        public static final String OBJECTIVE_CARD_ROW_SHADE_VARIETY_TITLE = "objective_card.row_shade_variety.title";
        public static final String OBJECTIVE_CARD_ROW_SHADE_VARIETY_DESCRIPTION = "objective_card.row_shade_variety.description";
        public static final String OBJECTIVE_CARD_SHADE_VARIETY_TITLE = "objective_card.shade_variety.title";
        public static final String OBJECTIVE_CARD_SHADE_VARIETY_DESCRIPTION = "objective_card.shade_variety.description";
        public static final String OBJECTIVE_CARD_YELLOW_SHADE_TITLE = "objective_card.yellow_shade.title";
        public static final String OBJECTIVE_CARD_YELLOW_SHADE_DESCRIPTION = "objective_card.yellow_shade.description";

        public static final String MATCH_GUI_NOT_ENOUGH_TOKEN = "match_gui.not_enough_token.text";
        public static final String MATCH_GUI_USE_BUTTON = "match_gui.use_button.text";
        public static final String MATCH_GUI_BACK_BUTTON = "match_gui.back_button.text";
        public static final String MATCH_GUI_CHOOSE_YOUR_WINDOW = "match_gui.choose_your_window.text";
        public static final String MATCH_GUI_CHOOSE_SHADE_FOR_DIE = "match_gui.choose_shade_for_die.text";
        public static final String MATCH_GUI_RUN_AGAIN = "match_gui.run_again.text";
        public static final String MATCH_GUI_STOP = "match_gui.stop.text";
        public static final String MATCH_GUI_CHOOSE_ACTION_FOR_EFFECT = "match_gui.choose_action_for_effect.text";
        public static final String MATCH_GUI_EFFECT = "match_gui.effect.text";
        public static final String MATCH_GUI_CHOOSE_LOCATION = "match_gui.choose_location.text";
        public static final String MATCH_GUI_END_TURN="match_gui.end_turn.text";

        public static final String WINDOW_GUI_INVALID_MOVE = "window_gui.invalid_move.text";
        public static final String WINDOW_GUI_MOVE_NOT_MATCHES_RULES= "window_gui.move_not_matches_rules.text";

        public static final String TOOL_CARD_HELPER_CHOOSE = "tool_card_helper.choose.text";
        public static final String TOOL_CARD_HELPER_ACTION_PERFORM = "tool_card_helper.actions_perform.text";
        public static final String TOOL_CARD_HELPER_CORRECT_NUMBER = "tool_card_helper.correct_number.text";

        private Strings() {
        }

        public static String toLocalized(String string) {
            return new LocalizedString(string).toString();
        }

        public static String getToolCardTitle(String toolCardId) {
            try {
                return (String) Strings.class.getField(String.format("TOOL_CARD_%s_TITLE", toolCardId.toUpperCase())).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }

        public static String getToolCardEffect(String toolCardId) {
            try {
                return (String) Strings.class.getField(String.format("TOOL_CARD_%s_EFFECT", toolCardId.toUpperCase())).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }

        public static String getObjectiveCardTitle(String objectiveCardId) {
            try {
                return (String) Strings.class.getField(String.format("OBJECTIVE_CARD_%s_TITLE", objectiveCardId.toUpperCase())).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }
        public static String getObjectiveCardDescription(String objectiveCardId) {
            try {
                return (String) Strings.class.getField(String.format("OBJECTIVE_CARD_%s_DESCRIPTION", objectiveCardId.toUpperCase())).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }


    }

    public enum Status {
            OWNER_UNLOCKED, OPPONENT_LOCKED, GAME_LOCKED, SELECTION_UNLOCKED
    }
}