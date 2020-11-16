package view;

import java.io.IOException;

import javax.swing.JOptionPane;

import controller.Candidato;
import controller.ListaCand;
import controller.ListaUser;
import controller.NO;
import controller.Usuario;

public class MenuPrincipal {
	
	static ListaUser lUser = new ListaUser();
	static ListaCand lCand = new ListaCand();
	static int faseProcesso = 1; //essa vari�vel vai ajudar na hora de validar a inscri��o e pedido de recursos

	public static void main(String[] args) {
		
		Usuario novoUser;
		Candidato novoCand;
		
		int option = 0;
		
		//sincroniza��o dos dados. Carrega aquilo que j� foi feito anteriormente asism que o programa inicia (ver console)
		try {
			lUser.readFile();
			lCand.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (option != 9){
			option = Integer.parseInt(JOptionPane.showInputDialog("MENU PRINCIPAL \n 1. Login \n 2.Cadastre-se \n "
					+ "9.FINALIZAR"));
			
			switch(option){
			case 1:
				while (option != 99){
					option = Integer.parseInt(JOptionPane.showInputDialog("TELA DE ACESSO \n 1. Identificar-se \n "
							+ "99.SAIR"));
					
					switch (option){
					case 1:
						Usuario userAtual = loginUser(); //informa e valida os dados e retorna um novo usu�rio
						
						if (userAtual != null){
							
								if (userAtual.getCategoria().equals("C")){ //"C" de candidato
									while (option != 9){
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL CANDIDATO \n 1. Realizar "
												+ "inscri��o no Processo \n 2. Consultar Situa��o \n 9.SAIR"));
										
										Candidato candAtual = lCand.buscaCand(userAtual.getCPF()); //verifica se candidato j� n�o est� inscrito
										
										switch(option){
										case 1:
											
											if (candAtual == null){
												novoCand = inscricaoProcesso(userAtual.getNome(), userAtual.getCPF());//novo candidato aproveitando os dados do usu�rio logado, s� vai pedir e-mail e gerar o protocolo de inscri��o
												lCand.AdicionaFinal(novoCand);
												JOptionPane.showMessageDialog(null, "Inscri��o realizada com sucesso! \n"
														+ "N�mero de inscri��o: " + novoCand.getNumInscricao());
												
											} else {
												JOptionPane.showMessageDialog(null, "Inscri��o j� foi realizada!");
											}
											break;
											
										case 2:
											
											//mostra o status do candidato em cada fase, se aprovado ou reprovado
											if (candAtual != null){
												JOptionPane.showMessageDialog(null, "FASE 1 \t\t --> " + candAtual.getFase1() + 
																					"\nFASE 2 \t\t --> " + candAtual.getFase2() +
																					"\nFASE 3 \t\t --> " + candAtual.getFase3());
											}else {
												//isso se ele estiver inscrito no processo
												JOptionPane.showMessageDialog(null, "Candidato n�o inscrito no processo");
											}
											break;
											
										}
									}
								} else {
									while (option != 9){
										//"F" de Funcion�rio
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL FUNCION�RIO \n 1. Aprovar "
												+ "Candidato \n 2. Emitir lista de candidatos \n 9.SAIR"));
										
										switch (option){
										case 1:
											Candidato candAval = buscarCandidato(); //busca candidato pelo n�mero do cpf
											String avaliacao;
											int resultado = Integer.parseInt(JOptionPane.showInputDialog("RESULTADO \n 1.Aprovado \n 2.Reprovado"));
											
											//informa se reprovado ou aprovado
											if (resultado == 1){
												avaliacao = "APROVADO";
											} else {
												avaliacao = "REPROVADO";
											}
											
											JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
											
											//atribui��o de acordo com a fase em que o processo se encontra
											switch(faseProcesso){
											case 1:
												candAval.setFase1(avaliacao);
												break;
											case 2:
												candAval.setFase2(avaliacao);
												break;
											case 3:
												candAval.setFase3(avaliacao);
												break;
											}
											
											//atualizada arquivo txt
											lCand.atualizarDados();
											
										}
									}			
								}
								
							
							
						} else {
							JOptionPane.showMessageDialog(null, "E tente novamente");
							option = 99;
						}
						 
						
						
					}
					
				}
			case 2:
				while (option != 99){
					option = Integer.parseInt(JOptionPane.showInputDialog("TELA DE CADASTRO \n 1. Identificar-se \n 99.SAIR"));
					
					switch (option){
					case 1:
						novoUser = cadastroPlataforma();
						lUser.AdicionaFinal(novoUser);
						JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso! Acesso liberado � plataforma!");
						option = 99;
					}
					
				}
				
			}
		}

	}

	private static Candidato buscarCandidato() {
		String CPF = JOptionPane.showInputDialog("CPF DO CANDIDATO");
		
		return lCand.buscaCand(CPF);
	}

	private static Candidato inscricaoProcesso(String nome, String CPF) {
		String email = JOptionPane.showInputDialog("E-mail");
		int numberAleat = (int) ((Math.random() * 10000) + 1);
		String numInscricao = Integer.toString(numberAleat);
		Candidato cand;
		return cand = new Candidato(nome, CPF, email, numInscricao);
	}

	private static Usuario loginUser() {
		String CPF = JOptionPane.showInputDialog("CPF");
		String nome = JOptionPane.showInputDialog("NOME");
		String senha = JOptionPane.showInputDialog("SENHA");
		
		return lUser.buscaUser(CPF, nome, senha);
	}

	private static Usuario cadastroPlataforma() {
		int auxCategoria = 0;
		String categoria;
		String CPF = "";
		String nome;
		String senha = "0";
		String auxSenha = "1";
		Usuario user;
		
		while (!(auxCategoria == 1 || auxCategoria == 2)){
			auxCategoria = Integer.parseInt(JOptionPane.showInputDialog("Selecione o perfil: \n 1. Candidato \n 2. Funcion�rio"));
			
			if (!(auxCategoria == 1 || auxCategoria == 2)){
				JOptionPane.showMessageDialog(null, "Selecione uma op��o v�lida!");
			}
		}
		
		if (auxCategoria == 1){
			categoria = "C";
		} else {
			categoria = "F";
		}
		
		while (CPF == ""){
			CPF = JOptionPane.showInputDialog("CPF");
		}
		
		
		nome = JOptionPane.showInputDialog("NOME");
		
		while (!(senha.equals(auxSenha))){
			senha = JOptionPane.showInputDialog("SENHA");
			auxSenha = JOptionPane.showInputDialog("CONFIRME A SENHA");
			
			if (!(senha.equals(auxSenha))){
				JOptionPane.showMessageDialog(null, "Senhas n�o conferem! Tente novamente");
			}
		}
		
		return user = new Usuario(categoria, nome, CPF, senha);
		
	}

}