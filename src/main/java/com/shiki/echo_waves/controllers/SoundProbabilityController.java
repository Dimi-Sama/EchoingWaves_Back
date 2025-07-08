package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.SoundProbability;
import com.shiki.echo_waves.services.SoundProbabilityService;
import com.shiki.echo_waves.dto.SoundProbabilityResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/probabilities")
@CrossOrigin(origins = "*")
@Tag(name = "SoundProbabilities", description = "API de gestion des probabilités des sons")
public class SoundProbabilityController {
    @Autowired
    private SoundProbabilityService soundProbabilityService;

    @Operation(summary = "Récupérer toutes les probabilités avec noms des chansons",
              description = "Retourne la liste de toutes les probabilités disponibles avec les noms des chansons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des probabilités trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<SoundProbabilityResponseDTO>> getAllProbabilities() {
        return ResponseEntity.ok(soundProbabilityService.getAllProbabilitiesWithSoundNames());
    }

    @Operation(summary = "Récupérer une probabilité par son ID avec nom de la chanson",
              description = "Retourne une probabilité unique identifiée par son ID avec le nom de la chanson")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Probabilité trouvée"),
        @ApiResponse(responseCode = "404", description = "Probabilité non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SoundProbabilityResponseDTO> getProbabilityById(@PathVariable Integer id) {
        return ResponseEntity.ok(soundProbabilityService.getProbabilityByIdWithSoundName(id));
    }

    @Operation(summary = "Récupérer les probabilités par le contenu de la boîte avec noms des chansons",
              description = "Retourne les probabilités associées à un contenu de boîte spécifié avec les noms des chansons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des probabilités trouvée"),
        @ApiResponse(responseCode = "404", description = "Contenu de boîte non trouvé")
    })
    @GetMapping("/box-content/{boxContentId}")
    public ResponseEntity<List<SoundProbabilityResponseDTO>> getProbabilitiesByBoxContent(
            @PathVariable Integer boxContentId) {
        return ResponseEntity.ok(soundProbabilityService.getProbabilitiesByBoxContentWithSoundNames(boxContentId));
    }

    @Operation(summary = "Créer une nouvelle probabilité",
              description = "Crée une nouvelle probabilité avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Probabilité créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<SoundProbability> createProbability(@RequestBody SoundProbability probability) {
        return ResponseEntity.ok(soundProbabilityService.createProbability(probability));
    }

    @Operation(summary = "Mettre à jour une probabilité existante",
              description = "Met à jour les informations d'une probabilité existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Probabilité mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Probabilité non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SoundProbability> updateProbability(
            @PathVariable Integer id, 
            @RequestBody SoundProbability probability) {
        return ResponseEntity.ok(soundProbabilityService.updateProbability(id, probability));
    }

    @Operation(summary = "Supprimer une probabilité",
              description = "Supprime une probabilité par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Probabilité supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Probabilité non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProbability(@PathVariable Integer id) {
        soundProbabilityService.deleteProbability(id);
        return ResponseEntity.ok().build();
    }
} 