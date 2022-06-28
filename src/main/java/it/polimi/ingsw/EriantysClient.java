package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.GUIApplication;

/**
 * entry point of the client
 */
public class EriantysClient {

    /**
     * if a specific argument is present run cli else run gui application
     * @param args program arguments set on launch
     */
    public static void main(String[] args) {
        if(args.length > 0 && (args[0].equals("--cli") || args[0].equals("-c"))) {
            Client client = new Client();
            client.start();
        }
        else {
            GUIApplication.run(args);
        }
    }

}
