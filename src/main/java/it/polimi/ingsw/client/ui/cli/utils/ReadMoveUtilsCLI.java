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


public class ReadMoveUtilsCLI {

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

    private static AggregateCompleter decorateWithCharacterHelper (int[] characters, ArgumentCompleter completer) {
        if (characters.length == 0) {
            return new AggregateCompleter(completer);
        }
        return new AggregateCompleter(
                completer,
                characterHelper(characters)
        );
    }
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

    private static ArgumentCompleter characterCompleter (int id) {
        return switch (id) {
            case 1 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-1"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("move"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            new StringsCompleter("to"),
                            new StringsCompleter("island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-0"),
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
                            new StringsCompleter("select"),
                            new StringsCompleter("island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-0"),
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
                            new StringsCompleter("select"),
                            new StringsCompleter("island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-0"),
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
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
                            new StringsCompleter("remove-from-dining"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
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
                            new StringsCompleter("select"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 10 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-10"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("take-from-entrance"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
                            new StringsCompleter("exchange-with-from-dining"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue", "nothing"),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 11 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-11"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("take"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            case 12 -> new ArgumentCompleter(
                    new StringsCompleter("use-character-12"),
                    new Completers.OptionCompleter(Arrays.asList(
                            new StringsCompleter("select"),
                            new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                            NullCompleter.INSTANCE),
                            JlineCommandRegistry.compileCommandOptions(""), 1)
            );
            default -> null;
        };
    }

    private static ArgumentCompleter characterHelper (int[] ids) {
        return new ArgumentCompleter(
                new StringsCompleter("help"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("character-" + ids[0], "character-" + ids[1], "character-" + ids[2]),
                        NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1));
    }

    private static String decorateWithHelperRegex (int[] characters, String regex) {
        if (characters.length != 0) {
            for (int character : characters) {
                regex += "|" + Regex.HELPER_CHARACTER_START + character + Regex.HELPER_CHARACTER_END;
            }
        }
        return regex;
    }
    private static String decorateWithUseCharacterAndHelperRegex (int[] characters, String regex) {
        if (characters.length == 0) {
            return regex;
        }
        else {
            return regex + "|" + characterRegex(characters[0]) + "|" + characterRegex(characters[1]) + "|" + characterRegex(characters[2]);
        }
    }

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

    public static void getMoveStudentToIsland (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterMoveAndHelper(characters, new ArgumentCompleter(
                new StringsCompleter("move-student"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                        new StringsCompleter("to"),
                        new StringsCompleter("dining-room", "island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-0"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithUseCharacterAndHelperRegex(characters, Regex.MOVE_STUDENT), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }

    public static void getMoveMotherNature (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterMoveAndHelper(characters, new ArgumentCompleter(
                new StringsCompleter("move-mother-nature"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("steps"),
                        new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithUseCharacterAndHelperRegex(characters, Regex.MOVE_MOTHER_NATURE), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }

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

    public static void getMovePlayAssistant (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        AggregateCompleter completer = decorateWithCharacterHelper(characters, new ArgumentCompleter(
                new StringsCompleter("play-assistant"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile(decorateWithHelperRegex(characters, Regex.PLAY_ASSISTANT), Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern);

        list.add(move);
    }
}
