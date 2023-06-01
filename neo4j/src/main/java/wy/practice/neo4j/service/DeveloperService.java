package wy.practice.neo4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wy.practice.neo4j.data.entity.Developer;
import wy.practice.neo4j.repository.DeveloperRepository;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public Developer findAmud() {
        return developerRepository.findOneByName("amud").block();
    }
}
