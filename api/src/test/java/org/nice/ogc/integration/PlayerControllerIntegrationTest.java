package org.nice.ogc.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nice.ogc.repositories.PlayerRepository;
import org.nice.ogc.requestmodel.PlayerRequest;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = org.nice.ogc.ApiApplication.class)
public class PlayerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    PlayerRepository playerRepository;

    @Test
    @DisplayName("Test 1: Get Players Page")
    @Order(1)
    public void getPlayersPage() {
        ResponseEntity<PaginatedResponse<PlayerDTO>> response = restTemplate
                .exchange("/api/v1/players?page=0&size=5&direction=DESC&sortBy=name",
                        HttpMethod.GET,null,
                        new ParameterizedTypeReference<PaginatedResponse<PlayerDTO>>(){});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getContent().size());
        assertEquals("player2b", response.getBody().getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test 2: create player")
    @Order(2)
    @Rollback
    public void createPlayers() {

        var player = PlayerRequest.builder()
                .name("player3")
                .email("player3@boite.fr")
                .position("DF")
                .build();

        ResponseEntity<PlayerDTO> response = restTemplate.postForEntity(
                "/api/v1/players/create",
                new HttpEntity<>(player),
                PlayerDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PlayerDTO createdPlayer = response.getBody();
        assertNotNull(createdPlayer);
        assertNotNull(createdPlayer.getId());
        assertEquals("player3", createdPlayer.getName());
    }

    @Test
    @DisplayName("Test 3: transfert player")
    @Order(3)
    public void transfertPlayer() {

        ResponseEntity<PlayerDTO> response = restTemplate
                .getForEntity("/api/v1/players/1/teams/2", PlayerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
