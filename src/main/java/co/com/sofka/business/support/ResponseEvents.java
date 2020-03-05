package co.com.sofka.business.support;

import co.com.sofka.business.generic.UseCase;
import co.com.sofka.domain.generic.DomainEvent;

import java.util.List;

/**
 * The type Response events.
 *
 * @author Raul .A Alzate
 * @version 1.0
 * @since 2019 -03-01
 */
public final class ResponseEvents implements UseCase.ResponseValues {

    private final List<DomainEvent> events;

    /**
     * Instantiates a new Response events.
     *
     * @param events the events
     */
    public ResponseEvents(List<DomainEvent> events) {
        this.events = events;
    }

    /**
     * Gets domain events.
     *
     * @return the domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return events;
    }
}