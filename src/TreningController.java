
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.html.HTMLDocument.RunElement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class TreningController {


    @FXML
    TextField regTreningField, regOvelseField, regApparatField, nSisteOktField, resultLoggField, regOvelsegruppeField, sloOppField;

    @FXML
    Button regTreningButton, regOvelseButton, regApparatButton, nSisteOktButton, resultLoggButton, regOvelsegruppeButton,sloOppButton, personligFormButton;

    @FXML
    TextArea tekstFelt;

    Connection myConn;

    public void initialize() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

        this.myConn = DBConn.connect();

    }



    // 1. Registrere apparater, øvelser og treningsøkter med tilhørende data.
    @FXML
    public void registrerOvelse() throws SQLException {
    try{
        List<String> input = Arrays.asList(regOvelseField.getText().split(","));
        String navn = input.get(0);
        String beskrivelse = input.get(1);
        AdminController.settInnOvelse(myConn, navn, beskrivelse);
        tekstFelt.setText("Exercise added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }

    }




    @FXML
    public void registrerApperat() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        try {
            List<String> input = Arrays.asList(regApparatField.getText().split(","));
            String navn = input.get(0);
            int apparatID = Integer. parseInt(input.get(1));
            //legg til apparat
            AdminController.settInnApparat(myConn, apparatID, navn);
            tekstFelt.setText("Machine added");
        }

        catch (RuntimeException e){

            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }





    //hjelper til å lage Timestamp-instans av en string vi får inn på bestemt format "dd.mm.yyyy hh:mm:ss:nn"
    public Timestamp makeTimetamp(List<String> input) {

        List<String> datoString = Arrays.asList(input.get(0).split("."));
        int ar = Integer.parseInt(datoString.get(2));
        int maned = Integer.parseInt(datoString.get(1));
        int dag = Integer.parseInt(datoString.get(0));

        List<String> tidString = Arrays.asList(input.get(1).split(":"));
        int time = Integer.parseInt(tidString.get(0));
        int minutt = Integer.parseInt(tidString.get(1));

        return new Timestamp(ar, maned, dag, time, minutt,0,0);
    }






    @FXML
    public void registrerTreningsokt() throws SQLException{
        try {
            List<String> input = Arrays.asList(regTreningField.getText().split(" "));

            Timestamp timestamp = makeTimetamp(input);

            int varighet = Integer.parseInt(input.get(2));
            int personligForm = Integer.parseInt(input.get(3));
            int personligPrestasjon = Integer.parseInt(input.get(4));
            String notat = input.get(5);

            AdminController.settInnTreningsokt(myConn, timestamp, varighet, personligForm, personligPrestasjon, notat);
        }

        catch (RuntimeException e){
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }






    // 2. Få opp informasjon om et antall n sist gjennomførte treningsøkter med notater, der n spesifiseres av brukeren.
    @FXML
    public void getnSisteOkt() throws NumberFormatException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        try {
            List<Treningsokt> treningsokter = AdminController.getNOkter(myConn, Integer.parseInt(nSisteOktField.getText()));
            String result = "Date \t\t tidspunkt \t varighet \t Form \t Prestasjon \t Notat\n";

            for(Treningsokt treningsokt : treningsokter ) {
                result += treningsokt.getTidsstempel().toString() + "\t";
                result += treningsokt.getVarighet() + "\t\t  ";
                result += treningsokt.getPersonligForm() + "\t\t ";
                result += treningsokt.getPersonligPrestasjon() + "\t\t\t";
                result += treningsokt.getNotat() + "\n";
            }

            tekstFelt.setText(result);

        }catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }





    // 3. For hver enkelt øvelse skal det være mulig å se en resultatlogg i et gitt tidsintervall
    @FXML
    public void getResultatlogg() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try {
            List<String> input = Arrays.asList(resultLoggField.getText().split(","));
            List<String> start = Arrays.asList(input.get(1).split(" "));
            List<String> slutt = Arrays.asList(input.get(2).split(" "));
            String ovelseNavn = input.get(0);

            //finne start timestamp

            Timestamp fra = makeTimetamp(start);

            //finne end timestamp
            Timestamp til = makeTimetamp(slutt);

            String resultatlogg = "";
            List<String> results = AdminController.getResultatlogg(myConn, ovelseNavn, fra, til);

            for (String result: results ){
                resultatlogg += result;
            }
            tekstFelt.setText(resultatlogg);
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }


    // 4. Lage øvelsesgrupper og finne øvelser som er i samme gruppe
    @FXML
    public void registrerOvelsegruppe() throws SQLException {
        try{
            List<String> input = Arrays.asList(regOvelsegruppeField.getText().split(","));
            String navn = input.get(0);
            String muskelgruppe = input.get(1);
            AdminController.settInnOvelsegruppe(myConn, navn, muskelgruppe);
            tekstFelt.setText("ExerciseGroup added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }



    @FXML
    public void getOvelse() throws SQLException{
        String result = "";
        String input = sloOppField.getText();
        List<Ovelse> g = AdminController.getOvelsegruppe(myConn, input);
        for (Ovelse ovelse: g){
            result += ovelse.toString();
        }
        tekstFelt.setText(result);

    }

    // 5. Finne gjennomsnittlig personlig form
    @FXML
    public void getGjennomsnitt()  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

        List<Treningsokt> treningsokter = AdminController.getNOkter(myConn, Integer.parseInt(nSisteOktField.getText()));
        int result = 0;
        for(Treningsokt treningsokt : treningsokter ){
            result += treningsokt.getPersonligForm();
        }
        int gjennomsnitt = result/treningsokter.size();

        tekstFelt.setText(String.valueOf(gjennomsnitt));
    }



}

