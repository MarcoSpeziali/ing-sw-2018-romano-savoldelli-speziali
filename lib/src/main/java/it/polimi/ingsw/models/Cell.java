package it.polimi.ingsw.models;

import it.polimi.ingsw.core.Die;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.locations.AlreadyPutException;

// FIXME: Cell dovrebbe implementare RandomPutLocation e RandomPickLocation
public class Cell /* implements RandomPutLocation, RandomPickLocation */ {

    private GlassColor cellColor;
    private Integer shade = 0;
    private Die die = null;

    public Cell(GlassColor cellColor) {
        this.cellColor = cellColor;
    }

    public Cell(Integer shade) {
        this.shade = shade;
    }

    public Cell() {
    }

    // FIXME: L'eccezione NullPointerException si riferisce all'accesso a metodi/attributi di una classe che
    // FIXME: però non è instanziata in memoria. Non ha gran senso qui. Se la cella non ha colore dovrebbe
    // FIXME: ritornare semplicemente null. Altrimenti come si fa a sapere il colore della cella?
    // FIXME: In più NullPointerException eredita da RuntimeException, come tutte le RuntimeException essa non va
    // FIXME: eplicitamente dichiarata come eccezione generata dal metodo.
    public GlassColor getCellColor() throws NullPointerException {
        if (this.cellColor != null) {
            return this.cellColor;
        }
        else throw new NullPointerException("This cell has no color!");
    }

    // FIXME: L'eccezione NullPointerException si riferisce all'accesso a metodi/attributi di una classe che
    // FIXME: però non è instanziata in memoria. Non ha gran senso qui. Se la cella è non ha una sfumatura dovrebbe
    // FIXME: ritorare semplicemente 0. Altrimenti come si fa a sapere la sfumatura della cella?
    // FIXME: In più NullPointerException eredita da RuntimeException, come tutte le RuntimeException essa non va
    // FIXME: eplicitamente dichiarata come eccezione generata dal metodo.
    public int getShade() throws NullPointerException {
        if (this.shade != null) {
            return this.shade;
        }
        else throw new NullPointerException("This cell has no shade!");
    }

    public boolean isOccupied() {
        return this.die != null;
    }

    // FIXME: Ritengo più utile che AlreadyPutException erediti da RuntimeException perché mettere un try
    // FIXME: e catch ogni volta che si cerca di mettere un dado è abbastanza fastidioso.
    /*@Override*/
    public void putDie(Die die) throws AlreadyPutException {
        if (this.die == null) this.die = die;
        else {
            throw new AlreadyPutException("A die has already been put on this cell!");
        }
    }

    /*
    @Override
    public int getFreeSpace() {
        return 0;
    }
    */

    // FIXME: L'eccezione NullPointerException si riferisce all'accesso a metodi/attributi di una classe che
    // FIXME: però non è instanziata in memoria. Non ha gran senso qui. Se la cella non ha data è corretto che
    // FIXME: venga generata un'eccezione, ma credo che crearne una custom sia più corretto.
    // FIXME: Ad esempio PutException, che ritengo più utile erediti da RuntimeException perché mettere un try
    // FIXME: e catch ogni volta che si cerca di prendere un dado è abbastanza fastidioso.
    /*@Override*/
    public Die pickDie() throws NullPointerException {
        if (this.die != null) {
            Die picked = die;
            die = null;
            return picked;
        }
        else {
            throw new NullPointerException("No die has been found to be picked!");
        }
    }

    /*
    @Override
    public int getNumberOfDice() {
        return 0;
    }
    */
}
