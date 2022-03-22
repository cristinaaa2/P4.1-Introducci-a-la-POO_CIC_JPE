package jocxarxa;

import Servidor.Connexio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class joc {
        static int f = 8;
        static int c = 9;
        static String[][] tbuit = new String[f][c];
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        try {
            //Mostrar IP del Servidor
            Connexio.mostrarip();

            //Inicialitzar connexió amb el 1r Client
            ServerSocket ss = new ServerSocket(5000);
            Socket s1 = Connexio.establirconnexio(ss);
            DataInputStream din=new DataInputStream(s1.getInputStream());
            DataOutputStream dout=new DataOutputStream(s1.getOutputStream());
            Boolean espera = true;
            Connexio.enviarEspera(dout, espera);

            //Inicialitzar connexió amb el 2n Client
            Socket s2 = Connexio.establirconnexio(ss);
            DataInputStream din2=new DataInputStream(s2.getInputStream());
            DataOutputStream dout2=new DataOutputStream(s2.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            espera = false;
            Connexio.enviarEspera(dout2, espera);
            dout.writeBoolean(true);
            dout.flush();

        } catch(IOException e){
            System.out.println(e);
        }
        //inicialitzarTaulell();
    }

    public static void inicialitzarTaulell() {
            for (int i = 1; i < f - 1; i++) {
                for (int j = 1; j < c -1; j++) {
                    tbuit[i][j] = ".";
                }
            }
            mostrarTaulell();
        }

        public static void mostrarTaulell() {
            System.out.println();
            int n = 0;
            do {
                n++;
                System.out.printf("%2d ", n);
            } while (c-2 != n);
            System.out.println();
            for (int i =1; i < tbuit.length -1; i++) {
                for (int j = 1; j < tbuit[i].length - 1; j++) {
                    System.out.print(" " + tbuit[i][j] + " ");
                }
                System.out.println();
            }
        }
        public static void afagirPeca() {
            System.out.print("En quina columna vols posar la peça (1-7):");
            int cp = scan.nextInt();
            scan.nextLine();
            //comprovarLloc(cp);

        }
        /*public static boolean comprovarLloc(int cp) {


        }
        public static void comprovarLinea() {

        }*/

}
