package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;



class BagTest {
    private Bag bag = new Bag();


    @Test
    void pickDieTest (){
        List<Enum> list = Arrays.asList(GlassColor.values());
        Assertions.assertNotNull(bag.pickDie());
        Assertions.assertTrue(list.contains(bag.pickDie().getColor()));
    }

    @Test
    void getNumberOfDieTest() {
        Assertions.assertTrue(bag.getNumberOfDice()<=90);
        Assertions.assertTrue(bag.getNumberOfDice()>=40);
    }

    @Test

    void putDieTest() {

    }


}