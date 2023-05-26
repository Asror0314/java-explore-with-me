package ru.yandex.explore.request.dto;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EventRequestStatusUpdateResultDto {
    private List<RequestDto> confirmedRequests = new ArrayList<>();
    private List<RequestDto> rejectedRequests = new ArrayList<>();

    public void addConfirmedRequest(RequestDto requestDto) {
        confirmedRequests.add(requestDto);
    }

    public void addRejectedRequest(RequestDto requestDto) {
        rejectedRequests.add(requestDto);
    }
}
