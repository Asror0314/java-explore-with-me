package ru.yandex.explore.request;

import ru.yandex.explore.request.dto.ParticipationRequestDto;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto mapRequest2RequestDto(ParticipationRequest request) {
        final ParticipationRequestDto requestDto = new ParticipationRequestDto();

        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());

        return requestDto;
    }
}
