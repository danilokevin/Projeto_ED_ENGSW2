package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class ListaUser {
	
	private NO inicio;
	
	public ListaUser(){
		inicio = null;
	}
	
	
	
	public void AdicionaFinal(Usuario e){
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
		
		
		try {
			createFile(e.getCategoria(), e.getNome(), e.getCPF(), e.getSenha());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	
	private void createFile(String categoria, String nome, String CPF, String senha) throws IOException {
		
		String conteudo = "\r\n" + categoria + "," + nome + "," + CPF + "," + senha;
		
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "UsuariosSistema";
		
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
		String path = "C:\\Users\\DaniloKevin\\Desktop";
		String name = "UsuariosSistema.txt";
		int qtdeSincron = 0;
		
		File arq = new File(path, name);
		
		if (arq.exists() && arq.isFile()){
			FileInputStream fluxo = new FileInputStream(arq);
			InputStreamReader leitor = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitor);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			
			while (linha != null){
				String[] vetor = linha.split(",");
				Usuario sincUser = new Usuario(vetor[0], vetor[1], vetor[2], vetor[3]);
				linha = buffer.readLine();
				SincronizacaoInicial(sincUser, 1);
				qtdeSincron++;
			}
			
			buffer.close();
			leitor.close();
			fluxo.close();
			
			System.out.println(qtdeSincron + " usuários da plataforma sincronizados.");
			
		} else {
			throw new IOException("Arquivo inválido");
		}
		
	}
	
	public void SincronizacaoInicial(Usuario e, int sincr){
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
	
	public Usuario buscaUser(String CPF, String nome, String senha){
		NO aux = inicio;
		Usuario userAtual = busca(aux, CPF, nome, senha);	
		
		return userAtual;
	}

	private Usuario busca(NO aux, String CPF, String nome, String senha) {
		boolean cond = false;
		int msg = 1;
		Usuario user = null;
		
		while (aux != null && cond == false){
			if (((Usuario) aux.getDado()).getCPF().equalsIgnoreCase(CPF)){
				if (((Usuario) aux.getDado()).getNome().equals(nome)){
					if (((Usuario) aux.getDado()).getSenha().equals(senha)){
						JOptionPane.showMessageDialog(null, "Bem vindo(a), " + nome);
						cond = true;
						user = (Usuario) aux.getDado();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Senha não confere. Verifique!");
					aux = null;
					msg = 2;
				}				
			} else {
				aux = aux.getProx();
			}
		} 
		
		if (!cond && msg == 1) {
			JOptionPane.showMessageDialog(null, "Usuário NÃO encontrado. Realize seu cadastro!");
		}
		
		return user;
		
		
	}
	
	
	
	public boolean buscaUser(String CPF){
		//realizar busca sequencial de um usuário através do número do CPF
		NO aux = inicio;
		
		return busca(aux, CPF);	
	}

	private boolean busca(NO aux, String CPF) {
		boolean cond = false;
		
		while (aux != null && cond == false){
			if (((Usuario) aux.getDado()).getCPF().equals(CPF)){
				cond = true;
			} else {
				aux = aux.getProx();
			}
		} 
		
		return cond;
	}
	
	public String recuperaSenha(String CPF){
		NO aux = inicio;
		String senha = "";
		
		while (aux != null){
			if (((Usuario) aux.getDado()).getCPF().equals(CPF)){
				senha = ((Usuario) aux.getDado()).getSenha();
				
				JOptionPane.showMessageDialog(null, "Sua senha é: " + senha);
				aux = null;
				
			} else {
				aux = aux.getProx();
			}
		}
		
		return senha;
		
		
	}



	


}
