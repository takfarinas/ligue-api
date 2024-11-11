package org.nice.ogc.services;

import org.nice.ogc.dtos.PaginatedResponse;
import org.nice.ogc.dtos.PaginationMapper;
import org.nice.ogc.dtos.PaginationRequestDTO;
import org.nice.ogc.dtos.PlayerDTO;
import org.nice.ogc.exceptions.ResourceAlreadyExistsException;
import org.nice.ogc.exceptions.ResourceNotFoundException;
import org.nice.ogc.mappers.PlayerMapper;
import org.nice.ogc.repositories.PlayerRepository;
import org.nice.ogc.repositories.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les jouer
 */
@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(final PlayerRepository playerRepository, TeamRepository teamRepository,
                         final PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * retourne la liste des jouer avec pagination
     * @param paginationRequestDTO PAralètre de pagination
     * @return Une page de jours
     */
    public PaginatedResponse<PlayerDTO> getPlayers(PaginationRequestDTO paginationRequestDTO) {
        var pageable = PageRequest.of(
                paginationRequestDTO.getPage(),
                paginationRequestDTO.getSize(),
                Sort.by(paginationRequestDTO.getDirection(), paginationRequestDTO.getSortBy()));
        var pageResult =  this.playerRepository.findAllBy(pageable);

        logger.info("Result found, page number {}, elements in page {}",
                pageResult.getNumber(), pageResult.getSize());

        return PaginationMapper.map(pageResult, PlayerMapper::fromViewToDTO);
    }

    /**
     * Creation d'un jouer
     * @param playerDTO Les données du jouer a créer.
     * @return Le jour créer
     */
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {

        if(playerRepository.findByEmail(playerDTO.getEmail()).isPresent()){
            throw new ResourceAlreadyExistsException("Player with Email " + playerDTO.getEmail() + " already exists.");
        }

        var palyerCreated = playerRepository.save(PlayerMapper.fromDTOToEntity(playerDTO));

        logger.info("player created with ID: {} ", palyerCreated.getId());

        return PlayerMapper.fromEntityToDTO(palyerCreated);
    }

    /**
     * traznsfert d'un joueur dans une autre équipe
     * @param playerId le joueur à tarnferer
     * @param teamId l'équipe où tranferer
     * @return le jouer transferer
     */
    public PlayerDTO transfertPlayer(Long playerId, Long teamId) {

        // vérifier si le joueur existe
        var playerToTransfert = this.playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player with id " + playerId + " not found."));

        // vérifier si le l'équipe existe
        var transfertTeam = this.teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + teamId + " not found."));

        // changer l'équipe du joueur
        playerToTransfert.setTeam(transfertTeam);

        logger.info("player with ID {} is transferted to team with ID: {} ",
                playerToTransfert.getId(), transfertTeam.getId());

        return PlayerMapper.fromEntityToDTO(playerRepository.save(playerToTransfert));
    }
}
