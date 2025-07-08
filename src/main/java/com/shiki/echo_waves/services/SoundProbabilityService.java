package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.SoundProbability;
import com.shiki.echo_waves.repositories.SoundProbabilityRepository;
import com.shiki.echo_waves.dto.SoundProbabilityResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoundProbabilityService {
    @Autowired
    private SoundProbabilityRepository soundProbabilityRepository;
    
    public List<SoundProbability> getAllProbabilities() {
        return soundProbabilityRepository.findAll();
    }
    
    public List<SoundProbabilityResponseDTO> getAllProbabilitiesWithSoundNames() {
        return soundProbabilityRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    public SoundProbability getProbabilityById(Integer id) {
        return soundProbabilityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Probabilité non trouvée"));
    }
    
    public SoundProbabilityResponseDTO getProbabilityByIdWithSoundName(Integer id) {
        SoundProbability probability = getProbabilityById(id);
        return convertToResponseDTO(probability);
    }
    
    public List<SoundProbability> getProbabilitiesByBoxContent(Integer boxContentId) {
        return soundProbabilityRepository.findByBoxContentId(boxContentId);
    }
    
    public List<SoundProbabilityResponseDTO> getProbabilitiesByBoxContentWithSoundNames(Integer boxContentId) {
        return soundProbabilityRepository.findByBoxContentId(boxContentId).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    public SoundProbability createProbability(SoundProbability probability) {
        validateProbability(probability);
        return soundProbabilityRepository.save(probability);
    }
    
    public SoundProbability updateProbability(Integer id, SoundProbability probabilityDetails) {
        SoundProbability probability = getProbabilityById(id);
        validateProbability(probabilityDetails);
        probability.setProbability(probabilityDetails.getProbability());
        probability.setSound(probabilityDetails.getSound());
        probability.setBoxContent(probabilityDetails.getBoxContent());
        return soundProbabilityRepository.save(probability);
    }
    
    public void deleteProbability(Integer id) {
        soundProbabilityRepository.deleteById(id);
    }
    
    private void validateProbability(SoundProbability probability) {
        if (probability.getProbability() < 0 || probability.getProbability() > 100) {
            throw new IllegalArgumentException("La probabilité doit être comprise entre 0 et 100");
        }
    }
    
    private SoundProbabilityResponseDTO convertToResponseDTO(SoundProbability probability) {
        String soundName = probability.getSound() != null ? probability.getSound().getNom() : "Son inconnu";
        String soundType = probability.getSound() != null ? probability.getSound().getType() : "Type inconnu";
        String soundRarity = probability.getSound() != null && probability.getSound().getRarete() != null 
            ? probability.getSound().getRarete().toString() : "Rareté inconnue";
        
        return new SoundProbabilityResponseDTO(
            probability.getId(),
            soundName,
            soundType,
            soundRarity,
            probability.getProbability()
        );
    }
} 