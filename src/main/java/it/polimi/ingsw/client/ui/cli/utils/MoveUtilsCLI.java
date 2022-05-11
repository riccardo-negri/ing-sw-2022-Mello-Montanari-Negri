package it.polimi.ingsw.client.ui.cli.utils;

import it.polimi.ingsw.client.ui.cli.BoardPageCLI;
import org.jline.builtins.Completers;
import org.jline.console.impl.JlineCommandRegistry;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.widget.AutosuggestionWidgets;
import org.jline.widget.TailTipWidgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;


public class MoveUtilsCLI {

    public static void askForMove (Terminal terminal, Completer completer, List<String> list) {
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .build();

        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        TailTipWidgets tailtipWidgets = new TailTipWidgets(reader, new HashMap<>(), 0, TailTipWidgets.TipType.COMPLETER);

        autosuggestionWidgets.enable();
        tailtipWidgets.enable();
        String temp = reader.readLine(String.valueOf(ansi().fgRgb(255, 128, 0).a("Tom").fgDefault().a(":").fgBlue().a("~").fgGreen().a("$ ").fgDefault()));
        list.add(temp);
    }

    public static void getMoveStudentToIsland (Terminal terminal,  List<String> list) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("student"),
                        new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                        new StringsCompleter("to"),
                        new StringsCompleter("dining-room", "island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-12"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        askForMove(terminal, completer, list);
    }

    public static void getMoveMotherNature (Terminal terminal,  List<String> list) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("mother"),
                        new StringsCompleter("nature"),
                        new StringsCompleter("steps"),
                        new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        askForMove(terminal, completer, list);
    }

    public static void getMoveSelectCloud (Terminal terminal,  List<String> list) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("select"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("cloud"),
                        new StringsCompleter("1", "2", "3", "4"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        askForMove(terminal, completer, list);
    }

    public static void getMovePlayAssistant (Terminal terminal,  List<String> list) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("play"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("assistant"),
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        askForMove(terminal, completer, list);
    }
}
