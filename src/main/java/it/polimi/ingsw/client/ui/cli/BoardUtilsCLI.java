package it.polimi.ingsw.client.ui.cli;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.text.MessageFormat;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    private static final String HEAD =              "    __________\n";
    private static final String ISLAND_ID =         "   /   ID{0}   \\\n";
    private static final String TOWER =             "  /    Tower   \\\n";
    private static final String RED =               " /      {0}      \\\n";
    private static final String YELLOW =            "/       {0}       \\\n";
    private static final String GREEN =             "\\       {0}       /\n";
    private static final String BLUE =              " \\      {0}      /\n";
    private static final String PINK =              "  \\     {0}     /\n";
    private static final String FOOTER =            "   \\__________/\n";


    private static final String HEXAGON =
            "    __________\n" +
            "   /          \\\n" +
            "  /            \\\n" +
            " /              \\\n" +
            "/                \\\n" +
            "\\                /\n" +
            " \\              /\n" +
            "  \\            /\n" +
            "   \\__________/\n";

    private static final String SMALL_HEXAGON =
            "   ______\n" +
            "  /      \\\n" +
            " /        \\\n" +
            "/          \\\n" +
            "\\          /\n" +
            " \\        /\n" +
            "  \\______/";

    public static void drawIsland2(Integer startRow, Integer startCol, Integer ID, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink) {

        AnsiConsole.out().println(ansi().
                cursor(startRow,startCol).a(HEAD)
        );
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(
                MessageFormat.format(ISLAND_ID, String.format("%02d", ID))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+2,startCol).a(
                MessageFormat.format(TOWER, tower)
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3,startCol).a(
                MessageFormat.format(RED, ansi().bgRgb(255, 102, 102).fgBlack().a(String.format("%02d", red)) + "" + ansi().bgDefault().fgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+4,startCol).a(
                MessageFormat.format(YELLOW, ansi().bgRgb(255, 255, 0).fgBlack().a(String.format("%02d", yellow)) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5,startCol).a(
                MessageFormat.format(GREEN, ansi().bgRgb(0, 255, 0).fgBlack().a(String.format("%02d", green)) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+6,startCol).a(
                MessageFormat.format(BLUE, ansi().bgRgb(51, 153, 255).fgBlack().a(String.format("%02d", blue)) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+7,startCol).a(
                MessageFormat.format(PINK, ansi().bgRgb(255, 153, 255).fgBlack().a(String.format("%02d", pink)) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+8,startCol).a(
                FOOTER
        ));
    }

    private static final String R1 =              "    __________\n";
    private static final String R2 =              "   /          \\\n";
    private static final String R3 =              "  /    {0}    \\\n";
    private static final String R4 =              " /              \\\n";
    private static final String R5 =              "/   {0}  {1}  {2}   \\\n";
    private static final String R6 =              "\\     {0}  {1}     /\n";
    private static final String R7 =              " \\              /\n";
    private static final String R8 =              "  \\   {0}   /\n";
    private static final String R9 =              "   \\__________/\n";

    public static Integer getIslandWidth() {return 18;}
    public static Integer getIslandHeight() {return 9;}

    public static void drawIsland (Integer ID, Integer startRow, Integer startCol, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink, boolean hasMotherNature) {

        AnsiConsole.out().println(ansi().cursor(startRow,startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(R2));
        AnsiConsole.out().println(ansi().cursor(startRow+2,startCol).a(
                MessageFormat.format(R3, (hasMotherNature ? ansi().fgRgb(255,128,0).a(String.format("ID%02d", ID)) + "" + ansi().fgDefault().bgDefault().a("") : String.format("ID%02d", ID)))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3,startCol).a(R4));
        AnsiConsole.out().println(ansi().cursor(startRow+4,startCol).a(
                MessageFormat.format(R5,
                        ansi().fgRgb(255, 102, 102).a(String.format("%02d", red)) + "" + ansi().fgDefault().bgDefault().a(""),
                        ansi().fgRgb(0, 255, 0).a(String.format("%02d", green)) + "" + ansi().fgDefault().bgDefault().a(""),
                        ansi().fgRgb(255, 153, 255).a(String.format("%02d", pink)) + "" + ansi().fgDefault().bgDefault().a("")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5,startCol).a(
                MessageFormat.format(R6,
                        ansi().fgRgb(255, 255, 0).a(String.format("%02d", yellow)) + "" + ansi().fgDefault().bgDefault().a(""),
                        ansi().fgRgb(51, 153, 255).a(String.format("%02d", blue)) + "" + ansi().fgDefault().bgDefault().a("")

                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+6,startCol).a(R7));
        AnsiConsole.out().println(ansi().cursor(startRow+7,startCol).a(
                MessageFormat.format(R8,translateTowerColor(tower) + "" + ansi().fgDefault().bgDefault().a("") )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+8,startCol).a(R9));
    }

    private static Ansi translateTowerColor(String tower) {
        switch(tower) {
            case "white": return ansi().fgRgb(255, 255, 255).a("W-Towr");
            case "black": return ansi().fgBrightBlack().a("B-Towr");
            case "grey": return ansi().fgRgb(180, 180, 180).a("G-Towr");
            default: return ansi().a("      ");
        }
    }
}
