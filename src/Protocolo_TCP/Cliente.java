package Protocolo_TCP;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;


public class Cliente {
	public static void main(String args[]) throws Exception {
		// INICIALIZAR O SOCKET DO CLIENTE
		Socket c= new Socket("localhost", 8081);
		InputStream i=c.getInputStream();
		
		// TRECHO DE CÓDIGO PARA EXIBIR INTERFACE DE SELECIONAR O DIRETÓRIO DESTINO DO ARQUIVO
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	
		int choice = chooser.showOpenDialog(chooser);
		
		if (choice != JFileChooser.APPROVE_OPTION) {
			c.close();
			return;
		} 
		
		String filepath;
		String final_path;

		filepath = chooser.getSelectedFile().getAbsolutePath();
		
		// TRECHO PARA INICIAR O FLUXO DE ENTRADA DO ARQUIVO A SER TRANSFERIDO
		DataInputStream d = new DataInputStream(i);
		// RECEBIMENTO DOS METADADOS DO ARQUIVO VIA DATAOUTPUTSTREAM
		String filename = d.readUTF();
		long fileLength = d.readLong();
		// TRECHO PARA DEFINIR O CAMINHO DO ARQUIVO, COMBINANDO O NOME DO ARQUIVO ORIGINAL COM O CAMINHO ESCOLHIDO PELO USUARIO
		final_path = filepath+"\\"+filename; 
		// INICIALIZAR UM ARRAY DE BYTES COM O TAMANHO DO ARQUIVO NO DESTINO
		byte []b=new byte[(int)fileLength];
		
		FileOutputStream f= new FileOutputStream(final_path);
		// INICIO DA CONTAGEM DE TEMPO PARA TRANSFERENCIA DO ARQUIVO
		long startDateClient = System.nanoTime();
		i.read(b, 0, b.length);
		f.write(b, 0, b.length);
		// FECHAMENTO DO SOCKET DO CLIENTE
		c.close();
		// FIM DA CONTAGEM DE TEMPO PARA TRANSFERENCIA DO ARQUIVO
		long endDateClient = System.nanoTime();
		long elapsedTimeClient = endDateClient - startDateClient;
		// CONVERSÕES DE TEMPO E TAMANHO DO ARQUIVO PARA GERAÇÃO DA TAXA DE TRANSFERENCIAS EM MEGABITS/SEGUNDO
		long totalConvertedTime = TimeUnit.SECONDS.convert(elapsedTimeClient, TimeUnit.NANOSECONDS);
		double rounded_filesize_megabits = ((int)fileLength/125000);
		double transfer_spd = (rounded_filesize_megabits/totalConvertedTime);
		
		System.out.println("Velocidade de transferencia do arquivo: "+ transfer_spd + "Mbps");
		// FECHAMENTO DO FLUXO DE BYTES DE SAIDA
		f.close();
	}
}
