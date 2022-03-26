package clients;
/**
 * Client joc Connecta 4
 * @author Cristina de la Iglesia, Jordi Palomino
 * @version 1.0
 */
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase Client que fa de Main
 */
public class Client {
    static DataInputStream din;
    static DataOutputStream dout;
    static Socket s;
    static Scanner scan = new Scanner(System.in);
    static String str = "";
    static boolean comprovarguanyador;

    /**
     * Mètode Main del Client
     * @param args
     */
    public static void main(String[] args) {
        String connexio = obtenirConnexio();
        int port = separarPort(connexio);
        connexio = separarIP(connexio);
        try {
            establirConnexio(connexio, port);
            Boolean jugador = esperarJugador();
            System.out.println(din.readUTF());
            procesJocClient(jugador);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Mètode per escriure la connexió del servidor
     * @return String de la IP + port
     */
    private static String obtenirConnexio() {
        String connexio;
        Scanner scan = new Scanner(System.in);
        System.out.print("Digues connexió al servidor: ");
        connexio = scan.nextLine();
        return connexio;
    }

    /**
     * Mètode per obtenir el port de la connexió
     * @param connexio String amb la connexió i el port
     * @return Enter amb el port
     */
    private static int separarPort(String connexio) {
        int delimiter = connexio.indexOf(':');
        String port = connexio.substring(delimiter + 1);
        return Integer.parseInt(port);
    }

    /**
     * Mètode peer treure el port de la variable Connexió
     * @param connexio String amb la IP i el port
     * @return String només amb la IP
     */
    private static String separarIP(String connexio) {
        int delimiter = connexio.indexOf(':');
        connexio = connexio.substring(0, delimiter);
        return connexio;
    }

    private static void establirConnexio(String connexio, int port) throws IOException {
        s = new Socket(connexio, port);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
    }

    /**
     * Mètode per fer l'espera del 2n jugador
     * @return Booleà de l'espera que identifica al jugador.
     */
    private static boolean esperarJugador() {
        try {
            System.out.println(din.readUTF());
            Boolean espera = din.readBoolean();
            if (espera == false) din.readBoolean();
            return espera;
        } catch (IOException e) {
            System.out.println(e);
            return true;
        }
    }

    /**
     * Procés partida per el Client
     * @param jugador booleà que identifica si és el jugador 1 o 2
     * @throws IOException
     */
    private static void procesJocClient(Boolean jugador) throws IOException {
        try {
            while (true) {
                mostrarTaulellClient();
                if (jugador == false) {
                    System.out.print(din.readUTF());
                    comprovarguanyador = din.readBoolean();
                    if (comprovarguanyador == true) {

                        for (int i = 0; i <= 55; ++i) {
                            str = din.readUTF();
                            System.out.print(str);
                        }
                        System.out.println("Has perdut");
                        s.close();
                        return;
                    }
                    jugador = true;
                } else {
                    boolean comprovapeca = false;
                    do {
                        comprovapeca = jugarTorn(comprovapeca);

                    } while (comprovapeca != true);
                    comprovarguanyador = din.readBoolean();
                    if (comprovarguanyador == true) {
                        for (int i = 0; i <= 55; ++i) {
                            str = din.readUTF();
                            System.out.print(str);
                        }
                        System.out.println("Has guanyat");
                        s.close();
                        return;
                    }
                    jugador = false;

                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Mètode per mostrar la taula al client
     * @throws IOException
     */
    private static void mostrarTaulellClient() throws IOException {
        for (int i = 0; i <= 55; ++i) {
            str = din.readUTF();
            System.out.print(str);
        }
    }

    /**
     * Mètode que permet el client posar la peça.
     * @param comprovapeca booleà per comprovar si on posa la peça és vàlid o no
     * @return booleà true si el lloc de la peça és vàlid o false si no.
     * @throws IOException
     */
    private static boolean jugarTorn(boolean comprovapeca) throws IOException {
        System.out.print(din.readUTF());
        dout.writeInt(scan.nextInt());
        scan.nextLine();
        dout.flush();
        comprovapeca = din.readBoolean();
        if (comprovapeca == false) {
            System.out.println(din.readUTF());
        }
        return comprovapeca;
    }
}



