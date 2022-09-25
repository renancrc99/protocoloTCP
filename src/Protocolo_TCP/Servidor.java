package Protocolo_TCP;

import java.io.*;
import java.net.*;

import javax.swing.*;

public class Servidor {
	public static void main(String[] args) throws Exception {
		// TRECHO PARA SELECIONAR O ARQUIVO A SER TRANSFERIDO
		System.out.println("Selecione o arquivo para transferencia: \n");
		
		JFileChooser chooser = new JFileChooser();
		
		int choice = chooser.showOpenDialog(chooser);
		
		if (choice != JFileChooser.APPROVE_OPTION) return;
		
		String filepath;
		String filename;
		
		filepath = chooser.getSelectedFile().getAbsolutePath();
		filename = chooser.getSelectedFile().getName();	
		
		// TRECHO PARA INICIAR O FLUXO DE ENTRADA DO ARQUIVO A SER TRANSFERIDO
		FileInputStream f= new FileInputStream(filepath);
	
		File file = chooser.getSelectedFile();
		
		// COLETAR O TAMANHO DO ARQUIVO
		long filesize = file.length();
		
		if (filesize>Integer.MAX_VALUE) {
			f.close();
			return;
		}
		// INICIALIZAR UM ARRAY DE BYTES COM O TAMANHO DO ARQUIVO
		byte b[]= new byte[(int) filesize];
		// INICIARLIZAR O SOCKET DO SERVIDOR NA PORTA ESCOLHIDA
		ServerSocket sr= new ServerSocket(8081);
		
		Socket sc=sr.accept();
		// INICIAR A LEITURA COM OS BYTES DO ARQUIVO SELECIONADO
		f.read(b, 0, b.length);
		OutputStream o=sc.getOutputStream();
		
		// INICIALIZAR O FLUXO DE SAIDA DE DADOS DO ARQUIVO
		DataOutputStream d = new DataOutputStream(o);
		// ENVIO DE METADADOS DO ARQUIVO VIA DATAOUTPUTSTREAM
        d.writeUTF(filename);
        d.writeLong(filesize);        
		d.write(b, 0, b.length);
		
		// ENCERRAMENTO DOS FLUXOS ABERTOS E DA CONEXÃO DO SOCKET SERVIDOR
		sc.close();
		f.close();
		sr.close();
	}
}
