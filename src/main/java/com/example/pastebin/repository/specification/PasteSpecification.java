package com.example.pastebin.repository.specification;

import com.example.pastebin.enums.Access;
import com.example.pastebin.model.Paste;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class PasteSpecification {
    public static Specification<Paste> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            Predicate accessPredicate = criteriaBuilder.equal(root.get("access"), Access.PUBLIC);
            Predicate namePredicate = criteriaBuilder.equal(root.get("name"), name);
            return criteriaBuilder.and(accessPredicate,namePredicate);
        };
    }

    public static Specification<Paste> byText(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null) {
                return criteriaBuilder.conjunction();
            }
            Predicate accessPredicate = criteriaBuilder.equal(root.get("access"), Access.PUBLIC);
            Predicate textPredicate = criteriaBuilder.equal(root.get("text"), text);
            return criteriaBuilder.and(accessPredicate,textPredicate);
        };
    }
}
