package org.nice.ogc.controllers;


import jakarta.validation.Valid;
import org.nice.ogc.mappers.TeamRequestToDTOMapper;
import org.nice.ogc.requestmodel.TeamRequest;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PaginationRequestDTO;
import org.nice.ogc.dtos.TeamDTO;
import org.nice.ogc.services.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
public class TeamController {

    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams")
    public ResponseEntity<PaginatedResponse<TeamDTO>> getTeam(PaginationRequestDTO paginationRequest) {
        logger.info("Get teams from pagination, with pagoantion parameters {}", paginationRequest);
        return new ResponseEntity<>(teamService.getTeams(paginationRequest) , HttpStatus.OK);
    }

    @PostMapping("/teams/create")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamRequest teamRequest) {
        logger.info("Create new team {}", teamRequest);
        var TeamCreated = teamService.createTeam(TeamRequestToDTOMapper.fromRequestToDTO(teamRequest));
        return new ResponseEntity<>(TeamCreated, HttpStatus.CREATED);
    }
}
