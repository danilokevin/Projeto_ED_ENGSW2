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
	static int faseProcesso = 3; //essa variável vai ajudar na hora de validar a inscrição e pedido de recursos
	static int numRelatorio = 1;

	public static void main(String[] args) {
		
		Usuario novoUser;
		Candidato novoCand;
		
		int option = 0;
		
		//sincronização dos dados. Carrega aquilo que já foi feito anteriormente asism que o programa inicia (ver console)
		try {
			lUser.readFile();
			lCand.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (option != 9){
			option = Integer.parseInt(JOptionPane.showInputDialog("MENU PRINCIPAL \n 1. Login \n 2.Cadastro \n "
					+ "9.FINALIZAR"));
			
			switch(option){
			case 1:
				while (option != 99){
					option = Integer.parseInt(JOptionPane.showInputDialog("TELA DE ACESSO \n 1. Identificar-se \n "
							+ "99.SAIR"));
					
					switch (option){
					case 1:
						Usuario userAtual = loginUser(); //informa e valida os dados e retorna um usuário
						
						if (userAtual != null){
							
								if (userAtual.getCategoria().equals("C")){ //"C" de candidato
									while (option != 9){
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL CANDIDATO \n 1. Realizar "
												+ "inscrição no Processo \n 2. Consultar Situação \n 3. Cancelar inscrição \n 9.SAIR"));
										
										Candidato candAtual = lCand.buscaCand(userAtual.getCPF()); //verifica se candidato já não está inscrito
										
										switch(option){
										case 1:
											
											if (candAtual == null){
												if (faseProcesso == 1){
													novoCand = inscricaoProcesso(userAtual.getNome(), userAtual.getCPF());//novo candidato aproveitando os dados do usuário logado, só vai pedir e-mail e gerar o protocolo de inscrição
													lCand.AdicionaFinal(novoCand);
												} else {
													JOptionPane.showMessageDialog(null, "Prazo para inscrições encerrado");
												}
												
											} else {
												JOptionPane.showMessageDialog(null, "Inscrição já foi realizada!");
											}
											break;
											
										case 2:
											
											//mostra o status do candidato em cada fase, se aprovado ou reprovado
											if (candAtual != null){
												JOptionPane.showMessageDialog(null, "FASE 1 - INSCRIÇÃO        \t\t --> " + candAtual.getFase1() + 
																					"\nFASE 2 - ENTREVISTA      \t\t --> " + candAtual.getFase2() +
																					"\nFASE 3 - PONTUAÇÃO FINAL \t\t --> " + candAtual.getPontuacao());
											}else {
												//isso se ele estiver inscrito no processo
												JOptionPane.showMessageDialog(null, "Candidato não inscrito no processo");
											}
											break;
										
										case 3:
											if (candAtual != null){
												int conf = Integer.parseInt(JOptionPane.showInputDialog("Tem certeza? \n 1. SIM \n 2. CANCELAR"));
												
												if (conf == 1){
													lCand.removerCand(userAtual.getCPF());
												}
											} else {
												JOptionPane.showMessageDialog(null, "Candidato não inscrito no processo");
											}
											break;
										}
									}
								} else {
									while (option != 9){
										//"F" de Funcionário
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL FUNCIONÁRIO \n 1. Avaliação "
												+ "Candidato \n 2. Emitir lista de candidatos \n 3. Lista Final \n 9.SAIR"));
										
										switch (option){
										case 1:
											
											Candidato candAval = buscarCandidato(); //busca candidato pelo número do cpf
											
											if (candAval != null){
												switch(faseProcesso){
												case 1:
													if (lCand.confirmarDoc(candAval.getCPF())){
														candAval.setFase1("APROVADO");
													} else {
														candAval.setFase1("REPROVADO");
														JOptionPane.showMessageDialog(null, "Entrega de documentos não confirmada!");
													}
													
													break;
												case 2:
													
													String avaliacao;
													int resultado = Integer.parseInt(JOptionPane.showInputDialog("RESULTADO \n 1.Aprovado \n 2.Reprovado"));
													
													//informa se reprovado ou aprovado
													if (resultado == 1){
														avaliacao = "APROVADO";
													} else {
														avaliacao = "REPROVADO";
													}
													candAval.setFase2(avaliacao);
													
													JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
													break;
												case 3:
													candAval.setPontuacao(pontuarCand());
													JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
													break;
												}
												
												//atualizada arquivo txt
												lCand.atualizarDados();
											
											
											} else {
												JOptionPane.showMessageDialog(null, "CPF inválido! Nenhum candidato encontrado! \n Verifique!");
											}
											
//											JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
											
											//atribuição de acordo com a fase em que o processo se encontra
											break;
										case 2:
											
											Candidato[] candidatos = lCand.ordenarLista();
											candidatos = lCand.quickSortAlfa(candidatos, 0, candidatos.length - 1);
											
											String name = "OrdemAlfabetica[" + numRelatorio + "].txt";
											
											try {
												for (int i = 0; i < candidatos.length; i++){
													String conteudo = "\r\n" + candidatos[i].getNome() + "," + candidatos[i].getCPF() + "," + candidatos[i].getEmail() + "," + candidatos[i].getNumInscricao();
													lCand.createFile(conteudo, name);
												}
												
											} catch (IOException e) {
												e.printStackTrace();
											}
											
											String path = "C:\\Users\\DaniloKevin\\Desktop";
											
											
											try {
												lCand.openFile(path, name);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											numRelatorio++;
											break;
											
										case 3:
											Candidato[] resultadoFinal = lCand.ordenarLista();
											
											
											resultadoFinal = lCand.quickSortNum(resultadoFinal, 0, resultadoFinal.length - 1);
											
											name = "OrdemPontuacao[" + numRelatorio + "].txt";
											
											try {
												for (int i = 0; i < resultadoFinal.length; i++){
													String conteudo = "\r\n" + resultadoFinal[i].getNome() + "," + resultadoFinal[i].getCPF() + "," + resultadoFinal[i].getEmail() + "," + resultadoFinal[i].getNumInscricao() + "," + resultadoFinal[i].getPontuacao();
													lCand.createFile(conteudo, name);
												}
												
											} catch (IOException e) {
												e.printStackTrace();
											}
											
											path = "C:\\Users\\DaniloKevin\\Desktop";
											
											
											try {
												lCand.openFile(path, name);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											numRelatorio++;
											break;
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
					option = Integer.parseInt(JOptionPane.showInputDialog("TELA DE CADASTRO \n 1. Identificar-se \n 2. Recuperar a senha \n 99.SAIR"));
					
					switch (option){
					case 1:
						novoUser = cadastroPlataforma();
						
						if (novoUser == null){
							JOptionPane.showMessageDialog(null, "Realize o login ou Recupere sua senha!");
						} else {
							lUser.AdicionaFinal(novoUser);
							JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso! Acesso liberado à plataforma!");
						}
						
						break;					
					case 2:
						String CPF = JOptionPane.showInputDialog("Informe seu CPF");
						lUser.recuperaSenha(CPF);
						
						
						option = 99;
						break;
					}
					
				}
				
			}
		}

	}

	private static String pontuarCand() {
		
		int tempo = (int) ((Math.random() * 100) + 80);
		return Integer.toString(tempo);
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
		Usuario user = null;
		
		while (CPF == ""){
			CPF = JOptionPane.showInputDialog("CPF");
		}
		
		if (lUser.buscaUser(CPF)){
			JOptionPane.showMessageDialog(null, "CPF já cadastrado!");
		} else {
			
			while (!(auxCategoria == 1 || auxCategoria == 2)){
				auxCategoria = Integer.parseInt(JOptionPane.showInputDialog("Selecione o perfil: \n 1. Candidato \n 2. Funcionário"));
				
				if (!(auxCategoria == 1 || auxCategoria == 2)){
					JOptionPane.showMessageDialog(null, "Selecione uma opção válida!");
				}
			}
			
			if (auxCategoria == 1){
				categoria = "C";
			} else {
				categoria = "F";
			}
			
			
			nome = JOptionPane.showInputDialog("NOME");
			
			while (!(senha.equals(auxSenha))){
				senha = JOptionPane.showInputDialog("SENHA");
				auxSenha = JOptionPane.showInputDialog("CONFIRME A SENHA");
				
				if (!(senha.equals(auxSenha))){
					JOptionPane.showMessageDialog(null, "Senhas não conferem! Tente novamente");
				}
			}
			
			user = new Usuario(categoria, nome, CPF, senha);
			
		}
		
		return user;
		
	}

}
