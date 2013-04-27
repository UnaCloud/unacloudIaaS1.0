package loglistener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Clouder
 */
public class Main {

    public static void main(String[] args) {
        final long l = System.currentTimeMillis();
        try {
            ServerSocket ss = new ServerSocket(1576);
            while (true) {
                try {
                    Socket s = ss.accept();
                    s.setSoTimeout(1000);
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    System.out.println(br.readLine() + " " + new Date(System.currentTimeMillis()));
                    s.close();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
        }


    }
}
