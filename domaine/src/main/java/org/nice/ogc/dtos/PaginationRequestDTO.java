package org.nice.ogc.dtos;

import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationRequestDTO{
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private Sort.Direction direction = Sort.Direction.ASC;
}

