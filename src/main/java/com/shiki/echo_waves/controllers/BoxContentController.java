package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.BoxContent;
import com.shiki.echo_waves.services.BoxContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/box-contents")
@CrossOrigin(origins = "*")
@Tag(name = "BoxContents", description = "API de gestion des contenus de boîtes")
public class BoxContentController {
    @Autowired
    private BoxContentService boxContentService;

    @Operation(summary = "Récupérer tous les contenus de boîtes",
              description = "Retourne la liste de tous les contenus de boîtes disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des contenus de boîtes trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<BoxContent>> getAllBoxContents() {
        return ResponseEntity.ok(boxContentService.getAllBoxContents());
    }
    @Operation(summary = "Récupérer un contenu de boîte par son ID",
              description = "Retourne un contenu de boîte unique identifié par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenu de boîte trouvé"),
        @ApiResponse(responseCode = "404", description = "Contenu de boîte non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoxContent> getBoxContentById(@PathVariable Integer id) {
        return ResponseEntity.ok(boxContentService.getBoxContentById(id));
    }

    @Operation(summary = "Créer un nouveau contenu de boîte",
              description = "Crée un nouveau contenu de boîte avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenu de boîte créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<BoxContent> createBoxContent(@RequestBody BoxContent boxContent) {
        return ResponseEntity.ok(boxContentService.createBoxContent(boxContent));
    }

    @Operation(summary = "Mettre à jour un contenu de boîte existant",
              description = "Met à jour les informations d'un contenu de boîte existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenu de boîte mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Contenu de boîte non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BoxContent> updateBoxContent(@PathVariable Integer id, @RequestBody BoxContent boxContent) {
        return ResponseEntity.ok(boxContentService.updateBoxContent(id, boxContent));
    }

    @Operation(summary = "Supprimer un contenu de boîte",
              description = "Supprime un contenu de boîte par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenu de boîte supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Contenu de boîte non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoxContent(@PathVariable Integer id) {
        boxContentService.deleteBoxContent(id);
        return ResponseEntity.ok().build();
    }
} 