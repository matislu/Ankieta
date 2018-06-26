import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    static final int Port = 55555;
    InetAddress serverHost;
    Socket socket;
    BufferedReader in;
    PrintStream out;


    public void init() throws Exception {
        super.init();
        serverHost = InetAddress.getLocalHost();
        socket = new Socket(serverHost, Port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
    }

    public void start(Stage primaryStage) {
    	Scanner odp = new Scanner(System.in);
        try {
        	String str;
        	String odpo=null;
        	String temp;
        	for(int j=0;j<4;j++) {
        		
        		for(int i=0;i<5;i++) {
        			str = in.readLine();
        			System.out.println(str);
        		}
        		
        		odpo = odp.nextLine();
        		out.println(odpo);
        	}
        	
        	System.out.println("Wyniki");
        	int g=1;
        	while (true) {
        		
        		temp = in.readLine();
        		if (temp.equals("exit"))
        			break;
        		System.out.println(g++);
        		System.out.println("A " + temp + "%");
        		
        		temp = in.readLine();
        		if (temp.equals("exit"))
        			break;
        		System.out.println("B " + temp + "%"); 
        		
        		temp = in.readLine();
        		if (temp.equals("exit"))
        			break;
        		System.out.println("C " + temp + "%");
        		
        		temp = in.readLine();
        		if (temp.equals("exit"))
        			break;
        		System.out.println("D " + temp + "%"); 
        		
        	}           

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws Exception {
        super.stop();
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
