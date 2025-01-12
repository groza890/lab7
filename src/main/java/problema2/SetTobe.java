package problema2;

public class SetTobe extends InstrumentMuzical {
    TipTobe tip_tobe;
    int nr_tobe;
    private int nr_cinele;

    public SetTobe(String producator, double pret, TipTobe tip_tobe, int nr_tobe, int nr_cinele) {
        super(producator, pret);
        this.tip_tobe = tip_tobe;
        this.nr_tobe = nr_tobe;
        this.nr_cinele = nr_cinele;
    }

    @Override
    public String toString() {
        return super.toString() + ", Tip Tobe: " + tip_tobe + ", Număr Tobe: " + nr_tobe + ", Număr Cinele: " + nr_cinele;
    }
}
