package pl.damianlegutko.fprecrutation.exchange.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "STOCKS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class Stock {
    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Company company;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long stockAmount;
}