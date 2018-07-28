package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class User {
    @Id
    @NotNull
    @Size(min = 3, max = 256)
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(min = 8, max = 256)
    @Column(nullable = false)
    private String password;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal money;
}
