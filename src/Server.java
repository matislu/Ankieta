import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static final int Port = 55555;

    public static void main(String[] args) throws IOException {

        JDBC dataBase = new JDBC();
        dataBase.connect();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Port);

            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new ServerRunnable(socket, dataBase));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            dataBase.closeConnection();
            System.out.println("\nServer wylaczony");
        }
    }
}
