package com.rim.auth.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "refresh_token")
@Data
public class RefreshToken {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

@Column(nullable = false, unique = true, length = 500)
private String token;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;

private LocalDateTime expiryDate;

private boolean revoked = false;

public RefreshToken(){}

public RefreshToken(User user, String token, LocalDateTime expiryDate){
    this.user = user;
    this.token = token;
    this.expiryDate = expiryDate;
}


}
