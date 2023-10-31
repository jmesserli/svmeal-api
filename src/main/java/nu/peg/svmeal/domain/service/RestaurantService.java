package nu.peg.svmeal.domain.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.domain.model.RestaurantDto;
import nu.peg.svmeal.domain.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository repository;

  public List<RestaurantDto> getRestaurantDtos() {
    return repository.getAllRestaurants();
  }
}
