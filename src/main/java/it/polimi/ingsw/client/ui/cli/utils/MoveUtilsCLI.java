package it.polimi.ingsw.client.ui.cli.utils;

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

import static org.fusesource.jansi.Ansi.ansi;


public class MoveUtilsCLI {

    public static String askForMove (Terminal terminal, Completer completer) {
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .build();

        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        TailTipWidgets tailtipWidgets = new TailTipWidgets(reader, new HashMap<>(), 0, TailTipWidgets.TipType.COMPLETER);

        autosuggestionWidgets.enable();
        tailtipWidgets.enable();
        return reader.readLine(String.valueOf(ansi().fgRgb(255, 128, 0).a("Tom").fgDefault().a(":").fgBlue().a("~").fgGreen().a("$ ").fgDefault()));
    }

    public static String getMoveStudentToIsland (Terminal terminal) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("student"),
                        new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                        new StringsCompleter("to"),
                        new StringsCompleter("dining-room", "island-1", "island-2", "island-3", "island-4", "island-5", "island-6", "island-7", "island-8", "island-9", "island-10", "island-11", "island-12"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        return askForMove(terminal, completer);
    }

    public static String getMoveMotherNature (Terminal terminal) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("move"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("mother"),
                        new StringsCompleter("nature"),
                        new StringsCompleter("steps"),
                        new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        return askForMove(terminal, completer);
    }

    public static String getMoveSelectCloud (Terminal terminal) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("select"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("cloud"),
                        new StringsCompleter("1", "2", "3", "4"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        return askForMove(terminal, completer);
    }

    public static String getMovePlayAssistant (Terminal terminal) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter("play"),
                new Completers.OptionCompleter(Arrays.asList(
                        new StringsCompleter("assistant"),
                        new StringsCompleter("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), NullCompleter.INSTANCE),
                        JlineCommandRegistry.compileCommandOptions(""), 1)
        );

        return askForMove(terminal, completer);
    }
}
