package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.UsersCollection;
import com.shiki.echo_waves.services.UsersCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/collections")
@CrossOrigin(origins = "*")
@Tag(name = "UsersCollections", description = "API de gestion des collections des utilisateurs")
public class UsersCollectionController {
    @Autowired
    private UsersCollectionService usersCollectionService;

    @Operation(summary = "Récupérer toutes les collections",
              description = "Retourne la liste de toutes les collections disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des collections trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<UsersCollection>> getAllCollections() {
        return ResponseEntity.ok(usersCollectionService.getAllCollections());
    }

    @Operation(summary = "Récupérer une collection par son ID",
              description = "Retourne une collection unique identifiée par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection trouvée"),
        @ApiResponse(responseCode = "404", description = "Collection non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersCollection> getCollectionById(@PathVariable Integer id) {
        return ResponseEntity.ok(usersCollectionService.getCollectionById(id));
    }

    @Operation(summary = "Récupérer les collections par l'utilisateur",
              description = "Retourne la liste des collections associées à un utilisateur spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des collections trouvée"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<UsersCollection> getCollectionByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersCollectionService.getCollectionByUserId(userId));
    }

    @Operation(summary = "Créer une nouvelle collection",
              description = "Crée une nouvelle collection avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<UsersCollection> createCollection(@RequestBody UsersCollection collection) {
        return ResponseEntity.ok(usersCollectionService.createCollection(collection));
    }

    @Operation(summary = "Supprimer une collection",
              description = "Supprime une collection par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Collection non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollection(@PathVariable Integer id) {
        usersCollectionService.deleteCollection(id);
        return ResponseEntity.ok().build();
    }
} 