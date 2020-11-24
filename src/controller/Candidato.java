package controller;

import javax.swing.JOptionPane;

public class Candidato {
	
	private String nome;
	private String CPF;
	private String email;
	private String numInscricao;
	private String fase1;
	private String fase2;
	private String pontuacao;
	
	public String getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(String pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Candidato(){
		
	}
	
	public Candidato(String nome, String CPF, String email, String numInscricao){
		this.nome = nome;
		this.CPF = CPF;
		this.email = email;
		this.numInscricao = numInscricao;
		this.pontuacao = "";
	}
	
	public Candidato(String nome, String CPF, String email, String numInscricao, String fase1, String fase2, String pontuacao){
		this.nome = nome;
		this.CPF = CPF;
		this.email = email;
		this.numInscricao = numInscricao;
		this.fase1 = fase1;
		this.fase2 = fase2;
		this.pontuacao = pontuacao;
	}


	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCPF() {
		return CPF;
	}
	public void setCPF(String cPF) {
		CPF = cPF;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumInscricao() {
		return numInscricao;
	}
	public void setNumInscricao(String numInscricao) {
		this.numInscricao = numInscricao;
	}
	public String getFase1() {
		return fase1;
	}
	public void setFase1(String fase1) {
		this.fase1 = fase1;
	}
	public String getFase2() {
		return fase2;
	}
	public void setFase2(String fase2) {
		this.fase2 = fase2;
	}
	


	
	
	

}
