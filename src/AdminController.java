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

    public static void settInnTreningsokt(Connection conn, Timestamp tidsstempel, int varighet, int form, int prestasjon, String notat) throws SQLException {
        String preQueryStatement = "INSERT INTO treningsøkt (Tidsstempel, Varighet, Form, Prestasjon, Notat) values (?,?,?,?,?)";
        PreparedStatement prepState= conn.prepareStatement(preQueryStatement);

        prepState.setTimestamp(1, tidsstempel);
        prepState.setInt(2, varighet);
        prepState.setInt(3, form);
        prepState.setInt(4, prestasjon);
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
        String preQueryStatement = "INSERT INTO apparat (ApparatID, ApparatNavn) values (?,?)";
        PreparedStatement prepState = conn.prepareStatement(preQueryStatement);

        prepState.setInt(1, apparatID);
        prepState.setString(2, apparatNavn);
        prepState.execute();
    }



    // 2. Få opp informasjon om et antall n sist gjennomførte treningsøkter med notater, der n
    //spesifiseres av brukeren.

    public static List<Treningsokt> hentOkter(Connection conn, int n) throws SQLException {
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

}
