package org.nice.ogc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.nice.ogc.ApiApplication;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PaginationRequestDTO;
import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.dtos.TeamDTO;
import org.nice.ogc.exceptions.ResourceAlreadyExistsException;
import org.nice.ogc.services.PlayerService;
import org.nice.ogc.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@ContextConfiguration(classes = ApiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test 1: Get Teams by Page OK")
    @Order(1)
    public void get_TeamsByPage_returnsOkWithPageOfTeams() throws Exception {

        var pageOfTeams = getTeamDTOPaginatedResponse();

        Mockito.when(teamService.getTeams(Mockito.any(PaginationRequestDTO.class))).thenReturn(pageOfTeams);

        mockMvc.perform(MockMvcRequestBuilders
               .get("/api/v1/teams?page=0&size=5&direction=DESC&sortBy=name")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].name", is("team1")))
               .andExpect(jsonPath("$.content[0].acronym", is("t1")))
               .andExpect(jsonPath("$.content[0].players",  hasSize(2)))
               .andExpect(jsonPath("$.content[0].players[0].name",  is("player1")))
               .andExpect(jsonPath("$.content[1].players",  hasSize(2)))
               .andExpect(jsonPath("$.content[0].players[1].name",  is("player2")))
               .andExpect(jsonPath("$.totalPages", is(2)));

    }

    private PaginatedResponse<TeamDTO> getTeamDTOPaginatedResponse() {
        var player1 = new PlayerDTO(1L, "player1", "player1@box.fr","DF");
        var player2 = new PlayerDTO(2L, "player2", "player2@box.fr","DF");

        var player3 = new PlayerDTO(3L, "player3", "player3@box.fr","DF");
        var player4 = new PlayerDTO(4L, "player4", "player4@box.fr","DF");

        var team1 = new TeamDTO(1L, "team1", "t1", "150", List.of(player1, player2));
        var team2 = new TeamDTO(2L, "team2", "t2", "200", List.of(player3, player4));

        var pageOfTeams = new PaginatedResponse<TeamDTO>(
                List.of(team1, team2),
                1,
                5,
                10,
                2,
                false);
        return pageOfTeams;
    }

    @Test
    @DisplayName("Test 2: Create Team return OK with Team")
    @Order(2)
    public void crateTeam_returnsOk_withTeamCreated() throws Exception {

        var player1 = new PlayerDTO(1L, "player1", "player1@box.fr","DF");
        var player2 = new PlayerDTO(2L, "player2", "player2@box.fr","DF");

        var team = new TeamDTO(1L, "team", "tm", "200", List.of(player1, player2));

        Mockito.when(teamService.createTeam(Mockito.any(TeamDTO.class)))
                .thenReturn(team);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("team")))
                .andExpect(jsonPath("$.acronym", is("tm")))
                .andExpect(jsonPath("$.players", hasSize(2)))
                .andExpect(jsonPath("$.players[0].name", is("player1")));
    }

    @Test
    @DisplayName("Test 3: create team throw exception already exists")
    @Order(3)
    public void crateTeam_returnsException_AlreadyExists() throws Exception {

        var player1 = new PlayerDTO(1L, "player1", "player1@box.fr","DF");
        var player2 = new PlayerDTO(2L, "player2", "player2@box.fr","DF");

        var team = new TeamDTO(1L, "team", "tm", "200", List.of(player1, player2));

        doThrow(new ResourceAlreadyExistsException("Team already exists."))
                .when(teamService).createTeam(Mockito.any(TeamDTO.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Team already exists.")))
                .andExpect(jsonPath("$.details", is("uri=/api/v1/teams/create")));

    }

    @Test
    @DisplayName("Test 4: create team throw validation exception")
    @Order(4)
    public void crateTeam_returnsValidationException() throws Exception {

        var player1 = new PlayerDTO(1L, "player1", "player1@box.fr","DF");
        var player2 = new PlayerDTO(2L, "player2", "player2@box.fr","DF");

        var team = new TeamDTO(1L, "team", "t", "200", List.of(player1, player2));

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/teams/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(team)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Validation failed")))
            .andExpect(jsonPath("$.details", is("acronym: Acronym should have at least 2 characters")));

    }
}
