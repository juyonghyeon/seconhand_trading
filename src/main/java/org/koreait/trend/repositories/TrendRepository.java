package org.koreait.trend.repositories;

import org.koreait.trend.entities.Trend;
import org.springframework.data.repository.ListCrudRepository;

public interface TrendRepository extends ListCrudRepository<Trend, Long> {

}
