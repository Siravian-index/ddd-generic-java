package co.com.sofka.business.asyn;

import co.com.sofka.business.generic.UseCase;
import co.com.sofka.business.support.ResponseEvents;
import co.com.sofka.domain.generic.DomainEvent;

import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Publisher event.
 */
public final class PublisherEvent extends SubmissionPublisher<DomainEvent> implements UseCase.UseCaseFormat<ResponseEvents> {
    private static final Logger logger = Logger.getLogger(PublisherEvent.class.getName());

    @Override
    public void onResponse(ResponseEvents responseEvents) {
        logger.log(Level.INFO, "Events processed {0}", responseEvents.getDomainEvents().size());
        responseEvents.getDomainEvents().forEach(this::submit);
    }

    @Override
    public void onError(RuntimeException exception) {
        getSubscribers()
                .forEach(sub -> sub.onError(exception));
    }


}
