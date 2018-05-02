package it.polimi.ingsw.models;

import it.polimi.ingsw.core.GlassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

// FIXME: Ma cosa è questa cosa?? Per quale motivo i test vengono fatti stampando a tastiera? NON si può testare un metodo guardando cosa viene stampato su console!
class BagTest {
    private Bag bag;

    @BeforeEach
    void setBag() {
        this.bag = new Bag();
    }

    @Test
    void getDieTest() {
        Assertions.assertNotNull(bag.getDice());
        // FIXME: Invece di guardare che i dadi siano corretti, stampandoli su console, controllare che:
        // FIXME:   count(red_dice) == count(yellow_dice) == count(green_dice) == count(blue_dice) == count(purple_dice) == 18;
        // FIXME:   count(dice) == 90
        System.out.println(bag.getDice());
    }

    @Test
    void getNumberPerColorTest() {
        Assertions.assertEquals(18, bag.getNumberPerColor(GlassColor.BLUE));
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(17, bag.getNumberPerColor(color));
    }

    @Test
    void pickDieTest() {
        List<Enum> list = Arrays.asList(GlassColor.values());
        Assertions.assertNotNull(bag.pickDie());
        Assertions.assertTrue(list.contains(bag.pickDie().getColor()));
        System.out.println(bag.pickDie().getColor());
        System.out.println(bag.pickDie().getColor());
        System.out.println(bag.pickDie().getColor());

    }

    @Test
    void getNumberOfDieTest() {
        Assertions.assertEquals(90, bag.getNumberOfDice());
        // FIXME: Non ne vedo il senso, dai test precendeti sai già che i dadi ritornati sono corretti
        System.out.println(bag.getDice());
        GlassColor color = bag.pickDie().getColor();
        Assertions.assertEquals(89, bag.getNumberOfDice());
        // FIXME: Non ne vedo il senso, invece di leggere di nuovo tutti i dadi puoi controllare che il numero di dadi del colore pescato sia stato decrementato
        System.out.println(bag.getDice());
        bag.putDie(new Die(color, 0));
        Assertions.assertEquals(90, bag.getNumberOfDice());
        // FIXME: Idem qui
        System.out.println(bag.getDice());
    }

    @Test
    void putDieTest() {
        int prev = bag.getNumberOfDice();
        // FIXME: Nemmeno qui ne vedo il senso. Se quello che volevi ottenere era una sorta di log di debug: 1) non è questo il modo per farlo 2) meglio se metti un breakpoint e guardi da solo
        System.out.println(bag.getDice());
        bag.putDie(new Die(GlassColor.BLUE, 0));
        Assertions.assertEquals(prev + 1, bag.getNumberOfDice());
        System.out.println(bag.getDice());
    }

    @Test
    void getFreeSpaceTest() {
        Assertions.assertEquals(90-bag.getNumberOfDice(), bag.getFreeSpace());

        // FIXME: Suggerisco un metodo migliore: (usando la funzione qua sotto viene ancora meglio)
        /*
        int removeN = new Random(System.currentTimeMillis()).nextInt(90);

        for (int i = 1; i <= removeN; i++) {
            this.bag.pickDie();

            Assertions.assertEquals(90 - i, this.bag.getNumberOfDice());
            Assertions.assertEquals(90 - this.bag.getNumberOfDice(), this.bag.getFreeSpace());
        }
        */
    }

    /*
    @ParameterizedTest
    @ValueSource(ints = {0, 15, 2, 90, 10, 56})
    void testFreeSpace(int numberOfDiceToPick) {
        for (int i = 1; i <= numberOfDiceToPick; i++) {
            this.bag.pickDie();

            Assertions.assertEquals(90 - i, this.bag.getNumberOfDice());
            Assertions.assertEquals(90 - this.bag.getNumberOfDice(), this.bag.getFreeSpace());
        }
    }
    */
}