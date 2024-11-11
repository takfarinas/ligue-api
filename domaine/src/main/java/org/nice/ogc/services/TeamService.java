package org.nice.ogc.services;

import org.nice.ogc.dtos.*;
import org.nice.ogc.entities.Player;
import org.nice.ogc.exceptions.ResourceAlreadyExistsException;
import org.nice.ogc.mappers.TeamMapper;
import org.nice.ogc.repositories.PlayerRepository;
import org.nice.ogc.repositories.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * service pour gérer les équipes
 */
@Service
public class TeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(final TeamRepository teamRepository, PlayerRepository playerRepository,
                       final TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * retourne la liste des équipe
     * @param paginationRequestDTO
     * @return une page d'équipe
     */
    public PaginatedResponse<TeamDTO> getTeams(PaginationRequestDTO paginationRequestDTO) {
        var pageable = PageRequest.of(
                paginationRequestDTO.getPage(),
                paginationRequestDTO.getSize(),
                Sort.by(paginationRequestDTO.getDirection(), paginationRequestDTO.getSortBy()));
        var pageResult =  this.teamRepository.findAllBy(pageable);
        return PaginationMapper.map(pageResult, TeamMapper::fromViewToDTO);
    }

    /**
     * Creation d'une équipe
     * @param teamDTO l'équipe a créer
     * @return L'équipe créer
     */
    public TeamDTO createTeam(TeamDTO teamDTO) {

        // vérifier si l'équipe existe déja (Acronym unique)
        if(this.teamRepository.findByAcronym(teamDTO.getAcronym()).isPresent()){
            throw new ResourceAlreadyExistsException("Team with Acronym " + teamDTO.getAcronym() + " already exists.");
        }

        // si il y a des joueurs
        if (teamDTO.getPlayers() != null) {
            // si un des joueurs existe déja, on ne crée pas l'équipe.
            var playerExisted = this.playerRepository.findAllByEmailIn(teamDTO.getPlayers()
                    .stream()
                    .map(PlayerDTO::getEmail)
                    .toList());

            if(!playerExisted.isEmpty()){
                var emailsExisted = playerExisted.stream()
                        .map(Player::getEmail)
                        .collect(Collectors.joining(", ", "[",  "]"));
                throw new ResourceAlreadyExistsException("Player(s) with email(s) " + emailsExisted+ " already exists.");
            }
        }

        // insérer l'équipe dans les joueur
        var teamToCreate = TeamMapper.fromDTOToEntity(teamDTO);
        teamToCreate.getPlayers().forEach(player -> player.setTeam(teamToCreate));

        return TeamMapper.fromEntityToDTO(teamRepository.save(teamToCreate));
    }
}