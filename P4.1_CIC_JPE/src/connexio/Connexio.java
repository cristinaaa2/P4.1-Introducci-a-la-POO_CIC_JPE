package connexio;
/**
 * Classe connexió del servidor
 * @author Cristina de la Iglesia, Jordi Palomino
 * @version 1.0
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
    La Classe Connexio es on esta la configuracio del Servidor perque els clinets es poguin conectar.
*/
public class Connexio {
    /**
     * Mètode per obtenir i mostrar la IP del servidor
     * @throws IOException
     */
    public static void mostrarIP() throws IOException {
        String ip;
        Socket s = new Socket("www.google.com",80);
        ip = s.getLocalAddress().getHostAddress();
        System.out.println("La ip del servidor per realitzar la connexió és: " + ip + ":5000");
    }

    /**
     * Mètde per establir la connexó amb el client
     * @param ss ServerSocket per poder fer la connexió
     * @return Connexió Establerta
     */
    public static Socket establirConnexio(ServerSocket ss){
        try {
            return ss.accept();
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Mètdoe per enviar una Espera al client perquè es connecti un 2n jugador
     * @param dout DataOutputStream per enviar missatges d'informació al client
     * @param espera Booleà per saber si és el 1r que es connecta o el 2n
     */
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
