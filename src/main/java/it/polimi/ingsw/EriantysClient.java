package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.GUIApplication;
import it.polimi.ingsw.utils.LogFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * entry point of the client
 */
public class EriantysClient {

    /**
     * if a specific argument is present run cli else run gui application
     * if the debug argument is present activate logging
     * @param args program arguments set on launch
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.contains("-d") || argsList.contains("--debug")) {
            LogFormatter.setDebugActivated(true);
        }
        if(argsList.contains("-c") || argsList.contains("--cli")) {
            Client client = new Client();
            client.start();
        }
        else {
            GUIApplication.run(args);
        }
    }

}
