package org.nice.ogc.controllers;

import jakarta.validation.Valid;
import org.nice.ogc.mappers.PlayerRequestToDTOMapper;
import org.nice.ogc.requestmodel.PlayerRequest;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PaginationRequestDTO;
import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<PaginatedResponse<PlayerDTO>> getPlayers(PaginationRequestDTO paginationRequest) {
        logger.info("getPlayers called with page parameters {}", paginationRequest);
        return new ResponseEntity<>(playerService.getPlayers(paginationRequest) , HttpStatus.OK);
    }

    @PostMapping("players/create")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerRequest playerRequest) {
        logger.info("create new Player {}", playerRequest);
        var playerCreated = playerService.createPlayer(PlayerRequestToDTOMapper.fromRequestToDTO(playerRequest));

        return new ResponseEntity<>(playerCreated, HttpStatus.CREATED);
    }

    @GetMapping("/players/{playerId}/teams/{teamId}")
    public ResponseEntity<PlayerDTO> transfertPlayer(@PathVariable Long playerId, @PathVariable Long teamId) {
        logger.info("transfertPlayer called with player ID:{} and team ID: {}", playerId, teamId);
        var palyerTransfered =  playerService.transfertPlayer(playerId, teamId);
        return new ResponseEntity<>(palyerTransfered, HttpStatus.OK);
    }
}
