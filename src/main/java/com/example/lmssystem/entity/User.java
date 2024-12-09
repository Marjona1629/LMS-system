package com.example.lmssystem.entity;

import com.example.lmssystem.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private Integer passwordSize;
    private String imageUrl;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> role;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Branch> branches;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Boolean canLogin;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Timestamp birthDate;
    private Boolean deleted;
    private String locale="en";

    public List<Long> getBranchess(){
        List<Long> branches = new ArrayList<>();
        for (Branch branch : this.branches) {
            branches.add(branch.getId());
        }
        return branches;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role1 : role) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role1.getName()));
            for (Permission permission : role1.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}