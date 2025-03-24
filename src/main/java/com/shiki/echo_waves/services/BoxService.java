package com.shiki.echo_waves.services;

import com.shiki.echo_waves.dto.BoxCreationDTO;
import com.shiki.echo_waves.dto.TirageResultDTO;
import com.shiki.echo_waves.dto.SoundProbabilityDTO;
import com.shiki.echo_waves.models.*;
import com.shiki.echo_waves.repositories.BoxRepository;
import com.shiki.echo_waves.repositories.BoxContentRepository;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.repositories.SoundRepository;
import com.shiki.echo_waves.repositories.TirageRepository;
import com.shiki.echo_waves.repositories.UserRepository;
import com.shiki.echo_waves.repositories.UsersCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;
import java.util.List;
import java.util.Date;

@Service
public class BoxService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersCollectionRepository usersCollectionRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private BoxContentRepository boxContentRepository;
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    @Autowired
    private SoundRepository soundRepository;
    @Autowired
    private UserCollectionSoundService userCollectionSoundService;
    @Autowired
    private TirageRepository tirageRepository;

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }
    
    public Box getBoxById(Integer id) {
        return boxRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Box non trouvée"));
    }
    
    @Transactional
    public Box createBoxWithContent(BoxCreationDTO boxDTO) {
        // Création de la box
        Box box = new Box();
        box.setNom(boxDTO.getNom());
        box.setHidden(boxDTO.getHidden());
        
        // Création du contenu
        BoxContent boxContent = new BoxContent();
        boxContent = boxContentRepository.save(boxContent);
        
        // Association box et content
        box.setBoxContent(boxContent);
        box = boxRepository.save(box);
        
        // Ajout des probabilités
        for (SoundProbabilityDTO probDTO : boxDTO.getSoundProbabilities()) {
            SoundProbability prob = new SoundProbability();
            prob.setBoxContent(boxContent);
            prob.setSound(soundRepository.findById(probDTO.getSoundId())
                .orElseThrow(() -> new RuntimeException("Son non trouvé")));
            prob.setProbability(probDTO.getProbability());
            soundProbabilityRepository.save(prob);
        }
        
        return box;
    }

    @Transactional
    public TirageResultDTO effectuerTirage(Integer boxId, Integer userId) {
        Box box = boxRepository.findById(boxId)
            .orElseThrow(() -> new RuntimeException("Box non trouvée"));
            
        // Vérification si la box est accessible
        if (box.getHidden()) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            if (!user.getSecret_bool()) {
                throw new RuntimeException("Box non accessible");
            }
        }

        // Récupération des probabilités
        List<SoundProbability> probabilities = 
            soundProbabilityRepository.findByBoxContentId(box.getBoxContent().getId());
        
        // Tirage aléatoire
        Sound sound = effectuerTirageAleatoire(probabilities);
        
        // Enregistrement du tirage
        Tirage tirage = new Tirage();
        tirage.setUser(userRepository.getReferenceById(userId));
        tirage.setBox(box);
        tirage.setDate_tirage(new Date());
        tirage.setInfo_tirage("Tirage de " + sound.getNom());
        tirageRepository.save(tirage);
        
        // Ajout à la collection
        boolean isNew = ajouterSoundACollection(userId, sound);
        
        // Création du résultat
        TirageResultDTO result = new TirageResultDTO();
        result.setSound(sound);
        result.setRarete(sound.getRarete().toString());
        result.setIsNew(isNew);
        result.setMessage(isNew ? "Nouveau son obtenu !" : "Son dupliqué !");
        
        return result;
    }

    private Sound effectuerTirageAleatoire(List<SoundProbability> probabilities) {
        double totalProb = probabilities.stream()
            .mapToDouble(SoundProbability::getProbability)
            .sum();
            
        double random = new Random().nextDouble() * totalProb;
        double cumsum = 0.0;
        
        for (SoundProbability prob : probabilities) {
            cumsum += prob.getProbability();
            if (random <= cumsum) {
                return prob.getSound();
            }
        }
        
        throw new RuntimeException("Erreur lors du tirage");
    }

    private boolean ajouterSoundACollection(Integer userId, Sound sound) {
        UsersCollection collection = usersCollectionRepository
            .findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Collection non trouvée"));

        UserCollectionSound collectionSound = new UserCollectionSound();
        collectionSound.setCollection(collection);
        collectionSound.setSound(sound);
        
        try {
            UserCollectionSound result = userCollectionSoundService.addSoundToCollection(collectionSound);
            return result.getQuantity() == 1; // retourne true si c'est un nouveau son
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout à la collection", e);
        }
    }
    
    public Box updateBox(Integer id, Box boxDetails) {
        Box box = getBoxById(id);
        box.setNom(boxDetails.getNom());
        box.setHidden(boxDetails.getHidden());
        box.setBoxContent(boxDetails.getBoxContent());
        return boxRepository.save(box);
    }
    
    public void deleteBox(Integer id) {
        boxRepository.deleteById(id);
    }

    public Box createBox(Box box) {
        return boxRepository.save(box);
    }
} 