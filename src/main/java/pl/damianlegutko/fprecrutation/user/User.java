package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class User {
    @Id
    @NotNull
    @Size(min = 3, max = 40)
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(min = 8, max = 255)//more because of password encryption
    @Column(nullable = false)
    private String password;

    @NotNull
    @Min(0)
    @Max(999999999)
    @Column(nullable = false)
    private BigDecimal money;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
