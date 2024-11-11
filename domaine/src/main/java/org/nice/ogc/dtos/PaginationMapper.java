package org.nice.ogc.dtos;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginationMapper {

    public static <T, U> PaginatedResponse<U> map(Page<T> page, Function<T, U> converter) {
        // Convert the content of the page using the provided converter
        List<U> content = page.getContent()
                .stream()
                .map(converter)
                .collect(Collectors.toList());

        // Build and return the PaginatedResponse
        return new PaginatedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

}
