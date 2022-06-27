package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.moves.CardChoice;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static it.polimi.ingsw.server.Procedures.clearFolder;

class SavesManagerTest {
    Logger logger = LogFormatter.getLogger("Test");

    String code;
    String state;

    ServerSocket socket;

    @Test
    void reloadTest() throws IOException, InterruptedException {
        clearFolder(logger);
        firstRun();
        secondRun();
        socket.close();
        clearFolder(logger);
    }

    void firstRun() throws InterruptedException {
        Server s = new MatchmakingServer();
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();

        Connection g1 = Procedures.creatorLogin("riccardo", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);
        Connection g2 = Procedures.joinerLogin("tommaso", false, logger);
        InitialState is = (InitialState) g1.waitMessage(InitialState.class);
        int id = Game.deserializeGameFromString(is.getState());
        Game g = Game.request(id);
        g2.waitMessage(InitialState.class);

        File[] files = SavesManager.listDirectory(SavesManager.SAVES_ROOT);
        assert files.length == 1;
        code = files[0].getName();

        files = SavesManager.listDirectory(SavesManager.gameFolderPath(code));
        assert files.length == 2;
        List<String> names = new ArrayList<>(Arrays.stream(files).map(File::getName).toList());
        assert names.contains(SavesManager.USERNAMES_FILE_NAME);
        names.remove(SavesManager.USERNAMES_FILE_NAME);
        int cont = Integer.parseInt(names.get(0));
        assert cont == 0;

        CardChoice cc1 = new CardChoice(g.getWizard(0), 3);
        CardChoice cc2 = new CardChoice(g.getWizard(1), 3);
        try {
            cc1.validate(g);
            g1.send(cc1);
        } catch (GameRuleException e) {
            g2.send(cc2);
        }

        g1.waitMessage(CardChoice.class);
        g2.waitMessage(CardChoice.class);

        TimeUnit.MILLISECONDS.sleep(100);
        files = SavesManager.listDirectory(SavesManager.gameFolderPath(code));
        assert files.length == 2;
        names = new ArrayList<>(Arrays.stream(files).map(File::getName).toList());
        assert names.contains(SavesManager.USERNAMES_FILE_NAME);
        names.remove(SavesManager.USERNAMES_FILE_NAME);
        cont = Integer.parseInt(names.get(0));
        assert 1 == cont;
        SavesManager sm = new SavesManager(logger);
        state = sm.readFile(SavesManager.gameFilePath(code, "1"));
        socket = s.socket;
        // don't close the server properly to simulate crash
    }

    void secondRun() throws InterruptedException {
        int newPort = 40000;
        Server s = new MatchmakingServer() { // bind second server on a different port because the other is still in use
            @Override
            int getPortToBind() {
                return newPort;
            }
        };
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();
        TimeUnit.MILLISECONDS.sleep(100);

        Connection g1 = Procedures.reconnectLogin("riccardo", false, logger, newPort);
        Connection g2 = Procedures.reconnectLogin("tommaso", false, logger, newPort);
        InitialState is = (InitialState) g1.waitMessage(InitialState.class);
        assert is.getState().equals(state);
        int id = Game.deserializeGameFromString(is.getState());
        Game g = Game.request(id);
        g2.waitMessage(InitialState.class);

        File[] files = SavesManager.listDirectory(SavesManager.SAVES_ROOT);
        assert files.length == 1;
        assert files[0].getName().equals(code);
        s.stop();
        t.join();
        files = SavesManager.listDirectory(SavesManager.SAVES_ROOT);
        assert files.length == 0;
    }
}
