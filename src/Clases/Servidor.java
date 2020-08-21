package Clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Servidor {

	// MODIFICABLES
	private final int puerto = 6666;
	String ruta_al_archivo="logs.txt";
	
	//Variables necesarias para funcionamiento
	private String message = " ", chat = " ";
	RandomAccessFile arch;
	

	Servidor() {

		try {
			//creo si no existe logs.txt o lo abro si existe
			CrearAbrirEscrituraArchivo();
			
			//creo un socket en un puerto
			ServerSocket Socketmio = new ServerSocket(puerto);
			
			//acepto cualquier socket que entre a esta IP:puerto
			Socket clienteConectado = Socketmio.accept();
			
			OutputStream OUT = clienteConectado.getOutputStream();
			InputStream IN = clienteConectado.getInputStream();

			PrintWriter output = new PrintWriter(new OutputStreamWriter(OUT));
			BufferedReader input = new BufferedReader(new InputStreamReader(IN));

			while (!message.equalsIgnoreCase("chau")) {
				// recibo mensaje

				message = input.readLine();

				
				//escribe en el log
				Escribir(clienteConectado.getInetAddress() + ":" + clienteConectado.getPort() + " dice: " + message+" , ");
				
				
				//escribe en consola
				System.out.println(clienteConectado.getInetAddress() + ":" + clienteConectado.getPort() + " dice: " + message);
				
				// pido respuesta
				chat = JOptionPane.showInputDialog("Responda el mensaje!!");

				// escribo el mensaje en el log
				Escribir("Usted dice:" + chat+" , ");

				// preparo mensaje a cliente
				output.println(chat);

				// envio mensaje al cliente
				output.flush();

			}
			// cierro log
			cerrarArchivo();
			// cierro cliente
			clienteConectado.close();

			//cierro socket
			Socketmio.close();
			System.out.println("fin server");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
		private void CrearAbrirEscrituraArchivo()
			{
				try
				{
					arch= new RandomAccessFile(ruta_al_archivo,"rw");		
				}
				catch(IOException e)
				{
					System.out.println("error al abrir para escritura");
				}
				
			}

		private void cerrarArchivo()
		{
			try
			{
			arch.close();	
			}
			catch(IOException e)
			{
				System.out.println("error al cerrar");
			}
			
		}

		private void leerTodoPorLineas()
		{
			String linea=new String();
			try
			{
				do
				{
				linea=arch.readLine();
				}while(linea!=null);
			}
			catch(Exception e)
			{
				System.out.println("error al leer"+e);
			}
		}

		private void Escribir(String frase)
		{
			try
			{
				String linea=new String();
				do
				{
				linea=arch.readLine();
				}while(linea!=null);
				arch.writeBytes(frase+"\n");
			}
			catch(Exception e)
			{
				System.out.println("error al grabar");
			}
		}
}

