package com.wcacg.wcgal.entity;

import com.wcacg.wcgal.service.UserService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private long userId;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    @ColumnDefault("false")
    @Column(name = "admin", nullable = false)
    private boolean admin;

    @Column(name = "head")
    private String head;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createUser")
    private List<Favorite> favorites;

    public User() {}

    public User(long userId) {
        this.userId = userId;
    }

    public User(String userName, String email, String password, boolean admin){
        this.userName = userName;
        this.email = email;
        this.admin = admin;

        this.salt = UUID.randomUUID().toString();
        this.password = UserService.strMd5(password, salt);
    }

    public String getHead() {
        return head == null ? "/img/head/user.jpg" : head;
    }
}
