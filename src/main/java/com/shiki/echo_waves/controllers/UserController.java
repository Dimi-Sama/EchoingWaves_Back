package com.shiki.echo_waves.controllers;

import com.shiki.echo_waves.models.User;
import com.shiki.echo_waves.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.HashMap;
import org.springframework.security.crypto.password.PasswordEncoder;    
import com.shiki.echo_waves.dto.LoginDTO;
import com.shiki.echo_waves.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "API de gestion des utilisateurs")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil JwtUtil;

    @Operation(summary = "Récupérer tous les utilisateurs", 
              description = "Retourne la liste de tous les utilisateurs enregistrés")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des utilisateurs trouvée",
                    content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Récupérer un utilisateur par son ID",
              description = "Retourne un utilisateur unique identifié par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Créer un nouvel utilisateur",
              description = "Crée un nouvel utilisateur avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @Operation(summary = "Mettre à jour un utilisateur",
              description = "Met à jour les informations d'un utilisateur existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Supprimer un utilisateur",
              description = "Supprime un utilisateur par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Connexion d'un utilisateur",
              description = "Connexion d'un utilisateur avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.getUserByPseudo(loginDTO.getPseudo());
            
            if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                String token = JwtUtil.generateToken(user.getPseudo(), user.getRole().toString());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("id", user.getId().toString());
                response.put("pseudo", user.getPseudo());
                response.put("role", user.getRole().toString());
                response.put("points", user.getPoints().toString());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Identifiants invalides"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Erreur serveur"));
        }
    }

    @Operation(summary = "Obtenir les points d'un utilisateur",
              description = "Retourne le nombre de points d'un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points récupérés avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getUserPoints(@PathVariable Integer id) {
        Integer points = userService.getUserPoints(id);
        return ResponseEntity.ok(Map.of("points", points));
    }

    @Operation(summary = "Ajouter des points à un utilisateur",
              description = "Ajoute un nombre spécifié de points à un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points ajoutés avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{id}/points/add")
    public ResponseEntity<User> addPoints(@PathVariable Integer id, @RequestBody Map<String, Integer> pointsMap) {
        Integer pointsToAdd = pointsMap.get("points");
        return ResponseEntity.ok(userService.addPoints(id, pointsToAdd));
    }

    @Operation(summary = "Retirer des points à un utilisateur",
              description = "Retire un nombre spécifié de points à un utilisateur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points retirés avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{id}/points/remove")
    public ResponseEntity<User> removePoints(@PathVariable Integer id, @RequestBody Map<String, Integer> pointsMap) {
        Integer pointsToRemove = pointsMap.get("points");
        return ResponseEntity.ok(userService.removePoints(id, pointsToRemove));
    }

    @Operation(summary = "Réinitialiser les points de tous les utilisateurs",
              description = "Met à 0 les points de tous les utilisateurs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points réinitialisés avec succès"),
        @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/points/reset-all")
    public ResponseEntity<Map<String, String>> resetAllUsersPoints() {
        try {
            userService.resetAllUsersPoints();
            return ResponseEntity.ok(Map.of("message", "Les points de tous les utilisateurs ont été réinitialisés à 0"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Erreur lors de la réinitialisation des points: " + e.getMessage()));
        }
    }
} 