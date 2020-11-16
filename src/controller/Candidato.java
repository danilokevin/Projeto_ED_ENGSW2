package controller;

import javax.swing.JOptionPane;

public class Candidato {
	
	private String nome;
	private String CPF;
	private String email;
	private String numInscricao;
	private boolean ativo;
	private String fase1;
	private String fase2;
	private String fase3;
	
	public Candidato(){
		
	}
	
	public Candidato(String nome, String CPF, String email, String numInscricao){
		this.nome = nome;
		this.CPF = CPF;
		this.email = email;
		this.numInscricao = numInscricao;
		this.ativo = true;
	}
	
	public Candidato(String nome, String CPF, String email, String numInscricao, String fase1, String fase2, String fase3){
		this.nome = nome;
		this.CPF = CPF;
		this.email = email;
		this.numInscricao = numInscricao;
		this.fase1 = fase1;
		this.fase2 = fase2;
		this.fase3 = fase3;
		this.ativo = true;
	}


	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
	public String getFase3() {
		return fase3;
	}
	public void setFase3(String fase3) {
		this.fase3 = fase3;
	}


	
	
	

}
