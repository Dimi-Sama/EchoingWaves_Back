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
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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


    // @PostMapping("/hash-all-passwords")
    // public ResponseEntity<Map<String, String>> hashAllPasswords() {
    //     try {
    //         List<User> users = userService.getAllUsers();
    //         int count = 0;
            
    //         for (User user : users) {
    //             if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
    //                 String hashedPassword = passwordEncoder.encode(user.getPassword());
    //                 user.setPassword(hashedPassword);
    //                 userService.updateUser(user.getId(), user);
    //                 count++;
    //             }
    //         }
            
    //         Map<String, String> response = new HashMap<>();
    //         response.put("message", count + " mots de passe ont été hashés avec succès");
    //         return ResponseEntity.ok(response);
            
    //     } catch (Exception e) {
    //         Map<String, String> errorResponse = new HashMap<>();
    //         errorResponse.put("message", "Erreur lors du hashage des mots de passe: " + e.getMessage());
    //         return ResponseEntity.internalServerError().body(errorResponse);
    //     }
    // }

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
                response.put("pseudo", user.getPseudo());
                response.put("role", user.getRole().toString());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Identifiants invalides"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Erreur serveur"));
        }
    }
} 