package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.RandomPickLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


// FIXME: Bag dovrebbe implementare RandomPickLocation, RandomPutLocation
public class Bag implements RandomPutLocation, RandomPickLocation/* implements RandomPutLocation, RandomPickLocation */ {

    private List<Die> dieList;

    private List<Die> getMyList () {

        List<Die> myList = new LinkedList<>();
        for (int i = 0; i < GlassColor.values().length; i++) {
            for (int j = 0; j < 3; j++) {
                myList.add(new Die(GlassColor.values()[i], j));
            }
        }
        return myList;
    }


    public Bag() {
        this.dieList = getMyList();
    }


    @Override
    public Die pickDie() {
        Random rand = new Random();
        return dieList.remove(rand.nextInt());
    }

    @Override
    public int getNumberOfDice() {

        return dieList.size();
    }

    @Override
    public void putDie(Die die) {
        this.dieList.add(die);
    }

    @Override
    public int getFreeSpace() {
        return 90 - dieList.size();
    }
    // TODO: Creare un attributo privato che permetta di prendere un dado uno alla volta. Direi che una coda (Queue<Die>) sia adatta allo scopo. Queue è però un'interfaccia. Cerca da solo come crearla (non va implementata l'interfaccia)
    // TODO: Creare un metodo privato (chiamato dal costruttore, forse) che generi la coda di 90 dadi, 18 per colore, 3 per sfumatura.
    // TODO: C'è un metodo più efficiente? (Che non involva l'allocazione di 90 dadi in memoria)


}


