package ru.yandex.explore.request;

import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.RequestStatus;

import java.util.List;

public class RequestMapper {
    public static RequestDto mapRequest2RequestDto(Request request) {
        final RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());

        return requestDto;
    }

    public static EventRequestStatusUpdateResultDto map2StatusUpdateResultDto(List<Request> requests) {
        final EventRequestStatusUpdateResultDto resultDto = new EventRequestStatusUpdateResultDto();
        RequestDto requestDto;

        for (Request request : requests) {
            if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                requestDto = RequestMapper.mapRequest2RequestDto(request);
                resultDto.addConfirmedRequest(requestDto);
            } else {
                requestDto = RequestMapper.mapRequest2RequestDto(request);
                resultDto.addRejectedRequest(requestDto);
            }
        }

        return resultDto;
    }
}