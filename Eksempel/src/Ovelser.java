public class Ovelser {
    private String navn, resultat;

    public Ovelser(String navn, String resultat) {
        this.navn = navn;
        this.resultat = resultat;
    }

    public final String getNavn(){
        return this.navn;
    }

    public String getResultat(){
        return this.resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }
}
