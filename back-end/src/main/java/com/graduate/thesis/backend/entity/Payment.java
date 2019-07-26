package com.graduate.thesis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("payment")
public class Payment {

    @Id
    private String id;

    private String userId;

    private double amount;

    private int beeCoin;

    private String content;

    private Date createdDate;

    private String type;

    private String status;
}
