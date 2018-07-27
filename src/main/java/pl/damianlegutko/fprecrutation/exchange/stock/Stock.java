package pl.damianlegutko.fprecrutation.exchange.stock;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;

import javax.persistence.*;

@Entity
@Table(name = "STOCKS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class Stock {
    @Id
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Company company;

    @Column(nullable = false)
    private Long stockAmount;
}