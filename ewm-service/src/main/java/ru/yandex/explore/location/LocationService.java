package ru.yandex.explore.location;

import ru.yandex.explore.location.dto.LocationDto;

public interface LocationService {
    Location addNewLocation(LocationDto locationDto);
}
