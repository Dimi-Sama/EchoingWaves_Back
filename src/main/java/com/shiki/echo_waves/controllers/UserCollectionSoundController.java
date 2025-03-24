package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.UserCollectionSound;
import com.shiki.echo_waves.services.UserCollectionSoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/collection-sounds")
@CrossOrigin(origins = "*")
@Tag(name = "UserCollectionSounds", description = "API de gestion des sons de collection des utilisateurs")
public class UserCollectionSoundController {
    @Autowired
    private UserCollectionSoundService userCollectionSoundService;

    @Operation(summary = "Récupérer tous les sons de collection",
              description = "Retourne la liste de tous les sons de collection disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des sons de collection trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<UserCollectionSound>> getAllCollectionSounds() {
        return ResponseEntity.ok(userCollectionSoundService.getAllCollectionSounds());
    }

    @Operation(summary = "Récupérer les sons de collection par l'ID de la collection",
              description = "Retourne la liste des sons de collection associés à un ID de collection spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des sons de collection trouvée"),
        @ApiResponse(responseCode = "404", description = "Collection non trouvée")
    })
    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<List<UserCollectionSound>> getCollectionSoundsByCollectionId(
            @PathVariable Integer collectionId) {
        return ResponseEntity.ok(userCollectionSoundService.getCollectionSoundsByCollectionId(collectionId));
    }

    @Operation(summary = "Ajouter un son à la collection",
              description = "Ajoute un son à la collection avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son ajouté à la collection avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<UserCollectionSound> addSoundToCollection(
            @RequestBody UserCollectionSound collectionSound) {
        return ResponseEntity.ok(userCollectionSoundService.addSoundToCollection(collectionSound));
    }

    @Operation(summary = "Supprimer un son de la collection",
              description = "Supprime un son de la collection par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Son supprimé de la collection avec succès"),
        @ApiResponse(responseCode = "404", description = "Son non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCollection(@PathVariable Integer id) {
        userCollectionSoundService.removeFromCollection(id);
        return ResponseEntity.ok().build();
    }
} 