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
        static char[][] tbuit = new char[f][c];
        static Socket s1;
        static Socket s2;
        static DataInputStream din1;
        static DataInputStream din2;
        static DataOutputStream dout1;
        static DataOutputStream dout2;
        static ServerSocket ss;
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        try {
            //Mostrar IP del Servidor
            Connexio.mostrarip();

            //Inicialitzar connexió amb el 1r Client
            ss = new ServerSocket(5000);
            s1 = Connexio.establirconnexio(ss);
            din1=new DataInputStream(s1.getInputStream());
            dout1=new DataOutputStream(s1.getOutputStream());
            Boolean espera = true;
            Connexio.enviarEspera(dout1, espera);

            //Inicialitzar connexió amb el 2n Client
            s2 = Connexio.establirconnexio(ss);
            din2=new DataInputStream(s2.getInputStream());
            dout2=new DataOutputStream(s2.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            espera = false;
            Connexio.enviarEspera(dout2, espera);
            dout1.writeBoolean(true);
            dout1.flush();

        } catch(IOException e){
            System.out.println(e);
        }
        inicialitzarTaulell();
    }

    public static void inicialitzarTaulell() {
            for (int i = 1; i < f - 1; i++) {
                for (int j = 1; j < c -1; j++) {
                    tbuit[i][j] = '.';
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
                String enviar = String.format("%2d ", n);
                try{
                    dout1.writeUTF(enviar);
                    dout1.flush();
                    dout2.writeUTF(enviar);
                    dout2.flush();
                } catch (IOException e){
                    System.out.println(e);
                }


            } while (c-2 != n);
            System.out.println();
            for (int i =1; i < tbuit.length -1; i++) {
                for (int j = 1; j < tbuit[i].length - 1; j++) {
                    System.out.print(" " + tbuit[i][j] + " ");
                }
                System.out.println();
            }
        }
        public static void afegirPeca(char peca) {
            int i = 1;
            System.out.print("En quina columna vols posar la peça (1-7):");
            int cp = scan.nextInt();
            scan.nextLine();
            if (comprovarColumnaPlena(cp)) {
                while(tbuit[i + 1][cp] == '·'){ ++i; }
                tbuit[i][cp] = peca;
            } else {
                System.out.println("Aquesta columna està plena tria un altre.");
            }
            comprovarLinia(i, cp);


        }
        public static boolean comprovarColumnaPlena(int cp) {
                int contador = 0;
                if (cp > 0 && cp < 7) {
                    for (int i = 1; i > tbuit.length - 1; i++) {
                        if (tbuit[i][cp] == 'X' || tbuit[i][cp] == 'O') {
                            contador++;
                        }
                    }
                    return (contador == f + 1)  ? false : true;
                }
                return false;
        }
        public static void comprovarLinia(int i, int cp) {
            
        }
}
