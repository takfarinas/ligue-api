package org.nice.ogc.mappers;

import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.entities.Player;
import org.nice.ogc.entities.projections.PlayerView;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper{

    public static PlayerDTO fromEntityToDTO(Player model) {
        return PlayerDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .position(model.getPosition())
                .build();
    }

    public static Player fromDTOToEntity(PlayerDTO dto) {
        return Player.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .position(dto.getPosition())
                .build();
    }

    public static PlayerDTO fromViewToDTO(PlayerView model) {
        return PlayerDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .position(model.getPosition())
                .build();
    }
}
