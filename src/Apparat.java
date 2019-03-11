public class Apparat {

    private final int apparatID;
    private String apparatNavn;

    public Apparat(int apparatID, String apparatNavn){
        this.apparatID= apparatID;
        this.apparatNavn= apparatNavn;
    }

    public Apparat getApparat(){
        return this;
    }

    public String getApparatNavn(){
        return this.apparatNavn;
    }

    public int  getApparatID(){
        return this.apparatID;
    }

    public void setNavn(String navn){
        this.apparatNavn= navn;
    }










}
