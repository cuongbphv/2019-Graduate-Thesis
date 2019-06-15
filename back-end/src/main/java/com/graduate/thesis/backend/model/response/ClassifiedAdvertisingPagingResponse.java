package com.graduate.thesis.backend.model.response;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
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
public class ClassifiedAdvertisingPagingResponse {

    List<ClassifiedAdvertisingElastic> content;

    private long totalRecord;

    private int pageNumber;

    private int pageSize;
}
