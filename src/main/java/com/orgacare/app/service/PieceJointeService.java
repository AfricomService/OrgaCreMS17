package com.orgacare.app.service;

import com.orgacare.app.config.ApplicationProperties;
import com.orgacare.app.domain.PieceJointe;
import com.orgacare.app.repository.FormationRepository;
import com.orgacare.app.repository.PieceJointeRepository;
import com.orgacare.app.repository.SessionFormationRepository;
import com.orgacare.app.service.dto.PieceJointeDTO;
import com.orgacare.app.service.mapper.PieceJointeMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeService.class);

    /** Extensions acceptées (liste blanche : évite de servir du .html/.js/.jsp depuis notre origine). */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        "pdf",
        "jpg",
        "jpeg",
        "png",
        "gif",
        "bmp",
        "webp",
        "tif",
        "tiff",
        "doc",
        "docx",
        "xls",
        "xlsx",
        "csv",
        "ppt",
        "pptx",
        "txt",
        "odt",
        "ods"
    );

    private static final int MAX_NAME_LENGTH = 200;

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    private final SessionFormationRepository sessionFormationRepository;

    private final FormationRepository formationRepository;

    private final ApplicationProperties applicationProperties;

    public PieceJointeService(
        PieceJointeRepository pieceJointeRepository,
        PieceJointeMapper pieceJointeMapper,
        SessionFormationRepository sessionFormationRepository,
        FormationRepository formationRepository,
        ApplicationProperties applicationProperties
    ) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
        this.sessionFormationRepository = sessionFormationRepository;
        this.formationRepository = formationRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a pieceJointe.
     */
    public PieceJointeDTO save(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to save PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Partially update a pieceJointe.
     */
    public Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointeDTO);

        return pieceJointeRepository
            .findById(pieceJointeDTO.getId())
            .map(existingPieceJointe -> {
                pieceJointeMapper.partialUpdate(existingPieceJointe, pieceJointeDTO);

                return existingPieceJointe;
            })
            .map(pieceJointeRepository::save)
            .map(pieceJointeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findAll() {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll().stream().map(pieceJointeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<PieceJointeDTO> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findById(id).map(pieceJointeMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }

    // ════════════════════════════════════════════════════════════
    //  Pièces jointes d'une SessionFormation
    // ════════════════════════════════════════════════════════════

    /**
     * Upload physique d'un fichier lié à une SessionFormation.
     * Le nom physique est généré côté serveur (UUID) : le client ne contrôle jamais le chemin.
     *
     * @throws IllegalArgumentException si le fichier ou la session est invalide (→ 400 dans le resource).
     */
    public PieceJointeDTO uploadForSession(MultipartFile file, Long sessionId) throws IOException {
        if (sessionId == null || !sessionFormationRepository.existsById(sessionId)) {
            throw new IllegalArgumentException("Session de formation introuvable");
        }
        PieceJointeDTO dto = storeFile(file);
        dto.setSessionId(sessionId);
        return save(dto);
    }

    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findBySessionId(Long sessionId) {
        log.debug("Request to get PieceJointes by SessionFormation : {}", sessionId);
        return pieceJointeRepository.findAllBySessionId(sessionId).stream().map(pieceJointeMapper::toDto).collect(Collectors.toList());
    }

    // ════════════════════════════════════════════════════════════
    //  Pièces jointes d'une Formation
    // ════════════════════════════════════════════════════════════

    public PieceJointeDTO uploadForFormation(MultipartFile file, Long formationId) throws IOException {
        if (formationId == null || !formationRepository.existsById(formationId)) {
            throw new IllegalArgumentException("Formation introuvable");
        }
        PieceJointeDTO dto = storeFile(file);
        dto.setFormationId(formationId);
        return save(dto);
    }

    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findByFormationId(Long formationId) {
        log.debug("Request to get PieceJointes by Formation : {}", formationId);
        return pieceJointeRepository.findAllByFormationId(formationId).stream().map(pieceJointeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Valide et enregistre physiquement le fichier. Retourne un DTO non sauvegardé en base.
     * Le nom physique est généré côté serveur (UUID) : le client ne contrôle jamais le chemin.
     */
    private PieceJointeDTO storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide");
        }

        String originalFilename = StringUtils.getFilename(
            StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), ""))
        );
        int dot = originalFilename == null ? -1 : originalFilename.lastIndexOf('.');
        if (dot <= 0 || dot == originalFilename.length() - 1) {
            throw new IllegalArgumentException("Nom de fichier invalide (extension manquante)");
        }

        String extension = originalFilename.substring(dot + 1).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Type de fichier non autorisé : ." + extension);
        }

        String baseName = originalFilename.substring(0, dot);
        if (baseName.length() > MAX_NAME_LENGTH) {
            baseName = baseName.substring(0, MAX_NAME_LENGTH);
        }

        ZonedDateTime now = ZonedDateTime.now();
        Path finalDir = baseDir()
            .resolve(String.valueOf(now.getYear()))
            .resolve(now.getMonth().toString())
            .resolve(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Files.createDirectories(finalDir);

        Path filePath = finalDir.resolve(UUID.randomUUID() + "." + extension);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath);
        }

        PieceJointeDTO dto = new PieceJointeDTO();
        dto.setNomFichier(baseName);
        dto.setType(extension);
        dto.setFichierURL(filePath.toString());
        dto.setDateUpload(ZonedDateTime.now());
        return dto;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getFile(Long pieceJointeId) {
        Optional<PieceJointeDTO> optional = findOne(pieceJointeId);
        if (optional.isEmpty() || optional.get().getFichierURL() == null) {
            return ResponseEntity.notFound().build();
        }
        PieceJointeDTO dto = optional.get();

        try {
            Path path = Paths.get(dto.getFichierURL()).toAbsolutePath().normalize();
            // Garde-fou : on ne sert que des fichiers situés sous le répertoire des pièces jointes
            if (!path.startsWith(baseDir())) {
                log.warn("PieceJointe {} pointe hors du répertoire autorisé : {}", pieceJointeId, path);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String downloadName = dto.getNomFichier() + (dto.getType() != null ? "." + dto.getType() : "");
            MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.inline().filename(downloadName, StandardCharsets.UTF_8).build().toString()
                )
                .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Renomme une pièce jointe (champ nomFichier uniquement : extension et fichier physique inchangés).
     */
    public PieceJointeDTO renamePieceJointe(Long id, String newName) {
        log.debug("Request to rename PieceJointe {} -> {}", id, newName);

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }

        PieceJointe pieceJointe = pieceJointeRepository
            .findById(id)
            .orElseThrow(() -> new javax.persistence.EntityNotFoundException("PieceJointe not found with id " + id));

        pieceJointe.setNomFichier(newName.trim());

        return pieceJointeMapper.toDto(pieceJointeRepository.save(pieceJointe));
    }

    private Path baseDir() {
        String dir = applicationProperties.getDirPieceJointe();
        if (!StringUtils.hasText(dir)) {
            throw new IllegalStateException("application.dir-piece-jointe n'est pas configuré");
        }
        return Paths.get(dir).toAbsolutePath().normalize();
    }
}
