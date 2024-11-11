package org.nice.ogc.repositories;

import org.junit.jupiter.api.*;
import org.nice.ogc.entities.Player;
import org.nice.ogc.entities.Team;
import org.nice.ogc.entities.projections.TeamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @BeforeAll
    public void initDataTest() {
        Team team1 = Team.builder()
                .name("Team 1")
                .acronym("t1")
                .budjet("400")
                .build();

        Team team2 = Team.builder()
                .name("Team 2")
                .acronym("t2")
                .budjet("400")
                .build();

        Player employee1 = Player.builder()
                .name("Sam")
                .email("sam@gmail.com")
                .position("df")
                .team(team1)
                .build();

        Player employee2 = Player.builder()
                .name("Joe")
                .email("joe@gmail.com")
                .position("ac")
                .team(team1)
                .build();

        Player employee3 = Player.builder()
                .name("Jane")
                .email("Jane@gmail.com")
                .position("ac")
                .team(team2)
                .build();

        team1.setPlayers(List.of(employee1, employee2));
        team2.setPlayers(List.of(employee3));

        teamRepository.saveAll(List.of(team1, team2));
    }

    @Test
    @DisplayName("Test 1:Save Team Test")
    @Order(1)
    @Rollback(value = true)
    public void savePlayerTest(){

        Team team = Team.builder()
                .name("Team a")
                .acronym("ta")
                .budjet("400")
                .build();

        Player player = Player.builder()
                .name("jane")
                .email("jane@gmail.com")
                .position("400")
                .team(team)
                .build();

        team.setPlayers(List.of(player));

        teamRepository.save(team);
        Assertions.assertNotNull(player.getId());
        Assertions.assertNotNull(player.getId());
    }

    @Test
    @DisplayName("Test 2:Get Team Page")
    @Order(2)
    @Rollback(value = true)
    public void getPlayerPageTest(){

        Page<TeamView> teamPage = teamRepository.findAllBy(PageRequest
                .of(0,5, Sort.by(Sort.Direction.ASC, "name")));

        Assertions.assertEquals(2, teamPage.getContent().size());
        Assertions.assertEquals("Team 2", teamPage.getContent().get(1).getName());
    }

    @Test
    @DisplayName("Test 2:Find By Acronym")
    @Order(3)
    @Rollback(value = true)
    public void fintByAcronym(){

        var teamPage = teamRepository.findByAcronym("t2");

        Assertions.assertTrue(teamPage.isPresent());
        Assertions.assertEquals("Team 2", teamPage.get().getName());
    }
}
