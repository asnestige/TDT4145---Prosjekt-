import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
public class Treningsokt {

    private Timestamp tidsstempel;
    private int varighet;
    private int personligForm;
    private int personligPrestasjon;
    private String notat;


    //private List<Ovelse> ovelseList; Ha med liste over øvelser?





    public Treningsokt(Timestamp tidsstempel, int varighet,int personligForm, int personligPrestasjon, String notat ) {
        this.tidsstempel = tidsstempel;
        this.varighet = varighet;
        this.personligForm = personligForm;
        this.personligPrestasjon = personligPrestasjon;
        this.notat = notat;
    }


    //public void addOvelse(Ovelse ex) {
    //  this.ovelseList.add(ex);
    //}


    public Timestamp getTidsstempel() {
        return tidsstempel;
    }

    public int getVarighet() {
        return varighet;
    }

    public int getPersonligForm() {
        return personligForm;
    }

    public int getPersonligPrestasjon() {
        return personligPrestasjon;
    }

    public String getNotat() {
        return notat;
    }

}