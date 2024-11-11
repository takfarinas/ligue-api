package org.nice.ogc.mappers;

import org.nice.ogc.requestmodel.TeamRequest;
import org.nice.ogc.dtos.TeamDTO;


public class TeamRequestToDTOMapper {

    public static TeamDTO fromRequestToDTO(TeamRequest request) {

        if (request == null) {
            return null;
        }

        return TeamDTO.builder()
                .name(request.getName())
                .acronym(request.getAcronym())
                .budjet(request.getBudjet())
                .players(request.getPlayers().stream()
                        .map(PlayerRequestToDTOMapper::fromRequestToDTO).toList())
                .build();
    }
}
