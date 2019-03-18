import java.util.ArrayList;
import java.util.List;

public class Ovelsegruppe {

    private String navn;
    private String muskelgruppe;
    private List<Ovelse> ovelser;

    public Ovelsegruppe(String navn, String muskelgruppe, List<Ovelse> ovelser){

        this.navn = navn;
        this.muskelgruppe = muskelgruppe;
        this.ovelser = new ArrayList<Ovelse>(ovelser);
    }

    public String getNavn(){
        return navn;
    }

    public String getMuskelgruppe(){
        return muskelgruppe;
    }

    public List<Ovelse> getOvelser(){

        return ovelser;

    }

}
