package com.shiki.echo_waves.services;

import com.shiki.echo_waves.models.BoxContent;
import com.shiki.echo_waves.repositories.BoxContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class BoxContentService {
    @Autowired
    private BoxContentRepository boxContentRepository;
    
    public List<BoxContent> getAllBoxContents() {
        return boxContentRepository.findAll();
    }
    
    public BoxContent getBoxContentById(Integer id) {
        return boxContentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenu de box non trouvé"));
    }
    
    public BoxContent createBoxContent(BoxContent boxContent) {
        return boxContentRepository.save(boxContent);
    }
    
    public BoxContent updateBoxContent(Integer id, BoxContent boxContentDetails) {
        BoxContent boxContent = getBoxContentById(id);
        boxContent.setBox(boxContentDetails.getBox());
        boxContent.setSoundProbabilities(boxContentDetails.getSoundProbabilities());
        return boxContentRepository.save(boxContent);
    }
    
    public void deleteBoxContent(Integer id) {
        boxContentRepository.deleteById(id);
    }
} 