package org.nice.ogc.mappers;

import org.nice.ogc.requestmodel.PlayerRequest;
import org.nice.ogc.dtos.PlayerDTO;

public class PlayerRequestToDTOMapper {

    public static PlayerDTO fromRequestToDTO(PlayerRequest request) {

        if (request == null) {
            return null;
        }

        return PlayerDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .position(request.getPosition())
                .build();
    }
}
