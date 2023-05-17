package ru.yandex.explore.location;

import ru.yandex.explore.location.dto.LocationDto;

public class LocationMapper {
    public static Location mapLocationDto2Location(LocationDto locationDto) {
        final Location location = new Location();

        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public static LocationDto mapLocation2LocationDto(Location location) {
        final LocationDto locationDto = new LocationDto();

        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }
}
