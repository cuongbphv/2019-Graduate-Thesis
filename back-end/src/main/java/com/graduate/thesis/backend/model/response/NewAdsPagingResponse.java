package com.graduate.thesis.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 19/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewAdsPagingResponse {

    private int totalPages;

    private long totalElements;

    private List<?> content;
}
