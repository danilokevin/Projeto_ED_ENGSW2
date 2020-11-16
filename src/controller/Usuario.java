package controller;

public class Usuario {
	
	private String categoria;
	private String nome;
	private String CPF;
	private String senha;
	
	public Usuario(String categoria, String nome, String CPF, String senha) {
		super();
		this.categoria = categoria;
		this.nome = nome;
		this.CPF = CPF;
		this.senha = senha;
	}
	
	

	public String getCPF() {
		return CPF;
	}



	public void setCPF(String cPF) {
		CPF = cPF;
	}



	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	
	

}
