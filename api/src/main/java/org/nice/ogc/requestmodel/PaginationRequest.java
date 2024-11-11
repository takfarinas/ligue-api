package org.nice.ogc.requestmodel;

import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class PaginationRequest {
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private Sort.Direction direction = Sort.Direction.ASC;
}
