package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("role")
public class Role {

    @Id
    private String id;

    private String name;

    private String description;

    private List<String> permissions;

    private int status;

}
