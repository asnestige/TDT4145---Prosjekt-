import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class AdminController {


    //1. Registrere apparater, øvelser og treningsøkter med tilhørende data.

    public static void settInnTreningsokt(Connection conn, Timestamp tidsstempel, int varighet, int personligForm, int personligPrestasjon, String notat) throws SQLException {
        String preQueryStatement = "INSERT INTO treningsøkt (Tidsstempel, Varighet, Form, Prestasjon, Notat) values (?,?,?,?,?)";
        PreparedStatement prepState= conn.prepareStatement(preQueryStatement);

        prepState.setTimestamp(1, tidsstempel);
        prepState.setInt(2, varighet);
        prepState.setInt(3, personligForm);
        prepState.setInt(4, personligPrestasjon);
        prepState.setString(5, notat);
        prepState.execute();
    }


    public static void settInnOvelse(Connection conn, String navn, String beskrivelse) throws SQLException {
        String preQueryStatement = "INSERT INTO øvelse (Navn, Beskrivelse) values (?,?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setString(1,navn);
        prepState.setString(2, beskrivelse);
        prepState.execute();
    }

    public static void settInnApparat(Connection conn, int apparatID, String apparatNavn) throws SQLException {

        String preQueryStatement = "INSERT INTO Apparat (ApparatID, ApparatNavn) values (?,?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setInt(1, apparatID);
        prepState.setString(2, apparatNavn);
        prepState.execute();
    }



    // 2. Få opp informasjon om et antall n sist gjennomførte treningsøkter med notater, der n
    //spesifiseres av brukeren.

    public static List<Treningsokt> getNOkter(Connection conn, int n) throws SQLException {
        List<Treningsokt> okter = new ArrayList<Treningsokt>();

        String preQueryStatement = "SELECT * FROM treningsøkt ORDER BY Tidsstempel DESC LIMIT ?";
        //preQueryStatement += n;
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setInt(1,n);
        ResultSet rs = prepState.executeQuery();

        while (rs.next()) {
            Treningsokt okt = new Treningsokt(rs.getTimestamp("Tidsstempel"), rs.getInt("Varighet"), rs.getInt("Form"),
                    rs.getInt("Prestasjon"), rs.getString("Notat"));
            okter.add(okt);

        }
        prepState.execute();
        return okter;
    }




    // 3. For hver enkelt øvelse skal det være mulig å se en resultatlogg i et gitt tidsintervall spesifisert
    // av brukeren.


    public static List<Treningsokt> getResultatlogg(Connection conn, Timestamp startTid, Timestamp sluttTid) throws SQLException {
        String preQueryStatement = "SELECT Resultat FROM OVELSE WHERE Tidsstempel BETWEEN ? AND ?";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);
    }






    //4. Lage øvelsegrupper og finne øvelser som er i samme gruppe.
    public static void lagOvelsegruppe(Connection conn, String ovelsegruppenavn, String muskelgruppe) throws SQLException{
        List<Ovelser> ovelsegrupper = new ArrayList<Ovelser>();

        String statement = "SELECT * FROM InngårI";
        PreparedStatement prepState = conn.prepareStatement(statement);
        ResultSet rs = prepState.executeQuery();

        //Lager en map der Ovelsegruppenavn er key, og liste med navnene til Ovelser er value
        Map<String, ArrayList<String>> inngåri = new HashMap<String, ArrayList<String>>();
        while (rs.next()) {
            if(inngåri.containsKey(rs.getString("Ovelsegruppenavn"))) {
                inngåri.get(rs.getString("Ovelsegruppenavn"));
            }
            else {
                inngåri.put(rs.getString("Ovelsegruppenavn"), new ArrayList<String>(Arrays.asList(rs.getString("Navn"))));
            }
        }



    }

}
