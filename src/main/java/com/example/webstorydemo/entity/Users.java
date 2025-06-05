package com.example.webstorydemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username")
    }
)
public class Users extends BaseEntity<Long>{

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String avatar;
    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, columnDefinition = "VARCHAR(150)")
    private String hashPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = true, columnDefinition = "VARCHAR(100)")
    private String verifyCode;

    //Role
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Roles> rolesList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserFollow> favoritesList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReadingHistory> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryComment> commentList = new ArrayList<>();

    public enum Status{
        NORMAL,
        VERIFY,
        BLOCK,
        DELETE
    }
}
