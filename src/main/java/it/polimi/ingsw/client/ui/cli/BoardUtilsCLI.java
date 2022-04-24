package it.polimi.ingsw.client.ui.cli;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiPrintStream;

import java.text.MessageFormat;
import java.util.ArrayList;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    public static Integer getIslandWidth () {
        return 18;
    }

    public static Integer getIslandHeight () {
        return 9;
    }

    private static String getAnsiFromColor(String color, int value) {
        switch(color) {
            case "RED":
                return ansi().fgRgb(255, 102, 102).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "GREEN":
                return ansi().fgRgb(0, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "PINK":
                return ansi().fgRgb(255, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "YELLOW":
                return ansi().fgRgb(255, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "BLUE":
                return ansi().fgRgb(51, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            default:
                return "";
        }
    }

    private static Ansi translateTowerColor (String tower) {
        switch (tower) {
            case "white":
                return ansi().fgRgb(255, 255, 255).a("W-Tower");
            case "black":
                return ansi().fgBrightBlack().a("B-Tower");
            case "grey":
                return ansi().fgRgb(180, 180, 180).a("G-Tower");
            default:
                return ansi().a("       ");
        }
    }

    public static void drawIsland (Integer ID, Integer startRow, Integer startCol, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink, boolean hasMotherNature) {
        final String R1 = "    _________\n";
        final String R2 = "   /         \\\n";
        final String R3 = "  /   ID-{0}   \\\n";
        final String R4 = " /             \\\n";
        final String R5 = "/   {0}   {1}   {2}   \\\n";
        final String R6 = "\\     {0}   {1}     /\n";
        final String R7 = " \\             /\n";
        final String R8 = "  \\  {0}  /\n";
        final String R9 = "   \\_________/\n";

        AnsiConsole.out().println(ansi().cursor(startRow, startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow + 1, startCol).a(R2));
        AnsiConsole.out().println(ansi().cursor(startRow + 2, startCol).a(
                MessageFormat.format(R3, (hasMotherNature ? ansi().fgRgb(255, 128, 0).a(String.format("%02d", ID)) + "" + ansi().fgDefault().bgDefault().a("") : String.format("%02d", ID)))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 3, startCol).a(R4));
        AnsiConsole.out().println(ansi().cursor(startRow + 4, startCol).a(
                MessageFormat.format(R5,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 5, startCol).a(
                MessageFormat.format(R6,
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " "),
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " ")

                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 6, startCol).a(R7));
        AnsiConsole.out().println(ansi().cursor(startRow + 7, startCol).a(
                MessageFormat.format(R8, translateTowerColor(tower) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 8, startCol).a(R9));
    }

    public static void drawCloud(Integer ID, Integer startRow, Integer startCol, Integer numOfPlayer, Integer yellow, Integer blue, Integer green, Integer red, Integer pink) {
        final String R1 =   "   .-\"-.";
        final String R2 =   " /` C-{0} `\\";
        final String R3 =   ";  {0}   {1}  ;";
        final String R4 =   ";    {0}    ;";
        final String R5 = " \\ {0}   {1} /";
        final String R6 =   "  `'-.-'`";

        AnsiConsole.out().println(ansi().cursor(startRow, startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow+1, startCol).a(
                MessageFormat.format(R2, ID)
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+2, startCol).a(
                MessageFormat.format(R3,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3, startCol).a(
                MessageFormat.format(R4,
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " ")

                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+4, startCol).a(
                MessageFormat.format(R5,
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5, startCol).a(R6));
    }
}
