import java.sql.*;
import java.util.Properties;

public class JDBC {

    private Statement st;
    private Connection connection;

    public JDBC() {
    }

    public void connect() {
        if (checkDriver("com.mysql.jdbc.Driver"))
            System.out.println("... OK");
        else
            System.exit(1);

        connection = getConnection("jdbc:mysql://", "localhost", 3306, "root", "");
        st = createStatement();
        if (executeUpdate("USE baza7;") == 0)
            System.out.println("Baza wybrana");
        else {
            System.out.println("Baza nie istnieje! Tworzymy baze: ");
            if (executeUpdate("create Database baza7;") == 1)
                System.out.println("Baza utworzona");
            else
                System.out.println("Baza nieutworzona!");
            if (executeUpdate("USE baza7;") == 0)
                System.out.println("Baza wybrana");
            else
                System.out.println("Baza niewybrana!");
            createTables();
        }
    }
    
    @SuppressWarnings("deprecation")
	private boolean checkDriver(String driver) {
        System.out.print("Sprawdzanie sterownika bazy danych ");
        try {
            Class.forName(driver).newInstance();
            return true;
        } catch (Exception e) {
            System.out.println("Blad przy ladowaniu sterownika bazy!");
            return false;
        }
    }

    private Connection getConnection(String kindOfDatabase, String adres, int port, String userName, String password) {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        try {
            conn = DriverManager.getConnection(kindOfDatabase + adres + ":" + port + "/",
                    connectionProps);
        } catch (SQLException e) {
            System.out.println("Blad poloczenia z baza danych danych! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(2);
        }
        System.out.println("Poloczenie z baza danych: ... OK");
        return conn;
    }

    private Statement createStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Blad createStatement! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(3);
        }
        return null;
    }

    private int executeUpdate(String sql) {
        try {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            return -1;
        }
    }

    private void createTables() {
        if (executeUpdate("CREATE TABLE pytania (idPytania int(10) NOT NULL, pytanie varchar(100) NOT NULL, " +
                "A varchar(20) NOT NULL, B varchar(20) NOT NULL, C varchar(20) NOT NULL, D varchar(20) NOT NULL);") == 0) {
        	
            executeUpdate("ALTER TABLE pytania ADD PRIMARY KEY (idPytania);");
            
            System.out.println("Tabela pytania utworzona");
            
            String sql = "INSERT INTO pytania VALUES" +
                    "(1, 'Ile masz lat?', 'A. <20', 'B. 21-30', 'C. 31-40', 'D. 40>'), " +
                    "(2, 'Co robisz w zyciu?', 'A. Ucze sie', 'B. Studiuje', 'C. Pracuje', 'D. Inna odpowiedz'), " +
                    "(3, 'Jaki sport uprawiasz?', 'A. Pilka nozna', 'B. Siatkowka', 'C. Koszykowka', 'D. Inna odpowiedz'), " +
                    "(4, 'Jaka dzis pogoda?', 'A. Ladna', 'B. Brzydka', 'C. Srednia', 'D. Bardzo ladna');";
            
            executeUpdate(sql);
            
            System.out.println("Tabela pytania dodana i wypelniona");
            
        } else
            System.out.println("Tabela pytania juz istnieje!");

        if (executeUpdate("CREATE TABLE odpowiedzi (idKlienta varchar(256) NOT NULL, idPytania int NOT NULL, odpowiedz varchar(20) NOT NULL);") == 0) {
            
        	System.out.println("Tabela odpowiedzi dodana");
        	
        } else
            System.out.println("Tabela odpowiedzi juz istnieje!");

        if (executeUpdate("CREATE TABLE wyniki (idPytania int(10) NOT NULL,A int(10) NOT NULL,B int(10) NOT NULL,C int(10) NOT NULL,D int(10) NOT NULL);") == 0) {
            
        	executeUpdate("ALTER TABLE wyniki ADD PRIMARY KEY (idPytania);");
            
        	System.out.println("Tabela wyniki utworzona");
            
        	String sql = "INSERT INTO wyniki VALUES" +
                    "(1, 0, 0, 0, 0), " +
                    "(2, 0, 0, 0, 0), " +
                    "(3, 0, 0, 0, 0), " +
                    "(4, 0, 0, 0, 0);";
            
        	executeUpdate(sql);
            
        	System.out.println("Tabela wyniki wypelniona");
        
        } else
            System.out.println("Tabela wyniki juz istnieje!");

    }

    public ResultSet pobierzPytania() {
        String sql = "Select * from pytania;";
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet pobierzWyniki() {
        String sql = "Select * from wyniki;";
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet pobierzPytanie(int id) {
        String sql = "Select * from pytania where idPytania = " + id;
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet pobierzOdpowiedzi(String idKlienta) {
        String sql = "Select odpowiedz from odpowiedzi where idKlienta ='" + idKlienta + "'";
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void dodajOdpowiedz(String idKlienta, int idPytania, String odpowiedz) {
        String sql = "INSERT INTO odpowiedzi VALUES('" + idKlienta + "'," + idPytania + ",'" + odpowiedz + "');";
        executeUpdate(sql);
    }

    public void zaktualizujWyniki(int idPytania, String answer) {
        String odp = answer;

        String sql = "Select " + odp + " from wyniki where idPytania =" + idPytania;
        int counter = 0;
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                counter = rs.getInt(odp);
            }
            counter++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "UPDATE wyniki set " + odp + " = " + counter + " where idPytania =" + idPytania;
        executeUpdate(sql);
    }

    public void closeConnection() {
        System.out.print("\nZamykanie polaczenia z baza ...");
        try {
            st.close();
            connection.close();
        } catch (SQLException e) {
            System.out
                    .println("Blad przy zamykaniu polaczenia z baza! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(4);
        }
        System.out.print(" zamkniecie OK");
    }

}

