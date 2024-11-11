package org.nice.ogc.repositories;

import org.nice.ogc.entities.Team;
import org.nice.ogc.entities.projections.TeamView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<TeamView> findAllBy(Pageable pageable);

    Optional<Team> findByAcronym(String acronym);
}
