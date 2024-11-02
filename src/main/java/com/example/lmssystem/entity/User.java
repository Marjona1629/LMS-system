package com.example.lmssystem.entity;

import com.example.lmssystem.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
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
    @ManyToOne
    private Role role;
    @ManyToOne
    private Branch branch;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Boolean canLogin;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date birthDate;
    private Boolean deleted;
    private String locale="en";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @OneToMany(mappedBy = "user")
    private Collection<Invoice> invoice;

    public Collection<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(Collection<Invoice> invoice) {
        this.invoice = invoice;
    }
}
