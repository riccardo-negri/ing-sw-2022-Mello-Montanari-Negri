package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractStorylinePage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class StorylinePageCLI extends AbstractStorylinePage {

    public StorylinePageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        final String STORYLINE = """
                ███████╗████████╗ ██████╗ ██████╗ ██╗   ██╗██╗     ██╗███╗   ██╗███████╗
                ██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝██║     ██║████╗  ██║██╔════╝
                ███████╗   ██║   ██║   ██║██████╔╝ ╚████╔╝ ██║     ██║██╔██╗ ██║█████╗\040\040
                ╚════██║   ██║   ██║   ██║██╔══██╗  ╚██╔╝  ██║     ██║██║╚██╗██║██╔══╝\040\040
                ███████║   ██║   ╚██████╔╝██║  ██║   ██║   ███████╗██║██║ ╚████║███████╗
                ╚══════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝╚═╝  ╚═══╝╚══════╝
                """;


        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, STORYLINE + "\n" +
                "How many times have you felt like you recognize fantastic shapes drawn by the clouds?\n" +
                "How many times have you dreamed of being able to dive into those giant, fluffy pillows in the sky?\n" +
                "\nHidden by the soft cloud whiteness, there is a world where floating islands are home to great schools for young magical creatures from five realms.\n" +
                "Cute little red dragons, clumsy pink fairies, spiteful yellow gnomes, small blue unicorns, \n" +
                "and green frogs who dream of becoming princes show up at the gates of schools,\n" +
                "with the hope of being admitted to the great hall and being able to admire the\n" +
                "famous professors of their realm.\n" +
                "\nRun one of Eriantys' four great schools and compete with other wizards to increase your fame and build new branches inside magical towers.\n" +
                "Invite as many creatures of a realm into your dining room, and the corresponding professor will sit with them: \n" +
                "for as long as you host professors, they will influence all the creatures of the same realm on the islands.\n" +
                "When Mother Nature visits an island, she will reward the school that influences the greatest\n" +
                " number of creatures on that island with a new tower.\n" +
                "Try to build all your towers before the other players, but be careful! \n" +
                "The loyalties of the creatures of the five realms are very fickle, and the professors switch schools easily.\n" +
                "Creatures that until recently were on your side, could change allegiance in a flapping of wings!\n" +
                "\nEriantys is a game full of strategy, tactics, and twists and turns. Carefully plan your moves and try to control your opponents' moves.\n" + "\nPress enter to continue...");
        moveCursorToEnd(terminal);
        waitEnterPressed(terminal);
        onEnd();
    }
}
