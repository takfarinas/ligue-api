package org.nice.ogc.dtos;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
public class TeamDTO {

    private Long id;

    private String name;

    private String acronym;

    private String budjet;

    private List<PlayerDTO> players;

}
