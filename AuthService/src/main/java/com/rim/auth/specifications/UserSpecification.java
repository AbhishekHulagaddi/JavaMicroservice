package com.rim.auth.specifications;


import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import com.rim.auth.domain.User;
import com.rim.auth.model.UserFilterModel;

import java.util.*;

public class UserSpecification {

    public static Specification<User> filter(UserFilterModel filter) {

        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null)
                return cb.conjunction();

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }

            if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("firstName")),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }

            if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("lastName")),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }

            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), filter.getActive()));
            }

            if (filter.getRoleId() != null) {
                predicates.add(cb.equal(
                        root.join("role").get("roleId"),
                        filter.getRoleId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
