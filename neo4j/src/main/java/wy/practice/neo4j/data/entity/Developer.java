package wy.practice.neo4j.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Node("Developer")
public class Developer {
    @Id
    private String name;
    private String country;
    private int age;

    public Developer(String name, String country, int age) {
        this.name = name;
        this.country = country;
        this.age = age;
    }
}
