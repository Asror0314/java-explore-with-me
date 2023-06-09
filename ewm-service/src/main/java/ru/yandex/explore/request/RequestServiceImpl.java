package ru.yandex.explore.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventRepository;
import ru.yandex.explore.event.EventService;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.exception.EditRulesException;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.RequestStatus;
import ru.yandex.explore.request.dto.EventRequestStatusUpdateRequest;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String dateTime = LocalDateTime.now().format(formatter);
    private final LocalDateTime createdOn = LocalDateTime.parse(dateTime, formatter);

    @Override
    @Transactional
    public RequestDto addNewRequest(Long requesterId, Long eventId) {
        final User requester = userService.findUserById(requesterId);
        final Event event = eventService.findEventById(eventId);
        validParticipationRequest(event, requester);

        final Request request = new Request();
        request.setCreated(createdOn);
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

        return RequestMapper.mapRequestToRequestDto(addedRequest);
    }

    @Override
    @Transactional
    public RequestDto cancelRequestRequester(Long requesterId, Long requestId) {
        userService.findUserById(requesterId);
        final Request request = findRequestById(requestId);

        if (!request.getRequester().getId().equals(requesterId)) {
            throw new EditRulesException(String.format("Requester id=%d mismatch", requesterId));
        }

        if (RequestStatus.CONFIRMED.equals(request.getStatus())) {
            final Event event = request.getEvent();

            if (event.getParticipantLimit() > event.getConfirmedRequests() - 1) {
                event.setLimitAvailable(true);
            }
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);
        request.setCreated(createdOn);
        final Request canceledRequest = repository.save(request);

        return RequestMapper.mapRequestToRequestDto(canceledRequest);
    }

    @Override
    public List<RequestDto> getRequestRequester(Long requesterId) {
        final User requester = userService.findUserById(requesterId);
        final List<Request> requests = repository.findAllByRequester(requester);

        return requests
                .stream()
                .map(RequestMapper::mapRequestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestInitiator(Long initiatorId, Long eventId) {
        final User initiator = userService.findUserById(initiatorId);
        eventService.findEventById(eventId);
        final List<Request> requests = repository.findAllByInitiator(initiator, eventId);

        return requests
                .stream()
                .map(RequestMapper::mapRequestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateRequestStatusInitiator(
            Long initiatorId,
            Long eventId,
            EventRequestStatusUpdateRequest requestInitiatorDto
    ) {
        final User initiator = userService.findUserById(initiatorId);
        final Event event = eventService.findEventById(eventId);
        validUpdateStatusRequest(event, initiator);

        final List<Request> requests = repository.findAllById(requestInitiatorDto.getRequestIds());

        switch (requestInitiatorDto.getStatus()) {
            case CONFIRMED: {
                for (Request request: requests) {
                    validStatusRequest(request);
                    confirmRequests(request, event);
                }
                eventRepository.save(event);
                break;
            }
            case REJECTED: {
                for (Request request: requests) {
                    validStatusRequest(request);
                    request.setStatus(RequestStatus.REJECTED);
                }
                break;
            }
            default: throw new NotFoundException(String.format("Unknown state: %s", requestInitiatorDto.getStatus()));
        }

        if (!event.isLimitAvailable()) {
            final List<Request> pendingRequests = repository.findAllPendingStatus();
            for (Request request: pendingRequests) {
                request.setStatus(RequestStatus.REJECTED);
            }
        }

        final List<Request> updatedRequests = repository.findAllById(requestInitiatorDto.getRequestIds());

        return RequestMapper.mapToStatusUpdateResultDto(updatedRequests);
    }

    private Request findRequestById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Request with id = %d was not found", requestId)));
    }

    private void validParticipationRequest(Event event, User requester) {
        if (repository.findAllByRequesterAndEvent(requester, event) > 0) {
            throw new EditRulesException("Can't add a repeat request");
        }

        if (event.getInitiator().equals(requester)) {
            throw new EditRulesException(String.format("Initiator id=%d of the event cannot add a request " +
                    "to participate in his event", requester.getId()));
        }

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new EditRulesException("Can't participate in an unpublished event");
        }

        if (!event.isLimitAvailable()) {
            throw new EditRulesException("The event has reached the limit of requests for participation");
        }
    }

    private void validUpdateStatusRequest(Event event, User initiator) {
        if (!event.getInitiator().equals(initiator)) {
            throw new EditRulesException(String.format("event initiator id=%d is mismatch", initiator.getId()));
        }

        if (!event.isLimitAvailable()) {
            throw new EditRulesException("The limit on applications for this event has been reached");
        }
    }

    private void confirmRequests(Request request, Event event) {
        if (event.isLimitAvailable()) {
            request.setStatus(RequestStatus.CONFIRMED);

            if (event.getParticipantLimit() <= event.getConfirmedRequests() + 1) {
                event.setLimitAvailable(false);
            }
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
    }

    private void validStatusRequest(Request request) {
        if (!RequestStatus.PENDING.equals(request.getStatus())) {
            throw new EditRulesException("Status can only be changed for applications that are in the pending state");
        }
    }
}
