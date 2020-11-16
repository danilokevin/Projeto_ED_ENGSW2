package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class ListaCand {
	
private NO inicio;
	
	public ListaCand(){
		inicio = null;
	}
	
	public void AdicionaFinal(Candidato e){
		//lista simplesmente encadeada
		if (inicio == null){
			NO n = new NO(e);
			inicio = n;
		} else {
			NO aux = inicio;
			while(aux.prox != null){
				aux = aux.prox;
			}
			
			NO n = new NO(e);
			aux.prox = n;
		}
		
		//salvar no txt
		try {
			createFile(e.getNome(), e.getCPF(), e.getEmail(), e.getNumInscricao(), e.getFase1(), e.getFase2(), e.getFase3());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	
	public void atualizarDados(){
		NO aux = inicio;
		boolean existe = false;
		int aux2 = 1;
		
		while (aux != null){
			Candidato candAtualizar = (Candidato) aux.getDado();
			
			
			try {
				createFile(candAtualizar.getNome(), candAtualizar.getCPF(), candAtualizar.getEmail(), candAtualizar.getNumInscricao(), 
						candAtualizar.getFase1(), candAtualizar.getFase2(), candAtualizar.getFase3(), existe, aux2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			aux = aux.prox;
			aux2++; //incrementa porque quando 1 ele escreve o cabecario
			existe = true; //muda para true para não sobrescrever após o segundo dado
		}
	}
	
	private void createFile(String nome, String CPF, String email, String numInscricao, String fase1, String fase2, String fase3, boolean existe, int aux) throws IOException {
		//esse método é utilizado para atualizar o arquivo
		String cabecario = "";
		String conteudo = "";
		
		if (aux == 1){
			cabecario = "nome, CPF, email, numInscricao, fase1, fase2, fase3";
			conteudo += cabecario + "\r\n"  + nome + "," + CPF + "," + email + "," + numInscricao + "," + fase1 + "," + fase2 + "," + fase3;
		} else {
			conteudo = "\r\n" + nome + "," + CPF + "," + email + "," + numInscricao + "," + fase1 + "," + fase2 + "," + fase3;
		}
		
		//arquivo txt salvo na area de trabalho
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "CandidatosProcesso";
		
		File dir = new File(path);
		File arq = new File(path, name + ".txt");
		
		if (dir.exists() && dir.isDirectory()){
			FileWriter fileWriter = new FileWriter(arq, existe);
			PrintWriter print = new PrintWriter(fileWriter);
			print.write(conteudo);
			print.flush();
			print.close();
			fileWriter.close();
			
		} else {
			throw new IOException("Diretório Inválido");
		}
		
		
	}
	
	private void createFile(String nome, String CPF, String email, String numInscricao, String fase1, String fase2, String fase3) throws IOException {
		//esse é utilizado para salvar o novo candidato assim que ele relizada a inscrição
		String conteudo = "\r\n" + nome + "," + CPF + "," + email + "," + numInscricao + "," + fase1 + "," + fase2 + "," + fase3;
		
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "CandidatosProcesso";
		
		File dir = new File(path);
		File arq = new File(path, name + ".txt");
		
		if (dir.exists() && dir.isDirectory()){
			boolean existe = false;
			
			if (arq.exists()) {
				existe = true;
			}
			
			FileWriter fileWriter = new FileWriter(arq, existe);
			PrintWriter print = new PrintWriter(fileWriter);
			print.write(conteudo);
			print.flush();
			print.close();
			fileWriter.close();
			
		} else {
			throw new IOException("Diretório Inválido");
		}
		
		
	}
	
	public void readFile() throws IOException {
		//esse método é acionado na hora da sincronização dos dados, quando iniciamos o programa
		//ele lê o arquivo txt com e atribui as informações salvas na lista
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "CandidatosProcesso.txt";
		int qtdeSincron = 0; //controla a quantidade de elementos sincronizados
		
		
		File arq = new File(path, name);
		
		if (arq.exists() && arq.isFile()){
			FileInputStream fluxo = new FileInputStream(arq);
			InputStreamReader leitor = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitor);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			
			while (linha != null){
				String[] vetor = linha.split(",");
				
				Candidato sincCand = new Candidato(vetor[0], vetor[1], vetor[2], vetor[3], vetor[4], vetor[5], vetor[6]);
				
				
				SincronizacaoInicial(sincCand, 1);
				qtdeSincron++;
				
				linha = buffer.readLine();
			}
			
			buffer.close();
			leitor.close();
			fluxo.close();
			
			System.out.println(qtdeSincron + " candidatos do processo sincronizados.");
			
		} else {
			throw new IOException("Arquivo inválido");
		}
		
	}
	


	public void SincronizacaoInicial(Candidato e, int sincr){
		if (inicio == null){
			NO n = new NO(e);
			inicio = n;
		} else {
			NO aux = inicio;
			while(aux.prox != null){
				aux = aux.prox;
			}
			
			NO n = new NO(e);
			aux.prox = n;
		}
	}
	
	
	public Candidato buscaCand(String CPF){
		//realizar busca sequencial de um candidato através do número do CPF
		NO aux = inicio;
		Candidato candAtual = busca(aux, CPF);	
		
		return candAtual;
	}

	private Candidato busca(NO aux, String CPF) {
		boolean cond = false;
		Candidato cand = null;
		
		while (aux != null && cond == false){
			if (((Candidato) aux.getDado()).getCPF().equals(CPF)){
						cond = true;
						cand = (Candidato) aux.getDado();
			} else {
				aux = aux.getProx();
			}
		} 
		
		return cand;
	}

}
