package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.Tirage;
import com.shiki.echo_waves.repositories.TirageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class TirageService {
    @Autowired
    private TirageRepository tirageRepository;
    
    public List<Tirage> getAllTirages() {
        return tirageRepository.findAll();
    }
    
    public Tirage getTirageById(Integer id) {
        return tirageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tirage non trouvé"));
    }
    
    public List<Tirage> getTiragesByUser(Integer userId) {
        return tirageRepository.findByUserId(userId);
    }
    
    public Tirage createTirage(Tirage tirage) {
        tirage.setDate_tirage(new Date());
        return tirageRepository.save(tirage);
    }
    
    public void deleteTirage(Integer id) {
        tirageRepository.deleteById(id);
    }
} 