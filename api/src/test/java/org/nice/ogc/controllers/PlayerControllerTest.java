package org.nice.ogc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.nice.ogc.ApiApplication;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PaginationRequestDTO;
import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.exceptions.ResourceAlreadyExistsException;
import org.nice.ogc.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

@WebMvcTest(PlayerController.class)
@ContextConfiguration(classes = ApiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test 1: Get Players by Page OK")
    @Order(1)
    public void get_playersByPage_returnsOkWithPageOfPlayers() throws Exception {

        var player1 = new PlayerDTO(1L, "player1", "player1@box.fr","DF");
        var player2 = new PlayerDTO(2L, "player2", "player2@box.fr","DF");

        var pageOfPlayers = new PaginatedResponse<PlayerDTO>(
                List.of(player1, player2),
                1,
                5,
                10,
                2,
                false);

        Mockito.when(playerService.getPlayers(Mockito.any(PaginationRequestDTO.class))).thenReturn(pageOfPlayers);

        mockMvc.perform(MockMvcRequestBuilders
               .get("/api/v1/players?page=0&size=5&direction=DESC&sortBy=name")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].name", is("player1")))
                .andExpect(jsonPath("$.content[0].email", is("player1@box.fr")))
               .andExpect(jsonPath("$.totalPages", is(2)));

    }

    @Test
    @DisplayName("Test 2: Create Player return OK with Player")
    @Order(2)
    public void cratePlayer_returnsOk_withPlayerCreated() throws Exception {

        var player = new PlayerDTO(1L, "player1", "player1@box.fr","DF");

        Mockito.when(playerService.createPlayer(Mockito.any(PlayerDTO.class)))
                .thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/players/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("player1")))
                .andExpect(jsonPath("$.email", is("player1@box.fr")));
    }

    @Test
    @DisplayName("Test 2: Get Players by Page OK")
    @Order(3)
    public void cratePlayer_returnsException_AlreadyExists() throws Exception {

        var player = new PlayerDTO(1L, "player1", "player1@box.fr","DF");

        doThrow(new ResourceAlreadyExistsException("Player already exists."))
                .when(playerService).createPlayer(Mockito.any(PlayerDTO.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/players/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Player already exists.")))
                .andExpect(jsonPath("$.details", is("uri=/api/v1/players/create")));

    }
    @Test
    @DisplayName("Test 4: create player return validation exception")
    @Order(4)
    public void cratePlayer_returnsValidationException() throws Exception {

        var player = new PlayerDTO(1L, "player1", "player1","DF");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/players/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.details", is("email: Email is not valid")));
    }
}
