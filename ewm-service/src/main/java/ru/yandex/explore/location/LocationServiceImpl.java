package ru.yandex.explore.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.explore.location.dto.LocationDto;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    @Override
    public Location addNewLocation(LocationDto locationDto) {
        final Location location = LocationMapper.mapLocationDto2Location(locationDto);

        return repository.save(location);
    }
}
