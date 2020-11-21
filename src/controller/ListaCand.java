package controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class ListaCand implements Comparable {
	
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
	
	public void removerCand(String CPF){
		NO aux = inicio;
		NO aux2 = inicio;
		remocao(aux, aux2, CPF);
	}

	private void remocao(NO aux, NO aux2, String CPF) {
		
		while (!((Candidato) aux.getDado()).getCPF().equals(CPF)){
			aux2 = aux;
			aux = aux.getProx();
		}
		
		aux2.setProx(aux.getProx());
		atualizarDados();
		JOptionPane.showMessageDialog(null, "Inscrição cancelada!");
		
	}
	
	public boolean confirmarDoc(String nome){
		String caminho = "C:\\Users\\DaniloKevin\\Desktop";
		File arq = new File(caminho, nome + ".txt");
		
		if (arq.exists()){
			JOptionPane.showMessageDialog(null, "Inscrição e Entrega de documentos confirmada!");
			return true;
		} else {
			return false;
			
		}
	}
	
	
	
	public Candidato[] ordenarLista(){
		Candidato dados[] = new Candidato[verificarTamanho()];
		preencherVetor(dados);
		
		return dados;
	}
	
	private int verificarTamanho() {
		NO aux = inicio;
		int cont = 0;  
		
		while (aux != null){
			cont++;
			aux = aux.getProx();
		}
		
		return cont;
	}
	
	private Candidato[] preencherVetor(Candidato[] dados) {
		NO aux = inicio;
		
		for(int i = 0; i < dados.length; i++){
			dados[i] = (Candidato) aux.getDado();
			aux = aux.getProx();
		}
		
		return dados;
	}
	
	
	public Candidato[] quickSort(Candidato[] vetor, int ini, int fim){
		int divisao;
		if (ini < fim + 1){
			divisao = particao(vetor, ini, fim);
			quickSort(vetor, ini, divisao-1);
			quickSort(vetor, divisao+1, fim);
		}
		
		return vetor;
	}
	
	private void troca(Candidato[] vetor, int index1, int index2){
		Candidato temp = vetor[index1];
		vetor[index1] = vetor[index2];
		vetor[index2] = temp;
	}
	
	private int getPivot(int low, int high){
		return (low+high)/2;
	}

	private int particao(Candidato[] vetor, int ini, int fim) {
		troca(vetor, ini, getPivot(ini, fim));
		
		int border = ini + 1;
		
		for (int i = border; i <= fim; i++){
			if (vetor[i].getNome().compareTo(vetor[ini].getNome()) < 0){
				troca(vetor, i, border++);
			}
		}
		
		troca(vetor, ini, border-1);
		return border - 1;
	}
	
	
	
	public void createFile(String conteudo, int numRelatorio) throws IOException {
		//esse é utilizado para criar um novo documento com os candidatos ordenados por nome
		
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "OrdemAlfabetica[" + numRelatorio + "].txt";
		
		
		
			
		File dir = new File(path);
		File arq = new File(path, name);
			
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
	
	public void openFile(String path, String name) throws IOException {
		File arq = new File(path, name);
		if (arq.exists() && arq.isFile()){
			Desktop desktop = Desktop.getDesktop();
			desktop.open(arq);
		} else {
			throw new IOException("Arquivo inválido");
		}
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
