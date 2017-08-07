package io.github.jhipster.jpa2jdl;

public class CurrencyDefinition {

    private final FieldDefinition fieldDefinition;
    private final String currencyName;

    public CurrencyDefinition(FieldDefinition fieldDefinition, String currencyName) {
        this.fieldDefinition = fieldDefinition;
        this.currencyName = currencyName;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void generateCurrencyField(final StringBuilder out, final String prefix, final boolean firstField) {
        if (!firstField) {
            out.append(",\n");
        }
        out.append("  " + FieldDefinition.toDBName(currencyName, prefix) + " " + String.class.getSimpleName() + (fieldDefinition.isRequired() ? " required" : "")
            + ( " " + "minlength" + "(" + 3 + ")")
            + ( " " + "maxlength" + "(" + 3 + ")"));
    }
}
