package com.polotic.taskmanager.security.user;

import com.polotic.taskmanager.model.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;

    @NaturalId(mutable = true)
    private String email;
    private String password;
    private boolean isEnabled = false;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    public UserEntity(String name, String surname, String email, String password, Collection<RoleEntity> roleEntities) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.roles = roleEntities;
    }
}
