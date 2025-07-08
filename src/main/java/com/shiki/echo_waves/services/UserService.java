package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.User;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.shiki.echo_waves.models.UsersCollection;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    
    @Transactional
    public User createUser(User user) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        // Vérifier si le pseudo existe déjà
        if (userRepository.existsByPseudo(user.getPseudo())) {
            throw new RuntimeException("Pseudo déjà utilisé");
        }
        
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Initialiser les points à 0
        user.setPoints(0);
        
        // Sauvegarder l'utilisateur d'abord
        User savedUser = userRepository.save(user);
        
        // Créer une collection pour l'utilisateur
        UsersCollection collection = new UsersCollection();
        collection.setUser(savedUser);
        collection = usersCollectionRepository.save(collection);
        
        // Associer la collection à l'utilisateur
        savedUser.setUsersCollection(collection);
        
        // Sauvegarder à nouveau l'utilisateur
        return userRepository.save(savedUser);
    }
    
    public User updateUser(Integer id, User userDetails) {
        User user = getUserById(id);
        user.setPseudo(userDetails.getPseudo());
        user.setEmail(userDetails.getEmail());
        user.setSecret_bool(userDetails.getSecret_bool());
        user.setRole(userDetails.getRole());
        if (userDetails.getPoints() != null) {
            user.setPoints(userDetails.getPoints());
        }
        return userRepository.save(user);
    }
    
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User getUserByPseudo(String pseudo) {
        return userRepository.findByPseudo(pseudo)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public User addPoints(Integer userId, Integer pointsToAdd) {
        User user = getUserById(userId);
        user.setPoints(user.getPoints() + pointsToAdd);
        return userRepository.save(user);
    }

    @Transactional
    public User removePoints(Integer userId, Integer pointsToRemove) {
        User user = getUserById(userId);
        int newPoints = Math.max(0, user.getPoints() - pointsToRemove);
        user.setPoints(newPoints);
        return userRepository.save(user);
    }

    public Integer getUserPoints(Integer userId) {
        User user = getUserById(userId);
        return user.getPoints();
    }

    @Transactional
    public void resetAllUsersPoints() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPoints(0);
        }
        userRepository.saveAll(users);
    }
} 