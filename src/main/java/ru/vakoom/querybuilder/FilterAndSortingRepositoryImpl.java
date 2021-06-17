package ru.vakoom.querybuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.compile.CriteriaQueryTypeQueryAdapter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilterAndSortingRepositoryImpl<BE extends BasicEntity> implements ru.gunmarket.querybuilder.FilterAndSortingRepository<BE> {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ru.gunmarket.querybuilder.QueryBuilder queryBuilder;

    @Override
    public List<BE> findAllByParameters(Map<String, String> requestParams, Pageable pageable, Class entityClass) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BE> criteriaQuery =
                queryBuilder.createCriteriaQueryFromParamMap(criteriaBuilder, requestParams, entityClass);
        queryBuilder.addSort(criteriaBuilder, criteriaQuery, pageable);
        TypedQuery<BE> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery = addPagination(typedQuery, pageable);
        try {
            CriteriaQueryTypeQueryAdapter<BE> typedQuery1 = (CriteriaQueryTypeQueryAdapter<BE>) typedQuery;
            log.info("SQL QUERY: {}", typedQuery1.getQueryString());
        } catch (Exception e) {

        }
        return typedQuery.getResultList();
    }

    private TypedQuery<BE> addPagination(TypedQuery<BE> typedQuery, Pageable pageable) {
        typedQuery.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery;
    }

}
