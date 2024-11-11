package org.nice.ogc.entities.projections;

import java.util.List;

public interface TeamView {

        Long getId();
        String getName();
        String getAcronym();
        String getBudjet();

        List<PlayerView> getPlayers();



}
