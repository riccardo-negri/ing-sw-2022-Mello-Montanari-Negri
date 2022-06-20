package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.networking.moves.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MovesOrderTest {

    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void reorderTest() throws IOException {
        List<Connection> connections = Procedures.twoBoundedConnections(logger);
        Connection c1 = connections.get(0);
        Connection c2 = connections.get(1);

        Wizard w = new Wizard(1, new ArrayList<>(), Tower.BLACK, 8);

        CardChoice cac = new CardChoice(w, 4);
        cac.setNumber(3);
        MotherNatureMovement mnm = new MotherNatureMovement(w, 5);
        mnm.setNumber(1);
        CloudChoice clc = new CloudChoice(w, 2);
        clc.setNumber(0);
        DiningRoomMovement drm = new DiningRoomMovement(w, StudentColor.BLUE);
        drm.setNumber(2);

        ObjectOutputStream outStream = c2.getWriter();
        outStream.writeObject(cac);
        outStream.writeObject(mnm);
        outStream.writeObject(clc);
        outStream.writeObject(drm);

        // check that is receiving in the correct order
        Move m = (Move) c1.waitMessage(Move.class);
        assert (m instanceof CloudChoice);
        m = (Move) c1.waitMessage(Move.class);
        assert (m instanceof MotherNatureMovement);
        m = (Move) c1.waitMessage(Move.class);
        assert (m instanceof DiningRoomMovement);
        m = (Move) c1.waitMessage(Move.class);
        assert (m instanceof CardChoice);
        assert c1.noMessageLeft();
    }

    @Test
    void assignNumberTest() throws IOException {

        List<Connection> connections = Procedures.twoBoundedConnections(logger);
        Connection c1 = connections.get(0);
        Connection c2 = connections.get(1);

        Wizard w = new Wizard(1, new ArrayList<>(), Tower.BLACK, 8);

        c1.send(new CardChoice(w, 4));
        c1.send(new MotherNatureMovement(w, 5));
        c1.send(new CloudChoice(w, 2));
        c1.send(new DiningRoomMovement(w, StudentColor.BLUE));

        // check that number was set correctly on send
        Move m = (Move) c2.waitMessage(Move.class);
        assert (m instanceof CardChoice);
        assert m.getNumber() == 0;
        m = (Move) c2.waitMessage(Move.class);
        assert (m instanceof MotherNatureMovement);
        assert m.getNumber() == 1;
        m = (Move) c2.waitMessage(Move.class);
        assert (m instanceof CloudChoice);
        assert m.getNumber() == 2;
        m = (Move) c2.waitMessage(Move.class);
        assert (m instanceof DiningRoomMovement);
        assert m.getNumber() == 3;
        assert c2.noMessageLeft();
    }
}
