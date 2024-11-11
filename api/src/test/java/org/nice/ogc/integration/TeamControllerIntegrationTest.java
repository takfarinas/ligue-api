package org.nice.ogc.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nice.ogc.requestmodel.PlayerRequest;
import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.dtos.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = org.nice.ogc.ApiApplication.class)

public class TeamControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Disabled
    @DisplayName("Test 1: Get Teams By Page")
    @Order(1)
    public void getTeamsByPage() {
        ResponseEntity<PaginatedResponse<TeamDTO>> response = restTemplate
                .exchange("/api/v1/teams?page=0&size=5&direction=DESC&sortBy=name",
                        HttpMethod.GET,null,
                        new ParameterizedTypeReference<PaginatedResponse<TeamDTO>>(){});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("team2", response.getBody().getContent().get(0).getName());
        assertEquals(2, response.getBody().getContent().get(0).getPlayers().size());

    }

    @Test
    @Disabled
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
}
