package org.nice.ogc.mappers;

import org.nice.ogc.dtos.TeamDTO;
import org.nice.ogc.entities.Team;
import org.nice.ogc.entities.projections.TeamView;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public static TeamDTO fromEntityToDTO(Team model) {
        return TeamDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .acronym(model.getAcronym())
                .budjet(model.getBudjet())
                .players(model.getPlayers().stream().map(PlayerMapper::fromEntityToDTO).toList())
                .build();
    }

    public static Team fromDTOToEntity(TeamDTO dto) {
        return Team.builder()
                .id(dto.getId())
                .name(dto.getName())
                .acronym(dto.getAcronym())
                .budjet(dto.getBudjet())
                .players(dto.getPlayers().stream().map(PlayerMapper::fromDTOToEntity).toList())
                .build();
    }

    public static TeamDTO fromViewToDTO(TeamView model) {

        if (model == null) return null;

        return TeamDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .acronym(model.getAcronym())
                .budjet(model.getBudjet())
                .players(model.getPlayers().stream().map(PlayerMapper::fromViewToDTO).toList())
                .build();
    }
}
