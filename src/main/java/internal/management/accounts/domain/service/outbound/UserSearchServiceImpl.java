package internal.management.accounts.domain.service.outbound;

import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.application.outbound.adapter.UserEntity2UserRegisterResponse;
import internal.management.accounts.application.outbound.request.SearchFilter;
import internal.management.accounts.domain.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class UserSearchServiceImpl implements UserSearchService {
    private static final String MULTIPLE_VALUES_DELIMITER = ",";
    private EntityManager entityManager;

    public UserSearchServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<UserRegisterResponse> search(SearchFilter request, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);

        Root<UserEntity> userRoot = criteriaQuery.from(UserEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        addPredicate(predicates, request.userId(), "uuid", criteriaBuilder, userRoot, false);
        addPredicate(predicates, request.userCode(), "userCode", criteriaBuilder, userRoot, true);
        addPredicate(predicates, request.email(), "email.value", criteriaBuilder, userRoot, false);
        addPredicate(predicates, request.firstName(), "fullname.firstName", criteriaBuilder, userRoot, true);
        addPredicate(predicates, request.lastName(), "fullname.lastName", criteriaBuilder, userRoot, true);

        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<UserEntity> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<UserEntity> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(UserEntity.class)));
        entityManager.createQuery(countQuery);

        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<UserRegisterResponse> responseList = resultList.stream()
                .map(item -> new UserEntity2UserRegisterResponse(item,true).getInstance())
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, totalRecords);
    }

    private void addPredicate(List<Predicate> predicates, String field, String fieldColumn,
                             CriteriaBuilder criteriaBuilder, Root<UserEntity> userRoot, boolean isLike) {
        Optional.ofNullable(field)
                .map(this::getValue)
                .map(items -> items.collect(Collectors.toList()))
                .ifPresent(items -> {
                    Path<String> fieldName = extractField(fieldColumn, userRoot);

                    if(fieldColumn.equals("uuid")){
                        Predicate predicate =
                                (items.size() == 1) ? criteriaBuilder.equal(fieldName, UUID.fromString(items.get(0)))
                                                    : fieldName.in(items.stream().map(UUID::fromString).toList());
                        predicates.add(predicate);
                    }else{
                        Predicate predicate =
                                (items.size() == 1) ?
                                        likeOrEquals(criteriaBuilder, isLike, items, fieldName)
                                        : fieldName.in(items);
                        predicates.add(predicate);
                    }
                });
    }

    private static Predicate likeOrEquals(CriteriaBuilder criteriaBuilder, boolean isLike, List<String> items, Path<String> fieldName) {
        return isLike ? criteriaBuilder.like(fieldName, items.get(0) + "%") : criteriaBuilder.equal(fieldName, items.get(0));
    }

    private static Path<String> extractField(String fieldColumn, Root<UserEntity> userRoot) {
        Path<String> fieldName = null;
        for(String c : fieldColumn.split("\\."))
            fieldName = (fieldName == null) ? userRoot.get(c) : fieldName.get(c);
        return fieldName;
    }

    public Stream<String> getValue(String field){
        return (field.contains(MULTIPLE_VALUES_DELIMITER)) ?
            Stream.of(field.split(MULTIPLE_VALUES_DELIMITER)) : Stream.of(field);
    }
}
