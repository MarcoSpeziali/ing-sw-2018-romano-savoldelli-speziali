package it.polimi.ingsw.server.compilers.instructions.predicates;

import it.polimi.ingsw.server.compilers.commons.ParametersCompiler;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirective;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PredicateCompiler {

    private static final String PREDICATE_CALL_REGEX = "^(?<id>[a-zA-Z_][a-zA-Z0-9_]+)(?<params>[^\\[]+)*(\\[(?<opts>.+)])?$";

    private PredicateCompiler() {
    }

    /**
     * Compiles a predicate call into its corresponding {@link CompiledPredicate}.
     *
     * @param predicateCall the predicate call
     * @param directives    the {@link List} of {@link PredicateCompiler}
     * @return the compiled predicate as {@link CompiledPredicate}
     */
    public static CompiledPredicate compile(String predicateCall, List<PredicateDirective> directives) {
        String predicateId = getPredicateId(predicateCall);

        PredicateDirective directive = directives.stream()
                .filter(predicateDirective -> predicateDirective.getId().equals(predicateId))
                .findFirst()
                .orElseThrow(() -> new UnrecognizedPredicateException(predicateId));

        return new CompiledPredicate(
                predicateId,
                directive.getTargetClass(),
                ParametersCompiler.parseParameters(predicateCall, directive, PREDICATE_CALL_REGEX)
        );
    }

    /**
     * @param rawPredicate the predicate as string
     * @return the id of the predicate
     */
    private static String getPredicateId(String rawPredicate) {
        Pattern pattern = Pattern.compile(PREDICATE_CALL_REGEX);
        Matcher matcher = pattern.matcher(rawPredicate);

        if (matcher.find()) {
            return matcher.group("id");
        }

        throw new IllegalArgumentException();
    }
}
