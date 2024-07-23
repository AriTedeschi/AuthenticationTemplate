package internal.management.accounts.domain.service.outbound;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.application.outbound.request.RoleSearchFilter;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.model.RoleEntity;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.SupportedLocales;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class RoleSearchServiceImpl implements RoleSearchService {
    private static final String MULTIPLE_VALUES_DELIMITER = ",";
    private EntityManager entityManager;
    private RoleRepository repository;

    public RoleSearchServiceImpl(EntityManager entityManager, RoleRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    @Override
    public Page<RoleRegisterRequest> search(RoleSearchFilter request, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<RoleEntity> criteriaQuery = criteriaBuilder.createQuery(RoleEntity.class);

        Root<RoleEntity> roleRoot = criteriaQuery.from(RoleEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        addPredicate(predicates, request.roleId(), "id", criteriaBuilder, roleRoot, false);
        addPredicate(predicates, request.roleName(), "name", criteriaBuilder, roleRoot, true);

        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<RoleEntity> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<RoleEntity> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(UserEntity.class)));
        entityManager.createQuery(countQuery);

        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<RoleRegisterRequest> responseList = resultList.stream()
                .map(item -> new RoleRegisterRequest(item.getId(), item.getName()))
                .collect(Collectors.toList());
        return new PageImpl<>(responseList, pageable, totalRecords);
    }

    @Override
    public RoleRegisterRequest byId(Integer roleId, String lang) {
        RoleEntity entity = repository.findById(roleId).orElseThrow(()->new ValidationException(Map.of("roleId",SupportedLocales.getMessage(lang, "register.roleId.nonexistent"))));
        return new RoleRegisterRequest(entity.getId(),entity.getName());
    }

    private void addPredicate(List<Predicate> predicates, String field, String fieldColumn,
                             CriteriaBuilder criteriaBuilder, Root<RoleEntity> userRoot, boolean isLike) {
        Optional.ofNullable(field)
                .map(this::getValue)
                .map(items -> items.collect(Collectors.toList()))
                .ifPresent(items -> {
                    Path<String> fieldName = extractField(fieldColumn, userRoot);
                    Predicate predicate =
                            (items.size() == 1) ?
                                    likeOrEquals(criteriaBuilder, isLike, items, fieldName)
                                    : fieldName.in(items);
                    predicates.add(predicate);
                });
    }

    private static Predicate likeOrEquals(CriteriaBuilder criteriaBuilder, boolean isLike, List<String> items, Path<String> fieldName) {
        return isLike ? criteriaBuilder.like(fieldName, items.get(0) + "%") : criteriaBuilder.equal(fieldName, items.get(0));
    }

    private static Path<String> extractField(String fieldColumn, Root<RoleEntity> userRoot) {
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
