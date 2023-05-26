package ru.yandex.explore.request.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    @NotNull
    private Set<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
