package it.polimi.ingsw.core.instructions;

import it.polimi.ingsw.core.Context;

/**
 * TODO: docs
 */
public class TestingInstruction extends Instruction {

    /**
     * Stores the current listener.
     */
    private OnTestingInstructionRunEvent listener;

    @Override
    public Integer run(Context context) {
        if (listener != null) {
            listener.onTestingInstructionRun(this, context);
        }

        return listener != null ? listener.getResult(this, context) : 0;
    }

    /**
     * Sets the current listener.
     * @param listener The listener
     */
    public void setListener(OnTestingInstructionRunEvent listener) {
        this.listener = listener;
    }

    public interface OnTestingInstructionRunEvent {
        void onTestingInstructionRun(TestingInstruction target, Context context);

        default Integer getResult(TestingInstruction target, Context context) {
            return 0;
        }
    }
}
