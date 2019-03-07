package com.graduate.thesis.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Huy Pham
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "session_user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SessionUser implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.graduate.thesis.backend.util.IDGeneratorUtil")
    private String id;

    private String username;

    private String password;

    @CreatedDate
    @Column(name = "login_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date loginDate;

    private String sessionData;
}
