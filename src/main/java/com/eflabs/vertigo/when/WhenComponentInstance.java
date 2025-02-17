package com.eflabs.vertigo.when;

import com.englishtown.promises.Promise;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import net.kuujo.vertigo.context.ComponentContext;
import net.kuujo.vertigo.instance.ComponentInstance;
import net.kuujo.vertigo.message.VertigoMessage;

import java.util.function.Function;

/**
 * When wrapper over a Vertigo ComponentInstance.
 */
public interface WhenComponentInstance {

    /**
     * Returns a wrapped input collector
     * @return Wrapped input collector
     */
    WhenInputCollector input();

    /**
     * Returns a wrapped output collector
     * @return Wrapped output collector
     */
    WhenOutputCollector output();

    /**
     * Creates a handler wrapper useful for bridging from when to async handlers.
     * @param completeHandler The handler to call.
     * @param <T> The type of promise the handler should return.
     * @return A handler for bridging from when to async handlers
     */
    <T> WhenVertigoHandler<T> handler(Handler<AsyncResult<Void>> completeHandler);

    /**
     * Creates a handler wrapper useful for bridging from when to Vertigo messages (for acking/failing messages).
     * @param message The message to ack or fail.
     * @param <T> The type of promise the handler should return.
     * @return A handler useful for bridging from when to Vertigo
     */
    <T> WhenVertigoHandler<T> handler(VertigoMessage<?> message);

    /**
     * Convenience method for chaining an async handler ack/fail.
     *
     * For example:
     *  WhenComponentInstance ci;
     *      [...]
     *      .then(ci.onSuccess(completeHandler), ci.onReject(completeHandler));
     *
     * @param completeHandler The handler to call.
     * @param <T> The type of promise the handler should return.
     * @return A function delegate that handles the error and forwards the result.
     */
    default  <T> Function<Throwable, Promise<T>> onReject(Handler<AsyncResult<Void>> completeHandler) {
        return this.<T>handler(completeHandler)::onReject;
    }

    /**
     * Convenience method for chaining an async handler ack/fail.
     *
     * For example:
     *  WhenComponentInstance ci;
     *      [...]
     *      .then(ci.onSuccess(completeHandler), ci.onReject(completeHandler));
     *
     * @param completeHandler The handler to call.
     * @param <T> The type of promise to return.
     * @return A function delegate that handles the success and forwards the result.
     */
    default <T> Function<T, Promise<T>> onSuccess(Handler<AsyncResult<Void>> completeHandler) {
        return this.<T>handler(completeHandler)::onSuccess;
    }

    /**
     * Returns the component context
     * @return The context.
     */
    ComponentContext context();

    /**
     * Returns the component instance
     * @return The component instance.
     */
    ComponentInstance componentInstance();

}
