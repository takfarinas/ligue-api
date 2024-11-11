package org.nice.ogc.dtos;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class PageDTO<T> {
    private List<T> content;
    private long totalElements;
}
