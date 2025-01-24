package ru.practicum.comments;

import java.util.Optional;

public enum SortComments {

    DESC,
    ASC;

    public static Optional<SortComments> from(String sortString) {
        for (SortComments sort : SortComments.values()) {
            if (sort.name().equalsIgnoreCase(sortString)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }

}