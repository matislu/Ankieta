import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerRunnable implements Runnable {

    private Socket socket;
    private JDBC dataBase;

    public ServerRunnable(Socket socket, JDBC dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            String idKlienta = String.valueOf(UUID.randomUUID());

            ResultSet rs = dataBase.pobierzPytania();
            rs.last();
            
            int numRows = rs.getRow();

            int idPytania = 0;

            for (int i = 1; i <= numRows; i++) {
                rs = dataBase.pobierzPytanie(i);
                while (rs.next()) {
                    idPytania = rs.getInt("idPytania");
                    out.println(rs.getString("pytanie"));
                    out.println(rs.getString("A"));
                    out.println(rs.getString("B"));
                    out.println(rs.getString("C"));
                    out.println(rs.getString("D"));
                }
                String odp = in.readLine();

                dataBase.dodajOdpowiedz(idKlienta, idPytania, odp);
                dataBase.zaktualizujWyniki(idPytania, odp);
            }
     
            rs = dataBase.pobierzOdpowiedzi(idKlienta);
            List<String> rs2 = new ArrayList<>();
            while (rs.next()) {
                rs2.add(rs.getString("odpowiedz"));
            }
            rs = dataBase.pobierzWyniki();
            
            while (rs.next()) {
                int counter = 0;
                counter += Integer.parseInt(rs.getString("A"));
                counter += Integer.parseInt(rs.getString("B"));
                counter += Integer.parseInt(rs.getString("C"));
                counter += Integer.parseInt(rs.getString("D"));

                out.println(Integer.parseInt(rs.getString("A")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("B")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("C")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("D")) * 100 / counter);             
            }
            out.println("exit");
            out.close();
            in.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
