
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.html.HTMLDocument.RunElement;

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


    public void registrerOvelse(){
    try{

        List<String> input = Arrays.asList(regOvelseField.getText().split(","));
        String navn = input.get(0);
        String beskrivelse = input.get(1);
        AdminController.insertExercise(myConn, navn, beskrivelse);
        tekstFelt.setText("Exercise added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }

    }

    public void registrerApperat() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        try {
            List<String> input = Arrays.asList(regApparatField.getText().split(","));
            String navn = input.get(0);
            String beskrivelse = input.get(1);
//legg til apperat
            AdminController.insertMachine(myConn, navn, beskrivelse);
            tekstFelt.setText("Machine added");
        }

        catch (RuntimeException e){

            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }

    }

    public void registrerTreningsokt(){

        try {
            List<String> input = Arrays.asList(regTreningField.getText().split(","));


            List<String> dateString = Arrays.asList(input.get(0).split("-"));
            int ar = Integer.parseInt(dateString.get(0));
            int maned = Integer.parseInt(dateString.get(1));
            int dag = Integer.parseInt(dateString.get(2));
            Date dato = new Date(ar - 1900, maned - 1, dag);
            Time tid = Time.valueOf(input.get(1));


            int varighet = Integer.parseInt(input.get(2));
            int personligform = Integer.parseInt(input.get(3));
            int prestasjon = Integer.parseInt(input.get(4));
            String notat = input.get(5);
        }

        catch (RuntimeException e){
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }


    }

    public void registrerOvelsegruppe(){
        try{
            List<String> input = Arrays.asList(regOvelsegruppeField.getText().split(","));
            String navn = input.get(0);
            AdminController.insertExerciseGroup(myConn, navn);
            tekstFelt.setText("ExerciseGroup added");
        }
        catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }

        }

    @FXML
    public void getnSisteOktField() throws NumberFormatException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        try {
            List<Treningsokt> treningsokter = AdminController.getNWorkouts(myConn, Integer.parseInt(nSisteOktField.getText()));
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

    @FXML
    public void getGjennomsnitt(){

        List<Treningsokt> treningsokter = AdminController.getNWorkouts(myConn, Integer.parseInt(nSisteOktField.getText()));
        int result = 0;
        for(Treningsokt treningsokt : treningsokter ){
            result += treningsokt.getPersonligForm();
        }
        int gjennomsnitt = result/treningsokter.size();

        tekstFelt.setText(String.valueOf(gjennomsnitt));
    }

    @FXML
    public void getExerciseResult() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        try {
            List<String> input = Arrays.asList(resultLoggField.getText().split(","));
            List<String> startDate = Arrays.asList(input.get(0).split("-"));
            List<String> endDate = Arrays.asList(input.get(1).split("-"));
            int startYear = Integer.parseInt(startDate.get(0));
            int startMonth = Integer.parseInt(startDate.get(1));
            int startDay = Integer.parseInt(startDate.get(2));
            Date dateStart = new Date(startYear, startMonth, startDay);
            int endYear = Integer.parseInt(endDate.get(0));
            int endMonth = Integer.parseInt(endDate.get(1));
            int endDay = Integer.parseInt(endDate.get(2));
            Date dateEnd = new Date(endYear, endMonth, endDay);
            String result = AdminController.getExerciseResult(myConn, dateStart, dateEnd);
            tekstFelt.setText(result);

        }catch (RuntimeException e) {
            tekstFelt.setText("Error: Key is already taken or you wrote unvalid data");
        }
    }



}

