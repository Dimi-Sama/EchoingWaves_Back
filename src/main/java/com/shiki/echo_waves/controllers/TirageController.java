package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.Tirage;
import com.shiki.echo_waves.services.TirageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tirages")
@CrossOrigin(origins = "*")
@Tag(name = "Tirages", description = "API de gestion des tirages")
public class TirageController {
    @Autowired
    private TirageService tirageService;

    @Operation(summary = "Récupérer tous les tirages",
              description = "Retourne la liste de tous les tirages disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des tirages trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<Tirage>> getAllTirages() {
        return ResponseEntity.ok(tirageService.getAllTirages());
    }

    @Operation(summary = "Récupérer un tirage par son ID",
              description = "Retourne un tirage unique identifié par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tirage trouvé"),
        @ApiResponse(responseCode = "404", description = "Tirage non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tirage> getTirageById(@PathVariable Integer id) {
        return ResponseEntity.ok(tirageService.getTirageById(id));
    }
    
    @Operation(summary = "Récupérer les tirages par l'utilisateur",
              description = "Retourne la liste des tirages associés à un utilisateur spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des tirages trouvée"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tirage>> getTiragesByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(tirageService.getTiragesByUser(userId));
    }

    @Operation(summary = "Créer un nouveau tirage",
              description = "Crée un nouveau tirage avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tirage créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Tirage> createTirage(@RequestBody Tirage tirage) {
        return ResponseEntity.ok(tirageService.createTirage(tirage));
    }

    @Operation(summary = "Supprimer un tirage",
              description = "Supprime un tirage par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tirage supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Tirage non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTirage(@PathVariable Integer id) {
        tirageService.deleteTirage(id);
        return ResponseEntity.ok().build();
    }
} 