package it.polimi.ingsw.client.ui.cli.utils;

import org.jline.builtins.Completers;
import org.jline.console.impl.JlineCommandRegistry;
import org.jline.reader.Completer;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.widget.AutosuggestionWidgets;
import org.jline.widget.TailTipWidgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * class with all the methods to take input from the user with autocompletion and validate it against REGEX
 */
public class ReadMoveUtilsCLI {
    private static final String GREEN = "green";
    private static final String RED = "red";
    private static final String YELLOW = "yellow";
    private static final String PINK = "pink";
    private static final String BLUE = "blue";

    private static final String ISLAND_0 = "island-0";
    private static final String ISLAND_1 = "island-1";
    private static final String ISLAND_2 = "island-2";
    private static final String ISLAND_3 = "island-3";
    private static final String ISLAND_4 = "island-4";
    private static final String ISLAND_5 = "island-5";
    private static final String ISLAND_6 = "island-6";
    private static final String ISLAND_7 = "island-7";
    private static final String ISLAND_8 = "island-8";
    private static final String ISLAND_9 = "island-9";
    private static final String ISLAND_10 = "island-10";
    private static final String ISLAND_11 = "island-11";
    private ReadMoveUtilsCLI () {
        
    }

    /**
     * print bash styled prompt text and ask user for the command
     * @param terminal JLine terminal
     * @param history commands history
     * @param completer completer
     * @param username user's username
     * @return typed command
     */
    public static String askForMove (Terminal terminal, History history, Completer completer, String username) {
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .history(history)
                .build();

        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        TailTipWidgets tailtipWidgets = new TailTipWidgets(reader, new HashMap<>(), 0, TailTipWidgets.TipType.COMPLETER);

        autosuggestionWidgets.enable();
        tailtipWidgets.enable();
        return reader.readLine(String.valueOf(ansi().fgRgb(255, 128, 0).a(username).fgDefault().a(":").fgBlue().a("~").fgGreen().a("$ ").fgDefault()));
    }

    /**
     * ask use to type the command and check it against the regex
     * @param terminal JLine terminal
     * @param history commands history
     * @param completer completer
     * @param username user's username
     * @param pattern regex
     * @return typed command
     */
    private static String askAndCheckMove (Terminal terminal, History history, Completer completer, String username, Pattern pattern) {
        Matcher matcher;
        String move;
        boolean madeIllegalMove = false;

        do {
            if (madeIllegalMove) {
                terminal.writer().println(ansi().fgRgb(255, 0, 0).a("Please type a valid command...").fgDefault());
            }
            move = askForMove(terminal, history, completer, username);
            matcher = pattern.matcher(move);
            madeIllegalMove = true;
        }
        while (!matcher.find());

        return move;
    }

    /**
     * to be used only in the planning phase, adds the characters' helper autocompletion
     * @param characters active characters IDs list
     * @param completer completer to decorate
     * @return decorated completer
     */
    private static AggregateCompleter decorateWithCharacterHelper (int[] characters, ArgumentCompleter completer) {
        if (characters.length == 0) {
            return new AggregateCompleter(completer);
        }
        return new AggregateCompleter(
                completer,
                characterHelper(characters)
        );
    }

    /**
     * decorator to add autocompletion for character commands and helper for characters, if the game mode is COMPLETE otherwise it does not add anything
     * @param characters list of active characters
     * @param completer current completer to decorate
     * @return decorated completer
     */
    private static AggregateCompleter decorateWithCharacterMoveAndHelper (int[] characters, ArgumentCompleter completer) {
        if (characters.length == 0) {
            return new AggregateCompleter(completer);
        }
        return new AggregateCompleter(
                completer,
                characterCompleter(characters[0]),
                characterCompleter(characters[1]),
                characterCompleter(characters[2]),
                characterHelper(characters)
        );
    }

    /**
     * create completer for character command
     * @param id character id
     * @return completer for character command
     */
    private static ArgumentCompleter characterCompleter (int id) {        
        final String SELECT = "select";
        final String NOTHING = "nothing";
        return switch (id) {
            case 1 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-1"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("move"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            new StringsCompleter("to"),
                            new StringsCompleter(ISLAND_1, ISLAND_2, ISLAND_3, ISLAND_4, ISLAND_5, ISLAND_6, ISLAND_7, ISLAND_8, ISLAND_9, ISLAND_10, ISLAND_11, ISLAND_0),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 2 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-2"),
                    new Completers.OptionCompleter(List.of(
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 3 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-3"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter(SELECT),
                            new StringsCompleter(ISLAND_1, ISLAND_2, ISLAND_3, ISLAND_4, ISLAND_5, ISLAND_6, ISLAND_7, ISLAND_8, ISLAND_9, ISLAND_10, ISLAND_11, ISLAND_0),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 4 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-4"),
                    new Completers.OptionCompleter(List.of(
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 5 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-5"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter(SELECT),
                            new StringsCompleter(ISLAND_1, ISLAND_2, ISLAND_3, ISLAND_4, ISLAND_5, ISLAND_6, ISLAND_7, ISLAND_8, ISLAND_9, ISLAND_10, ISLAND_11, ISLAND_0),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 6 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-6"),
                    new Completers.OptionCompleter(List.of(
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 7 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-7"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("take"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            new StringsCompleter("remove-from-dining"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 8 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-8"),
                    new Completers.OptionCompleter(List.of(
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 9 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-9"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter(SELECT),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 10 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-10"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("take-from-entrance"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            new StringsCompleter("exchange-with-from-dining"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE, NOTHING),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 11 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-11"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("take"),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 12 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-12"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter(SELECT),
                            new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            default -> null;
        };
    }

    /**
     * create completer for the helper commands for every active character
     * @param ids array of active characters IDs
     * @return helper completer
     */
    private static ArgumentCompleter characterHelper (int[] ids) {
        final String CHARACTER_PREFIX = "character-";
        return new ArgumentCompleter(
                new StringsCompleter("character-info"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter(CHARACTER_PREFIX + ids[0], CHARACTER_PREFIX + ids[1], CHARACTER_PREFIX + ids[2]),
                        NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1));
    }

    /**
     * to be used on in the planning phase, adds also the helpers regex
     * @param characters active characters list
     * @return regex for play assistant command and helper commands
     */
    private static String decorateWithHelperRegex (int[] characters) {
        if (characters.length != 0) {
            StringBuilder regexBuilder = new StringBuilder(Regex.PLAY_ASSISTANT);
            for (int character : characters) {
                regexBuilder.append("|").append(Regex.HELPER_CHARACTER_START).append(character).append(Regex.HELPER_CHARACTER_END);
            }
            return regexBuilder.toString();
        }
        return Regex.PLAY_ASSISTANT;
    }

    /**
     * add regex for use character commands and helper commands
     * @param characters list of active character
     * @param regex regex to decorate
     * @return decorated regex
     */
    private static String decorateWithUseCharacterAndHelperRegex (int[] characters, String regex) {
        if (characters.length == 0) {
            return regex;
        }
        else {
            return regex + "|" + characterRegex(characters[0]) + "|" + characterRegex(characters[1]) + "|" + characterRegex(characters[2]);
        }
    }

    /**
     * get character use command and helper command regex from character id
     * @param id character id
     * @return regex of the command to use the character
     */
    private static String characterRegex (int id) {
        return switch (id) {
            case 1 -> Regex.USE_CHARACTER_1;
            case 2 -> Regex.USE_CHARACTER_2;
            case 3 -> Regex.USE_CHARACTER_3;
            case 4 -> Regex.USE_CHARACTER_4;
            case 5 -> Regex.USE_CHARACTER_5;
            case 6 -> Regex.USE_CHARACTER_6;
            case 7 -> Regex.USE_CHARACTER_7;
            case 8 -> Regex.USE_CHARACTER_8;
            case 9 -> Regex.USE_CHARACTER_9;
            case 10 -> Regex.USE_CHARACTER_10;
            case 11 -> Regex.USE_CHARACTER_11;
            case 12 -> Regex.USE_CHARACTER_12;
            default -> null;
        } + "|" + Regex.HELPER_CHARACTER_START + id + Regex.HELPER_CHARACTER_END;
    }

    /**
     * read command from user while in move student phase with autocompletion
     * @param terminal JLine terminal
     * @param history commands history
     * @param username user's username
     * @param characters characters list (empty if game mode is SIMPLE)
     * @param list list where to append the command written by the user if it passed the regex checks
     */
    public static void getMoveStudentToIsland (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterMoveAndHelper(characters, new ArgumentCompleter(
                new StringsCompleter("move-student"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter(GREEN, RED, YELLOW, PINK, BLUE),
                        new StringsCompleter("to"),
                        new StringsCompleter("dining-room", ISLAND_1, ISLAND_2, ISLAND_3, ISLAND_4, ISLAND_5, ISLAND_6, ISLAND_7, ISLAND_8, ISLAND_9, ISLAND_10, ISLAND_11, ISLAND_0), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithUseCharacterAndHelperRegex(characters, Regex.MOVE_STUDENT), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }

    /**
     * read command from user while in move mother nature phase with autocompletion
     * @param terminal JLine terminal
     * @param history commands history
     * @param username user's username
     * @param characters characters list (empty if game mode is SIMPLE)
     * @param list list where to append the command written by the user if it passed the regex checks
     */
    public static void getMoveMotherNature (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterMoveAndHelper(characters, new ArgumentCompleter(
                new StringsCompleter("move-mother-nature"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("steps"),
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithUseCharacterAndHelperRegex(characters, Regex.MOVE_MOTHER_NATURE), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }

    /**
     * read command from user while in select cloud phase with autocompletion
     * @param terminal JLine terminal
     * @param history commands history
     * @param username user's username
     * @param characters characters list (empty if game mode is SIMPLE)
     * @param list list where to append the command written by the user if it passed the regex checks
     */
    public static void getMoveSelectCloud (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterMoveAndHelper(characters, new ArgumentCompleter(
                new StringsCompleter("select-cloud"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("0", "1", "2", "3"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithUseCharacterAndHelperRegex(characters, Regex.SELECT_CLOUD), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }

    /**
     * read command from user while in planning phase with autocompletion
     * @param terminal JLine terminal
     * @param history commands history
     * @param username user's username
     * @param characters characters list (empty if game mode is SIMPLE)
     * @param list list where to append the command written by the user if it passed the regex checks
     */
    public static void getMovePlayAssistant (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterHelper(characters, new ArgumentCompleter(
                new StringsCompleter("play-assistant"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithHelperRegex(characters), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }
}
