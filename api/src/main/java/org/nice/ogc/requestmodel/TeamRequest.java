package org.nice.ogc.requestmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TeamRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    @NotBlank(message = "Acronym is required")
    @Size(min = 2, message = "Acronym should have at least 2 characters")
    private String acronym;

    @NotBlank(message = "Budjet is required")
    private String budjet;

    private List<PlayerRequest> players = new ArrayList<>();;
}
