package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Id
    private String email;
    private String password;
}
