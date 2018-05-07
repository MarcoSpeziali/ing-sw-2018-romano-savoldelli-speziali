package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;

import java.util.Arrays;

public class CountDiagonalInstruction extends Instruction {

    /**
     * Represents the type of filtering.
     */
    private final DieFilter filterBy;

    /**
     * If set to a value different then {@code 0} the {@link #run(Context)}
     * method will analyze only cells with that shade.
     */
    private final Integer strictShade;

    /**
     * If set to a value different then {@code null} the {@link #run(Context)}
     * method will analyze only cells with that color.
     */
    private final GlassColor strictColor;

    /**
     * A boolean 2D matrix containing the cells already counted.
     */
    private boolean[][] alreadyCountedDice;

    public CountDiagonalInstruction(DieFilter filterBy, Integer strictShade, GlassColor strictColor) {
        this.filterBy = filterBy;
        this.strictShade = strictShade;
        this.strictColor = strictColor;
    }

    @Override
    public Integer run(Context context) {
        // Retrieves the cells from the window
        Cell[][] cells = ((Window) context.get(Context.WINDOW)).getCells();
        // Maps the cells matrix to a die matrix
        Die[][] diceMatrix = Arrays.stream(cells)
                .map(cellsRow ->
                        Arrays.stream(cellsRow)
                                .map(Cell::getDie)
                                .toArray(Die[]::new)
                ).toArray(Die[][]::new);

        this.initializeAlreadyCountedDice(diceMatrix);

        // Initializes the score to zero
        Integer score = 0;

        for (int i = 0; i < diceMatrix.length; i++) {
            for (int j = 0; j < diceMatrix[i].length; j++) {
                if (this.alreadyCountedDice[i][j]) {
                    continue;
                }

                Integer result;
                if (this.filterBy == DieFilter.COLOR) {
                    result = this.countDiagonallyRecursivelyByColor(diceMatrix, i, j, diceMatrix[i][j].getColor(), diceMatrix.length, diceMatrix[0].length);
                } else {
                    result = this.countDiagonallyRecursivelyByShade(diceMatrix, i, j, diceMatrix[i][j].getShade(), diceMatrix.length, diceMatrix[0].length);
                }

                // If the result is 1 that no diagonal pattern where found
                score += result == 1 ? 0 : result;
            }
        }

        return score;
    }

    /**
     * @param diceMatrix The dice matrix
     */
    private void initializeAlreadyCountedDice(Die[][] diceMatrix) {
        alreadyCountedDice = new boolean[diceMatrix.length][diceMatrix[0].length];

        for (int i = 0; i < diceMatrix.length; i++) {
            for (int j = 0; j < diceMatrix[i].length; j++) {
                if (strictShade != 0 && !diceMatrix[i][j].getShade().equals(strictShade)) {
                    alreadyCountedDice[i][j] = true;
                } else {
                    alreadyCountedDice[i][j] =
                            strictColor != null &&
                            !diceMatrix[i][j].getColor().equals(strictColor);
                }
            }
        }
    }

    /**
     * Counts TODO: continue
     * @param diceMatrix
     * @param i
     * @param j
     * @param glassColor
     * @param rowCount
     * @param columnCount
     * @return
     */
    private Integer countDiagonallyRecursivelyByColor(Die[][] diceMatrix, int i, int j, GlassColor glassColor, int rowCount, int columnCount) {
        if (!diceMatrix[i][j].getColor().equals(glassColor) || this.alreadyCountedDice[i][j]) {
            return 0;
        }

        this.alreadyCountedDice[i][j] = true;

        Integer partialResult = 0;

        if (case1(i, j)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j + 1, glassColor, rowCount, columnCount);
        }
        else if (case2(i, j, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j + 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j - 1, glassColor, rowCount, columnCount);
        }
        else if (case3(i, j, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j - 1, glassColor, rowCount, columnCount);
        }
        else if (case4(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j - 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j - 1, glassColor, rowCount, columnCount);
        }
        else if (case5(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j - 1, glassColor, rowCount, columnCount);
        }
        else if (case6(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j - 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j + 1, glassColor, rowCount, columnCount);
        }
        else if (case7(i, j, rowCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j + 1, glassColor, rowCount, columnCount);
        }
        else if (case8(i, j, rowCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j + 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j + 1, glassColor, rowCount, columnCount);
        }
        else if (case9(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j + 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i - 1, j - 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j + 1, glassColor, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByColor(diceMatrix, i + 1, j - 1, glassColor, rowCount, columnCount);
        }

        // The result of case9 should always be true if case1-8 were false. Just for consistency and readability
        // the last branch is an else if, it could be an else.

        // +1 because the current cell has been analyzed
        return partialResult + 1;
    }

    /**
     * @param diceMatrix
     * @param i
     * @param j
     * @param shade
     * @param rowCount
     * @param columnCount
     * @return
     */
    private Integer countDiagonallyRecursivelyByShade(Die[][] diceMatrix, int i, int j, Integer shade, int rowCount, int columnCount) {
        if (!diceMatrix[i][j].getShade().equals(shade) || this.alreadyCountedDice[i][j]) {
            return 0;
        }

        this.alreadyCountedDice[i][j] = true;

        Integer partialResult = 0;

        if (case1(i, j)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j + 1, shade, rowCount, columnCount);
        }
        else if (case2(i, j, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j + 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j - 1, shade, rowCount, columnCount);
        }
        else if (case3(i, j, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j - 1, shade, rowCount, columnCount);
        }
        else if (case4(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j - 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j - 1, shade, rowCount, columnCount);
        }
        else if (case5(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j - 1, shade, rowCount, columnCount);
        }
        else if (case6(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j - 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j + 1, shade, rowCount, columnCount);
        }
        else if (case7(i, j, rowCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j + 1, shade, rowCount, columnCount);
        }
        else if (case8(i, j, rowCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j + 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j + 1, shade, rowCount, columnCount);
        }
        else if (case9(i, j, rowCount, columnCount)) {
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j + 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i - 1, j - 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j + 1, shade, rowCount, columnCount);
            partialResult += countDiagonallyRecursivelyByShade(diceMatrix, i + 1, j - 1, shade, rowCount, columnCount);
        }

        // The result of case9 should always be true if case1-8 were false. Just for consistency and readability
        // the last branch is an else if, it could be an else.

        // +1 because the current cell has been analyzed
        return partialResult + 1;
    }

    /**
     * 1: no left, no above
     * [O][ ][ ][ ][ ]
     * [ ][x][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     */
    private static boolean case1(int i, int j) {
        return i == 0 && j == 0;
    }

    /**
     * 2: no above, yes left & right & below
     * [ ][O][ ][ ][ ]
     * [x][ ][x][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][O][ ][ ]
     * [ ][x][ ][x][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][ ][O][ ]
     * [ ][ ][x][ ][x]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     */
    private static boolean case2(int i, int j, int columnCount) {
        return i == 0 && j < columnCount - 1;
    }

    /**
     * 3: no right & above, yes left & below
     * [ ][ ][ ][ ][O]
     * [ ][ ][ ][x][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     */
    private static boolean case3(int i, int j, int columnCount) {
        return i == 0 && j == columnCount - 1;
    }

    /**
     * 4: no right, yes left & above & below
     * [ ][ ][ ][x][ ]
     * [ ][ ][ ][ ][O]
     * [ ][ ][ ][x][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][x][ ]
     * [ ][ ][ ][ ][O]
     * [ ][ ][ ][x][ ]
     */
    private static boolean case4(int i, int j, int rowCount, int columnCount) {
        return i > 0 && i < rowCount - 1 && j == columnCount - 1;
    }

    /**
     * 5: no right & below, yes above & left
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][x][ ]
     * [ ][ ][ ][ ][O]
     */
    private static boolean case5(int i, int j, int rowCount, int columnCount) {
        return i == rowCount - 1 && j == columnCount - 1;
    }

    /**
     * 6: no below, yes above & left & right
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][ ][x][ ][x]
     * [ ][ ][ ][O][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][x][ ][x][ ]
     * [ ][ ][O][ ][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [x][ ][x][ ][ ]
     * [ ][O][ ][ ][ ]
     */
    private static boolean case6(int i, int j, int rowCount, int columnCount) {
        return i == rowCount - 1 && j > 0 && j < columnCount - 1;
    }

    /**
     * 7: no left & below, yes above & right
     * [ ][ ][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * [ ][x][ ][ ][ ]
     * [O][ ][ ][ ][ ]
     */
    private static boolean case7(int i, int j, int rowCount) {
        return i == rowCount - 1 && j == 0;
    }

    /**
     * 8: no left, yes right & above & below
     * [ ][x][ ][ ][ ]
     * [O][ ][ ][ ][ ]
     * [ ][x][ ][ ][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][x][ ][ ][ ]
     * [O][ ][ ][ ][ ]
     * [ ][x][ ][ ][ ]
     */
    private static boolean case8(int i, int j, int rowCount) {
        return i > 0 && i < rowCount - 1 && j == 0;
    }

    /**
     * 9: yes below & right & left & above
     * [x][ ][x][ ][ ]
     * [ ][O][ ][ ][ ]
     * [x][ ][x][ ][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][x][ ][x][ ]
     * [ ][ ][O][ ][ ]
     * [ ][x][ ][x][ ]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][x][ ][x]
     * [ ][ ][ ][O][ ]
     * [ ][ ][x][ ][x]
     * [ ][ ][ ][ ][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [x][ ][x][ ][ ]
     * [ ][O][ ][ ][ ]
     * [x][ ][x][ ][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][x][ ][x][ ]
     * [ ][ ][O][ ][ ]
     * [ ][x][ ][x][ ]
     * ---------------
     * [ ][ ][ ][ ][ ]
     * [ ][ ][x][ ][x]
     * [ ][ ][ ][O][ ]
     * [ ][ ][x][ ][x]
     */
    private static boolean case9(int i, int j, int rowCount, int columnCount) {
        return i > 0 && i < rowCount - 1 && j > 0 && j < columnCount - 1;
    }
}
