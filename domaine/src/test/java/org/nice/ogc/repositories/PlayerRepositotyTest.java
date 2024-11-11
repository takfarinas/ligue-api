package org.nice.ogc.repositories;

import org.junit.jupiter.api.*;
import org.nice.ogc.entities.Player;
import org.nice.ogc.entities.projections.PlayerView;
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
public class PlayerRepositotyTest {

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeAll
    public void initDataTest() {
        Player employee1 = Player.builder()
                .name("Sam")
                .email("sam@gmail.com")
                .position("400")
                .build();

        Player employee2 = Player.builder()
                .name("Joe")
                .email("joe@gmail.com")
                .position("400")
                .build();

        Player employee3 = Player.builder()
                .name("Jane")
                .email("Jane@gmail.com")
                .position("400")
                .build();

        playerRepository.saveAll(List.of(employee1, employee2, employee3));
    }

    @Test
    @DisplayName("Test 1:Save Player Test")
    @Order(1)
    @Rollback(value = true)
    public void savePlayerTest(){

        Player player = Player.builder()
                .name("bob")
                .email("bob@gmail.com")
                .position("400")
                .build();

        playerRepository.save(player);
        Assertions.assertNotNull(player.getId());
    }

    @Test
    @DisplayName("Test 2:Get Player Page")
    @Order(2)
    @Rollback(value = true)
    public void getPlayerPageTest(){

        Page<PlayerView> employeePage = playerRepository.findAllBy(PageRequest
                .of(0,5, Sort.by(Sort.Direction.ASC, "name")));

        Assertions.assertInstanceOf(PlayerView.class, employeePage.getContent().get(0));
        Assertions.assertEquals(3, employeePage.getContent().size());
        Assertions.assertEquals("Sam", employeePage.getContent().get(2).getName());
    }

    @Test
    @DisplayName("Test 3: find All By Email In")
    @Order(3)
    @Rollback(value = true)
    public void findAllByEmailIn() {

        var players = playerRepository.findAllByEmailIn(List.of("joe@gmail.com", "sam@gmail.com" ));
        Assertions.assertEquals(2, players.size());
    }
}
