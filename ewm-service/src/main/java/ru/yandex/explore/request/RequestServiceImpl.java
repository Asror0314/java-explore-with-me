package ru.yandex.explore.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventRepository;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.RequestStatus;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateRequest;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public RequestDto addNewRequest(Long requesterId, Long eventId) {
        final User requester = findUserById(requesterId);
        final Event event = findEventById(eventId);
        validParticipationRequest(event, requester);

        final Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);

            if (event.getParticipantLimit() <= event.getConfirmedRequests() + 1) {
                event.setLimitAvailable(false);
            }
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        Request addedRequest = repository.save(request);

        return RequestMapper.mapRequest2RequestDto(addedRequest);
    }

    @Override
    @Transactional
    public RequestDto cancelRequestRequester(Long requesterId, Long requestId) {
        findUserById(requesterId);
        final Request request = findRequestById(requestId);

        if (request.getRequester().getId() != requesterId) {
            throw new EditRulesException(String.format("Requester id=%d mismatch", requesterId));
        }

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            final Event event = request.getEvent();

            if (event.getParticipantLimit() > event.getConfirmedRequests() - 1) {
                event.setLimitAvailable(true);
            }
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);
        request.setCreated(LocalDateTime.now());
        final Request canceledRequest = repository.save(request);

        return RequestMapper.mapRequest2RequestDto(canceledRequest);
    }

    @Override
    public List<RequestDto> getRequestRequester(Long requesterId) {
        final User requester = findUserById(requesterId);
        final List<Request> requests = repository.findAllByRequester(requester);

        return requests
                .stream()
                .map(RequestMapper::mapRequest2RequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestInitiator(Long initiatorId, Long eventId) {
        final User initiator = findUserById(initiatorId);
        findEventById(eventId);
        final List<Request> requests = repository.findAllByInitiator(initiator, eventId);

        return requests
                .stream()
                .map(RequestMapper::mapRequest2RequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateRequestStatusInitiator(
            Long initiatorId,
            Long eventId,
            EventRequestStatusUpdateRequest requestInitiatorDto
    ) {
        final User initiator = findUserById(initiatorId);
        final Event event = findEventById(eventId);
        validUpdateStatusRequest(event, initiator);

        final List<Request> requests = repository.findAllById(requestInitiatorDto.getRequestIds());

        switch (requestInitiatorDto.getStatus()) {
            case CONFIRMED: {
                for (Request request: requests) {
                    validStatusRequest(request);
                    confirmRequests(request, event);
                }
                break;
            }
            case REJECTED: {
                for (Request request: requests) {
                    validStatusRequest(request);
                    rejectRequest(request);
                }
                break;
            }
            default: throw new RuntimeException(String.format("Unknown state: %s", requestInitiatorDto.getStatus()));
        }

        if (!event.isLimitAvailable()) {
            final List<Request> pendingRequests = repository.findAllPendingStatus();
            for (Request request: pendingRequests) {
                rejectRequest(request);
            }
        }

        final List<Request> updatedRequests = repository.findAllById(requestInitiatorDto.getRequestIds());

        return RequestMapper.map2StatusUpdateResultDto(updatedRequests);
    }

    private Request findRequestById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Request with id = %d was not found", requestId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id = %d was not found", userId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id = %d was not found", eventId)));
    }

    private void validParticipationRequest(Event event, User requester) {
        if (repository.findAllByRequesterAndEvent(requester, event) > 0) {
            throw new EditRulesException("Can't add a repeat request");
        }

        if (event.getInitiator().getId() == requester.getId()) {
            throw new EditRulesException(String.format("Initiator id=%d of the event cannot add a request " +
                    "to participate in his event", requester.getId()));
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EditRulesException("Can't participate in an unpublished event");
        }

        if (!event.isLimitAvailable()) {
            throw new EditRulesException("The event has reached the limit of requests for participation");
        }
    }

    private void validUpdateStatusRequest(Event event, User initiator) {
        if (!event.getInitiator().equals(initiator)) {
            throw new EditRulesException(String.format("event initiator id={} is mismatch", initiator.getId()));
        }

        if (!event.isLimitAvailable()) {
            throw new EditRulesException("The limit on applications for this event has been reached");
        }
    }

    private void confirmRequests(Request request, Event event) {
        if (event.isLimitAvailable()) {
            request.setStatus(RequestStatus.CONFIRMED);
            repository.save(request);

            if (event.getParticipantLimit() <= event.getConfirmedRequests() + 1) {
                event.setLimitAvailable(false);
            }
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);

            eventRepository.save(event);
        } else {
            rejectRequest(request);
        }
    }

    private void rejectRequest(Request request) {
        request.setStatus(RequestStatus.REJECTED);
        repository.save(request);
    }

    private void validStatusRequest(Request request) {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new EditRulesException("Status can only be changed for applications that are in the pending state");
        }
    }
}
