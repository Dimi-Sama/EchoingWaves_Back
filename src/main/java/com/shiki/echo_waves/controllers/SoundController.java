package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.Sound;
import com.shiki.echo_waves.services.SoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sounds")
@CrossOrigin(origins = "*")
@Tag(name = "Sounds", description = "API de gestion des sons")
public class SoundController {
    @Autowired
    private SoundService soundService;

    @Operation(summary = "Récupérer tous les sons",
              description = "Retourne la liste de tous les sons disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des sons trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<Sound>> getAllSounds() {
        return ResponseEntity.ok(soundService.getAllSounds());
    }

    @Operation(summary = "Récupérer un son par son ID",
              description = "Retourne un son unique identifié par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son trouvé"),
        @ApiResponse(responseCode = "404", description = "Son non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sound> getSoundById(@PathVariable Integer id) {
        return ResponseEntity.ok(soundService.getSoundById(id));
    }

    @Operation(summary = "Créer un nouveau son",
              description = "Crée un nouveau son avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Sound> createSound(@RequestBody Sound sound) {
        return ResponseEntity.ok(soundService.createSound(sound));
    }

    @Operation(summary = "Mettre à jour un son existant",
              description = "Met à jour les informations d'un son existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Son non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sound> updateSound(@PathVariable Integer id, @RequestBody Sound sound) {
        return ResponseEntity.ok(soundService.updateSound(id, sound));
    }

    @Operation(summary = "Supprimer un son",
              description = "Supprime un son par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Son non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSound(@PathVariable Integer id) {
        soundService.deleteSound(id);
        return ResponseEntity.ok().build();
    }
} 