package ru.yandex.explore.request;

import ru.yandex.explore.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.explore.request.dto.RequestDto;
import ru.yandex.explore.request.dto.RequestStatus;

import java.util.List;

public class RequestMapper {
    public static RequestDto mapRequestToRequestDto(Request request) {
        final RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());

        return requestDto;
    }

    public static EventRequestStatusUpdateResultDto mapToStatusUpdateResultDto(List<Request> requests) {
        final EventRequestStatusUpdateResultDto resultDto = new EventRequestStatusUpdateResultDto();
        RequestDto requestDto;

        for (Request request : requests) {
            if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                requestDto = RequestMapper.mapRequestToRequestDto(request);
                resultDto.addConfirmedRequest(requestDto);
            } else {
                requestDto = RequestMapper.mapRequestToRequestDto(request);
                resultDto.addRejectedRequest(requestDto);
            }
        }

        return resultDto;
    }
}
