package problema2;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) // Permite serializarea și deserializarea polimorfă
public abstract class InstrumentMuzical {
    protected String producator;
    protected double pret;

    public InstrumentMuzical(String producator, double pret) {
        this.producator = producator;
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Producător: " + producator + ", Preț: " + pret;
    }
}
