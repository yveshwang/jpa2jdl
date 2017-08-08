package io.github.jhipster.jpa2jdl.example.entities.complex;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MoneyEntity {

    @GeneratedValue(strategy = SEQUENCE) @Id
    private Long id;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit")
    private CurrencyUnit originalCurrency;

    private BigDecimal originalValue;

    @Columns(columns = {@Column(name = "pumpPriceCurrency"), @Column(name = "pumpPriceValue")})
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
    private Money pumpPrice;

    @Embedded
    private EmbeddableMoney embeddedMoney;

}
