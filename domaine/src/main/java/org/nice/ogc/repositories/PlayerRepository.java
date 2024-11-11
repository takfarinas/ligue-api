package org.nice.ogc.repositories;

import org.nice.ogc.entities.Player;
import org.nice.ogc.entities.projections.PlayerView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Page<PlayerView> findAllBy(Pageable pageable);

    Optional<Player> findByEmail(String email);

    List<Player> findAllByEmailIn(List<String> list);
}
