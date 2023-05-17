package ru.yandex.explore.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventRepository;
import ru.yandex.explore.event.dto.EventState;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.request.dto.ParticipationRequestDto;
import ru.yandex.explore.user.User;
import ru.yandex.explore.user.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto addNewRequest(Long requesterId, Long eventId) {
        final User requester = userRepository.findById(requesterId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id = %d was not found", requesterId)));
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Event with id = %d was not found", eventId)));
        final ParticipationRequest request = new ParticipationRequest();

        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);

        if (event.isRequestModeration()) {
            request.setStatus(EventState.PENDING);
        } else {
            request.setStatus(EventState.CONFIRMED);
        }
        ParticipationRequest addedRequest = repository.save(request);

        return ParticipationRequestMapper.mapRequest2RequestDto(addedRequest);
    }
}
