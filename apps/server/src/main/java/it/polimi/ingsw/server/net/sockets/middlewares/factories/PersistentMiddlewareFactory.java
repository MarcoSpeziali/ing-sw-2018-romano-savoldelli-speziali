package it.polimi.ingsw.server.net.sockets.middlewares.factories;

import it.polimi.ingsw.server.net.sockets.middlewares.Middleware;

import java.util.function.Supplier;

public final class PersistentMiddlewareFactory<T extends Middleware> implements MiddlewareFactory<T> {

    private T cached;
    private final Supplier<T> middlewareSupplier;

    public static <T extends Middleware> MiddlewareFactory<T> persistentMiddleware(Supplier<T> middlewareSupplier) {
        return new PersistentMiddlewareFactory<>(middlewareSupplier);
    }

    private PersistentMiddlewareFactory(Supplier<T> middlewareSupplier) {
        this.middlewareSupplier = middlewareSupplier;
    }

    @Override
    public T instantiateMiddleware() {
        if (cached == null) {
            cached = middlewareSupplier.get();
        }

        return cached;
    }
}
