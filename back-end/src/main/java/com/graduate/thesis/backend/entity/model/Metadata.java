package com.graduate.thesis.backend.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    private String label;

    private String slug;

    private String selectionType;

    private String type;

    private List<MetadataOption> options;
}
