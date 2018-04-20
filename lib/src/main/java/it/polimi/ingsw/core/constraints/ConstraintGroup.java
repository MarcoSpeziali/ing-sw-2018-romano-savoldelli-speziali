package it.polimi.ingsw.core.constraints;

import it.polimi.ingsw.core.Context;

import java.util.Optional;
import java.util.Set;

public class ConstraintGroup implements EvaluableConstraint {

    private String id;

    private Set<EvaluableConstraint> constraints;

    public Set<EvaluableConstraint> getConstraints() {
        return this.constraints;
    }

    public String getId() {
        return id;
    }

    public ConstraintGroup(String id, Set<EvaluableConstraint> constraints) {
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
