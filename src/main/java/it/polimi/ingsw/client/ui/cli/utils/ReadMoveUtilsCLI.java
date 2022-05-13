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
import org.jline.reader.impl.history.DefaultHistory;
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

    private static String askAndCheckMove (Terminal terminal, History history, Completer completer, String username, Pattern pattern, boolean madeIllegalMove) {
        Matcher matcher;
        String move;

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

    private static AggregateCompleter decorateWithCharacterMove (int[] characters, ArgumentCompleter completer) {
        if(characters.length == 0) {
            return new AggregateCompleter(completer);
        }
        return new AggregateCompleter(
                completer,
                new ArgumentCompleter(
                        new StringsCompleter("play"),
                        new Completers.OptionCompleter(Arrays.asList(
                                new StringsCompleter("character"),
                                new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE),
                                JlineCommandRegistry.compileCommandOptions(""), 1)
                )
        );
    }

    public static void getMoveStudentToIsland (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        getMoveStudentToIsland(terminal, history, username, characters, list, false);
    }

    public static void getMoveStudentToIsland (Terminal terminal, History history, String username, int[] characters, List<String> list, boolean madeIllegalMove) {
        AggregateCompleter completer = decorateWithCharacterMove(characters, new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("student"),
                        new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                        new StringsCompleter("to"),
                        new StringsCompleter("dining-room", "island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-0"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile("^(move student )(green|red|yellow|pink|blue)( to )(dining-room|((island-)([0-9]|1[0-1])))\s?$", Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern, madeIllegalMove);

        list.add(move);
    }

    public static void getMoveMotherNature (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        getMoveMotherNature(terminal, history, username, characters, list, false);
    }

    public static void getMoveMotherNature (Terminal terminal, History history, String username, int[] characters, List<String> list, boolean madeIllegalMove) {
        AggregateCompleter completer = decorateWithCharacterMove(characters, new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("mother"),
                        new StringsCompleter("nature"),
                        new StringsCompleter("steps"),
                        new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile("^(move mother nature steps )([1-5])\s?$", Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern, madeIllegalMove);

        list.add(move);
    }

    public static void getMoveSelectCloud (Terminal terminal, History history, String username, int[] characters, List<String> list) {
        getMoveSelectCloud(terminal, history, username, characters, list, false);
    }

    public static void getMoveSelectCloud (Terminal terminal, History history, String username, int[] characters, List<String> list, boolean madeIllegalMove) {
        AggregateCompleter completer = decorateWithCharacterMove(characters, new ArgumentCompleter(
                new StringsCompleter("select"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("cloud"),
                        new StringsCompleter("1", "2", "3", "4"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        ));
        Pattern pattern = Pattern.compile("^(select cloud )([0-3])\s?$", Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern, madeIllegalMove);

        list.add(move);
    }

    public static void getMovePlayAssistant (Terminal terminal, History history, String username, List<String> list) {
        getMovePlayAssistant(terminal, history, username, list, false);
    }

    public static void getMovePlayAssistant (Terminal terminal, History history, String username, List<String> list, boolean madeIllegalMove) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("play"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("assistant"),
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );
        Pattern pattern = Pattern.compile("^(play assistant )([1-9]|1[0])\s?$", Pattern.CASE_INSENSITIVE);

        String move = askAndCheckMove(terminal, history, completer, username, pattern, madeIllegalMove);

        list.add(move);
    }
}
