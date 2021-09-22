package com.game.model.specification;

import com.game.entity.Player;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PlayerSpecifications {
    public static Specification<Player> containPartOfValueString(String field, String partOfValue) {
        return (Specification<Player>) (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(field), '%' + partOfValue + '%');
    }

    public static Specification<Player> betweenDates(String field, Long after, Long before) {
        if (after != null && before != null) {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.between(root.get(field), new Date(after), new Date(before));
        } else if (after != null) {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), new Date(after));
        } else {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(field), new Date(before));
        }
    }

    public static Specification<Player> betweenNumbers(String field, Integer min, Integer max) {
        if (min != null && max != null) {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.between(root.get(field), min, max);
        } else if (min != null) {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), min);
        } else {
            return (root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get(field), max);
        }
    }

    public static Specification<Player> withEnum(String field, Object value) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(field), value);
    }

    public static Specification<Player> withBoolean(String field, Boolean value) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(field), value);
    }

}
