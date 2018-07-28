package pl.damianlegutko.fprecrutation.exchange.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

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

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long stockAmount;

    @NotNull
    @Size(min = 3, max = 256)
    @Column(nullable = false)
    private String userName;

    public void increaseStockAmount(Long increaseByValue){
        this.stockAmount = Long.sum(this.stockAmount, increaseByValue);
    }

    public void decreaseStockAmount(Long decreaseByValue) throws UserHaveNotEnoughStocksException {
        if (this.stockAmount.compareTo(decreaseByValue) < 0) throw new UserHaveNotEnoughStocksException(decreaseByValue, stockAmount);

        this.stockAmount -= decreaseByValue;
    }
}