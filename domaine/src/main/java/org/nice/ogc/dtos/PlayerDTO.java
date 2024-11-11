package org.nice.ogc.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerDTO {

    private Long id;

    private String name;

    private String email;

    private String position;
}
