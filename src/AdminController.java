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

    public static void settInnTreningsokt(Connection conn, Timestamp tidsstempel, double varighet, int personligForm, int personligPrestasjon, String notat) throws SQLException {
        String preQueryStatement = "INSERT INTO treningsøkt (Tidsstempel, Varighet, Form, Prestasjon, Notat) values (?,?,?,?,?)";
        PreparedStatement prepState= conn.prepareStatement(preQueryStatement);

        prepState.setTimestamp(1, tidsstempel);
        prepState.setDouble(2, varighet);
        prepState.setInt(3, personligForm);
        prepState.setInt(4, personligPrestasjon);
        prepState.setString(5, notat);
        prepState.execute();
    }


    public static void settInnOvelse(Connection conn, String navn, String resultat) throws SQLException {
        String sisteOktID = "SELECT ØktID FROM treningsøkt ORDER BY ØktID DESC LIMIT 1";
        PreparedStatement prepState = conn.prepareStatement(sisteOktID);
        ResultSet rs = prepState.executeQuery();

        int siste = 0;
        while (rs.next()) {
            siste = rs.getInt("ØktID");
            System.out.println(siste);
        }

        String preQueryStatement = "INSERT INTO øvelse (Navn, Resultat) values (?,?)";
        PreparedStatement prepState1 = conn.prepareStatement(preQueryStatement);

        prepState1.setString(1, navn);
        prepState1.setString(2, resultat);
        prepState1.execute();

        settInnOvelseUtfort(conn, siste, navn);

    }

    public static void settInnOvelseUtfort(Connection conn, int oktID, String navn) throws SQLException{
        String tilRelasjon = "INSERT INTO øvelseutført (ØktID, Navn) VALUES (?,?)";
        PreparedStatement prepState = conn.prepareStatement(tilRelasjon);

        prepState.setInt(1,oktID);
        prepState.setString(2,navn);
        prepState.execute();
    }

    public static void settInnApparat(Connection conn, String apparatNavn) throws SQLException {

        String preQueryStatement = "INSERT INTO apparat (Apparatnavn) values (?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setString(1, apparatNavn);
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

    public static List<String> getResultatlogg(Connection conn, String ovelseNavn, int start, int slutt) throws SQLException {
        String preQueryStatement = "SELECT Resultat FROM (treningsøkt NATURAL JOIN øvelseutført NATURAL JOIN øvelse) WHERE (Navn = ?) AND (ØktID BETWEEN ? AND ?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setString(1, ovelseNavn);
        prepState.setInt(2, start);
        prepState.setInt(3, slutt);
        ResultSet rs = prepState.executeQuery();

        List<String> resultater = new ArrayList<String>();

        while (rs.next()) {
            String resultat = rs.getString("Resultat");
            resultater.add(resultat);
        }

        return resultater;
    }






    //4. Lage øvelsegrupper og finne øvelser som er i samme gruppe.
    public static void settInnOvelsegruppe(Connection conn, String ovelsegruppenavn, String muskelgruppe) throws SQLException {
        String preQueryStatement = "INSERT INTO øvelsegruppe (Øvelsegruppenavn, Muskelgruppe) VALUES (?, ?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setString(1, ovelsegruppenavn);
        prepState.setString(2, muskelgruppe);
        prepState.execute();
    }

    public static void settInnOvelseIGruppe(Connection conn, int gruppeID, int ovelseID) throws SQLException{
        String preQueryStatement = "INSERT INTO inngåri (gruppeID, ovelseID) VALUES (?, ?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setInt(1, gruppeID);
        prepState.setInt(2, ovelseID);
        prepState.execute();

    }



    public static List<Ovelse> getOvelsegruppe(Connection conn, String ovelsegruppenavn) throws SQLException{
        String preQueryStatement = "SELECT * FROM (øvelsegruppe NATURAL JOIN inngåri NATURAL JOIN øvelsegruppe) WHERE Øvelsegruppenavn = ?";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setString(1, ovelsegruppenavn);
        ResultSet rs = prepState.executeQuery();

        //Lager en map der Ovelsegruppenavn er key, og liste med navnene til Ovelser er value
        Map<String, ArrayList<Ovelse>> inngåri = new HashMap<String, ArrayList<Ovelse>>();
        while (rs.next()) {
            if(inngåri.containsKey(rs.getString("Øvelsegruppenavn"))) {
                return inngåri.get(rs.getString("Øvelsegruppenavn"));
            }
            else {
                Ovelse ovelse = new Ovelse(rs.getString("Navn"), rs.getString("Notat"));
                inngåri.put(rs.getString("Øvelsegruppenavn"), new ArrayList<Ovelse>(Arrays.asList(ovelse)));
            }
        }
        return inngåri.get(rs.getString("Øvelsegruppenavn"));
    }


}
