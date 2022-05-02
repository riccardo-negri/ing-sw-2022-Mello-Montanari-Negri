package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class BagTest {
    Bag bag;

    @BeforeEach
    void setBag() {
        bag = new Bag(new Random());
    }

    @Test
    void requestStudents() {
        try {
            List<StudentColor> result = bag.requestStudents(0);
            Assertions.assertEquals(0,result.size());
            result = bag.requestStudents(130);
            assert (result.size() == 130);
            Integer[] vals = {0,0,0,0,0};
            for (StudentColor s : result) {
                assert (s != null);
                vals[s.getValue()]++;
            }
            assert (vals[0] == 26 && vals[1] == 26 && vals[2] == 26 && vals[3] == 26 && vals[4] == 26);
            bag.requestStudents(1);
        } catch (Exception e) { }
    }
}
