package servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
    La Classe Connexio es on esta la configuracio del Servidor perque els clinets es poguin conectar.
*/
public class Connexio {
    public static void mostrarip() throws IOException {
        String ip;
        Socket s = new Socket("www.google.com",80);
        ip = s.getLocalAddress().getHostAddress();
        System.out.println("La ip del servidor per realitzar la connexió és: " + ip + ":5000");
    }

    public static Socket establirconnexio(ServerSocket ss){
        try {
            return ss.accept();
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
    public static void enviarEspera(DataOutputStream dout, Boolean espera){
        try {
            if (espera == true) {
                System.out.println("Connexió establerta, esperant el 2n jugador.");
                dout.writeUTF("Connexió establerta, esperant el 2n jugador.");
                dout.flush();
                dout.writeBoolean(false);
                dout.flush();
            }
            else{
                System.out.println("Connexió establerta, ara iniciarà el joc.");
                dout.writeUTF("Connexió establerta, ara iniciarà el joc.");
                dout.flush();
                dout.writeBoolean(true);
                dout.flush();
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
