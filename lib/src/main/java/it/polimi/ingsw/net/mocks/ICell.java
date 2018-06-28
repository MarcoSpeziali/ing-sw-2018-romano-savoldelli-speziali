package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.utils.io.json.JSONElement;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface ICell extends JSONSerializable {

    @JSONElement("color")
    GlassColor getColor();
    @JSONElement("shade")
    Integer getShade();
    @JSONElement("die")
    IDie getDie();
    @JSONElement("uuid")
    int getUUID();

    default boolean isOccupied() {
        return this.getDie() != null;
    }

    default boolean isBlank() {
        return this.getColor() == null && this.getShade() == 0;
    }

    /**
     * Compares a specified {@link Die} with the cell.
     *
     * @param die         an instance of the {@link Die} to compare.
     * @param ignoreColor the boolean flag which avoids color control.
     * @param ignoreShade the boolean flag which avoids shade control.
     * @return true if cell matches with color, shade or is blank, false otherwise.
     */
    default boolean matchesOrBlank(IDie die, boolean ignoreColor, boolean ignoreShade) {
        return this.isBlank() ||
                (this.getColor() == null || ignoreColor || this.getColor().equals(die.getColor())) &&
                        (this.getShade().equals(0) || ignoreShade || this.getShade().equals(die.getShade()));
    }
}
