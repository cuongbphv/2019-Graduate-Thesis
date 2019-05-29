package com.graduate.thesis.backend.model.response.sysAdmin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cuongbphv created on 22/05/2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse {

    private List<?> content;

    private long totalElements;

    private int totalPages;
}
