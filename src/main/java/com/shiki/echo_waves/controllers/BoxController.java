package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.Box;
import com.shiki.echo_waves.services.BoxService;
import com.shiki.echo_waves.dto.BoxCreationDTO;
import com.shiki.echo_waves.dto.TirageResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/boxes")
@CrossOrigin(origins = "*")
@Tag(name = "Boxes", description = "API de gestion des boîtes")
public class BoxController {
    @Autowired
    private BoxService boxService;

    @Operation(summary = "Récupérer toutes les boîtes",
              description = "Retourne la liste de toutes les boîtes disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des boîtes trouvée"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<Box>> getAllBoxes() {
        return ResponseEntity.ok(boxService.getAllBoxes());
    }

    @Operation(summary = "Récupérer une boîte par son ID",
              description = "Retourne une boîte unique identifiée par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boîte trouvée"),
        @ApiResponse(responseCode = "404", description = "Boîte non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Box> getBoxById(@PathVariable Integer id) {
        return ResponseEntity.ok(boxService.getBoxById(id));
    }

    @Operation(summary = "Créer une nouvelle boîte",
              description = "Crée une nouvelle boîte avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boîte créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Box> createBox(@RequestBody Box box) {
        return ResponseEntity.ok(boxService.createBox(box));
    }

    @Operation(summary = "Mettre à jour une boîte existante",
              description = "Met à jour les informations d'une boîte existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boîte mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Boîte non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Box> updateBox(@PathVariable Integer id, @RequestBody Box box) {
        return ResponseEntity.ok(boxService.updateBox(id, box));
    }

    @Operation(summary = "Supprimer une boîte",
              description = "Supprime une boîte par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boîte supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Boîte non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBox(@PathVariable Integer id) {
        boxService.deleteBox(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Créer une boîte avec du contenu",
              description = "Crée une boîte avec du contenu spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boîte créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/create-with-content")
    public ResponseEntity<Box> createBoxWithContent(@RequestBody BoxCreationDTO boxDTO) {
        return ResponseEntity.ok(boxService.createBoxWithContent(boxDTO));
    }

    @Operation(summary = "Effectuer un tirage",
              description = "Effectue un tirage pour une boîte spécifiée et un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tirage effectué avec succès"),
        @ApiResponse(responseCode = "404", description = "Boîte ou utilisateur non trouvé")
    })
    @PostMapping("/{boxId}/tirage")
    public ResponseEntity<TirageResultDTO> effectuerTirage(
            @PathVariable Integer boxId,
            @RequestParam Integer userId) {
        return ResponseEntity.ok(boxService.effectuerTirage(boxId, userId));
    }
} 