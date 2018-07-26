package pl.damianlegutko.fprecrutation.exchange.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ASSETS", uniqueConstraints={
        @UniqueConstraint(columnNames = {"company", "userName"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Company company;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String userName;
}