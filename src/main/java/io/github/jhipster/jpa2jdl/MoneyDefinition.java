package io.github.jhipster.jpa2jdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class MoneyDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(MoneyDefinition.class);
    private final FieldDefinition fieldDefinition;
    private final String currencyName;
    private final String amountName;

    public MoneyDefinition(final Field moneyField, final String currencyName, final String amountName) {
        this(FieldProcessor.process(moneyField), currencyName, amountName);
    }

    public MoneyDefinition(final FieldDefinition fieldDefinition, final String currencyName, final String amountName) {
        this.fieldDefinition = fieldDefinition;
        this.currencyName = currencyName;
        this.amountName = amountName;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getAmountName() {
        return amountName;
    }

    public void generateMoneyField(final StringBuilder out, final String prefix, final boolean firstField) {
        LOG.debug("Prefix option is not supported in joda.Money field, skipping {}", prefix);
        if (!firstField) {
            out.append(",\n");
        }
        out.append("  " + currencyName + " " + String.class.getSimpleName() + (fieldDefinition.isRequired() ? " required" : "")
            + ( " " + "minlength" + "(" + 3 + ")")
            + ( " " + "maxlength" + "(" + 3 + ")"));
        out.append(",\n");
        out.append("  " + amountName + " " + BigDecimal.class.getSimpleName() + (fieldDefinition.isRequired() ? " required" : ""));
    }
}
