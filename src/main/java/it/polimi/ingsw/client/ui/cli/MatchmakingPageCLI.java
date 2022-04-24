package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMatchmakingPage;

public class MatchmakingPageCLI extends AbstractMatchmakingPage {
    public MatchmakingPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String GAME_MODE =
                "███╗   ███╗ █████╗ ████████╗ ██████╗██╗  ██╗███╗   ███╗ █████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗ \n" +
                "████╗ ████║██╔══██╗╚══██╔══╝██╔════╝██║  ██║████╗ ████║██╔══██╗██║ ██╔╝██║████╗  ██║██╔════╝ \n" +
                "██╔████╔██║███████║   ██║   ██║     ███████║██╔████╔██║███████║█████╔╝ ██║██╔██╗ ██║██║  ███╗\n" +
                "██║╚██╔╝██║██╔══██║   ██║   ██║     ██╔══██║██║╚██╔╝██║██╔══██║██╔═██╗ ██║██║╚██╗██║██║   ██║\n" +
                "██║ ╚═╝ ██║██║  ██║   ██║   ╚██████╗██║  ██║██║ ╚═╝ ██║██║  ██║██║  ██╗██║██║ ╚████║╚██████╔╝\n" +
                "╚═╝     ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ \n";
        CoreCLI cli = (CoreCLI) client.getUI();

        cli.clearTerminal();
        cli.printTerminalCenteredMultilineText(GAME_MODE, 1);
        cli.printEmptyLine();
        cli.printTerminalCenteredLine("You are currently in queue to join a game...");
        //cli.waitKeyPressed();
        Boolean SetupConnectionLock = client.getConnectionLock();
        synchronized (SetupConnectionLock) {
            while(!SetupConnectionLock) {
                try {
                    SetupConnectionLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        onEnd();
    }
}
