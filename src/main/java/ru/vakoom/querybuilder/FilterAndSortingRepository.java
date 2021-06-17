package ru.vakoom.querybuilder;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FilterAndSortingRepository<BE extends BasicEntity> {
    List<BE> findAllByParameters(Map<String, String> requestParams, Pageable pageable, Class entityClass);
}
