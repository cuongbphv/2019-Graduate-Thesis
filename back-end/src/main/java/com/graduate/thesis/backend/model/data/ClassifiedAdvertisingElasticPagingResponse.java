package com.graduate.thesis.backend.model.data;

import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
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
public class ClassifiedAdvertisingElasticPagingResponse {

    private long totalRecord;

    List<ClassifiedAdvertisingElastic> items;
}
