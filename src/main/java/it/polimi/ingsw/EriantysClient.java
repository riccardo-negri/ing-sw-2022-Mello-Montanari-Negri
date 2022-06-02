package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.GUIApplication;

public class EriantysClient {
    public static void main(String[] args) {
        if(args.length > 0 && (args[0].equals("--cli") || args[0].equals("-c"))) {
            Client client = new Client(false);
            client.start();
        }
        else {
            GUIApplication.run(args);
        }
    }

}
