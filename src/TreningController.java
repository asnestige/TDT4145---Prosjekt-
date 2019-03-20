
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


public class TreningController {

    @FXML
    TextField regTreningField, regOvelseField, regApparatField, nSisteOktField, resultLoggField, regOvelsegruppeField, sloOppField, leggTilOvelseIGruppeField;

    @FXML
    Button regTreningButton, regOvelseButton, regApparatButton, nSisteOktButton, resultLoggButton, regOvelsegruppeButton,sloOppButton, personligFormButton, leggTilOvelseIGruppeButton;

    @FXML
    TextArea tekstFelt;

    Connection myConn;

    public void initialize() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
        this.myConn = DBConn.getDBConnection();
    }



    // 1. Registrere apparater, øvelser og treningsøkter med tilhørende data.
    @FXML
    public void registrerOvelse() throws SQLException {
    try{
        List<String> input = Arrays.asList(regOvelseField.getText().split(","));
        String navn = input.get(0).trim();
        String beskrivelse = input.get(1).trim();
        AdminController.settInnOvelse(myConn, navn, beskrivelse);
        tekstFelt.setText("Exercise added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }

    }




    @FXML
    public void registrerApparat() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        try {
            List<String> input = Arrays.asList(regApparatField.getText().split(","));
            String navn = input.get(0).trim();
            AdminController.settInnApparat(myConn, navn);
            tekstFelt.setText("Machine added");

        }

        catch (RuntimeException e){
            System.out.println(e);


            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }





    //hjelper til å lage Timestamp-instans av en string vi får inn på bestemt format "dd.mm.yyyy hh:mm:ss:nn"
    public Timestamp makeTimetamp(List<String> input) {
        List<String> datoString = Arrays.asList(input.get(0).split("\\."));

        int ar = Integer.parseInt(datoString.get(2).trim());
        int maned = Integer.parseInt(datoString.get(1).trim());
        int dag = Integer.parseInt(datoString.get(0).trim());

        List<String> tidString = Arrays.asList(input.get(1).split(":"));
        int time = Integer.parseInt(tidString.get(0).trim());
        int minutt = Integer.parseInt(tidString.get(1).trim());

        return new Timestamp(ar-1900, maned-1, dag, time, minutt,0,0);
    }






    @FXML
    public void registrerTreningsokt() throws SQLException{

        try {
            List<String> input = Arrays.asList(regTreningField.getText().split(","));

            List<String> tidListe = Arrays.asList(input.get(0).split(" "));
            Timestamp timestamp = makeTimetamp(tidListe);

            double varighet = Double.parseDouble(input.get(1).trim());
            int personligForm = Integer.parseInt(input.get(2).trim());
            int personligPrestasjon = Integer.parseInt(input.get(3).trim());
            String notat = input.get(4).trim();
            System.out.println(timestamp);


            AdminController.settInnTreningsokt(myConn, timestamp, varighet, personligForm, personligPrestasjon, notat);
            tekstFelt.setText("Treningsøkten er registrert!");
        }

        catch (RuntimeException e){
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
            System.out.println(e);

        }
    }






    // 2. Få opp informasjon om et antall n sist gjennomførte treningsøkter med notater, der n spesifiseres av brukeren.
    @FXML
    public void getnSisteOkt() throws NumberFormatException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        try {
            List<Treningsokt> treningsokter = AdminController.getNOkter(myConn, Integer.parseInt(nSisteOktField.getText()));
            String result = "Dato \t\t Tidspunkt \t Varighet \t Form \t Prestasjon \t Notat\n";

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
            int start = Integer.parseInt(input.get(1).trim());
            int slutt = Integer.parseInt(input.get(2).trim());
            String ovelseNavn = input.get(0).trim();

            String resultatlogg = "";
            List<String> results = AdminController.getResultatlogg(myConn, ovelseNavn, start, slutt);

            for (String result: results ){
                resultatlogg += result+"\n";
            }
            tekstFelt.setText(resultatlogg);
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
            System.out.println(e);
        }
    }


    // 4. Lage øvelsesgrupper og finne øvelser som er i samme gruppe
    @FXML
    public void registrerOvelsegruppe() throws SQLException {
        try{
            List<String> input = Arrays.asList(regOvelsegruppeField.getText().split(","));
            String navn = input.get(0).trim();
            String muskelgruppe = input.get(1).trim();
            AdminController.settInnOvelsegruppe(myConn, navn, muskelgruppe);
            tekstFelt.setText("ExerciseGroup added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }

    @FXML
    public void leggTilOvelseIGruppe() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            List<String> input = Arrays.asList(leggTilOvelseIGruppeField.getText().split(","));

            System.out.println(input);

            int gruppeID = Integer.parseInt(input.get(0).trim());
            int ovelseID = Integer.parseInt(input.get(1).trim());
            AdminController.settInnOvelseIGruppe(myConn, gruppeID, ovelseID);
            tekstFelt.setText("Exercise added to exercise group.");

        }catch (RuntimeException e) {
            System.out.println(e);
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

