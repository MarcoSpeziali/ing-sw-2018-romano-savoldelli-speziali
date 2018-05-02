package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;

import java.util.List;
import java.util.Optional;

public class ConstraintGroup implements EvaluableConstraint {

    /**
     * The constraint id.
     */
    private String id;

    /**
     * The constraints of the {@link ConstraintGroup}.
     */
    private List<EvaluableConstraint> constraints;

    /**
     * @return The constraint id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The constraints of the {@link ConstraintGroup}.
     */
    public List<EvaluableConstraint> getConstraints() {
        return this.constraints;
    }

    /**
     * @param id The constraint id.
     * @param constraints The constraints of the {@link ConstraintGroup}.
     */
    public ConstraintGroup(String id, List<EvaluableConstraint> constraints) {
        this.id = id;
        this.constraints = constraints;
    }

    @Override
    public boolean evaluate(Context context) {
        Optional<Boolean> res = this.constraints.stream()
                .map(constraint -> constraint.evaluate(context))
                .reduce((c1, c2) -> c1 && c2);

        return res.isPresent() ? res.get() : true;
    }
}
