package wy.practice.neo4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wy.practice.neo4j.data.entity.Developer;
import wy.practice.neo4j.service.DeveloperService;

@RestController
public class DeveloperController {
    @Autowired
    private DeveloperService developerService;

    @GetMapping("/")
    public Developer findAmud() {
        return developerService.findAmud();
    }
}