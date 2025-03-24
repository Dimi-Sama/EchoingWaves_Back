package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.Sound;
import com.shiki.echo_waves.repositories.SoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SoundService {
    @Autowired
    private SoundRepository soundRepository;
    
    public List<Sound> getAllSounds() {
        return soundRepository.findAll();
    }
    
    public Sound getSoundById(Integer id) {
        return soundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Son non trouvé"));
    }
    
    public Sound createSound(Sound sound) {
        return soundRepository.save(sound);
    }
    
    public Sound updateSound(Integer id, Sound soundDetails) {
        Sound sound = getSoundById(id);
        sound.setNom(soundDetails.getNom());
        sound.setType(soundDetails.getType());
        sound.setRarete(soundDetails.getRarete());
        sound.setLink(soundDetails.getLink());
        return soundRepository.save(sound);
    }
    
    public void deleteSound(Integer id) {
        soundRepository.deleteById(id);
    }
} 