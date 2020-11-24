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
	static int numRelatorio = 1;

	public static void main(String[] args) {
		
		Usuario novoUser;
		Candidato novoCand;
		
		int option = 0;
		
		sincronizarDados();
		
		
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
						Usuario userAtual = loginUser(); //informa e valida os dados e retorna um usu�rio, caso esteja cadastrado
						
						if (userAtual != null){
							
								if (userAtual.getCategoria().equals("C")){ //"C" de candidato
									while (option != 9){
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL CANDIDATO \n 1. Realizar "
												+ "inscri��o no Processo \n 2. Consultar Situa��o \n 3. Cancelar inscri��o \n 9.SAIR"));
										
										Candidato candAtual = lCand.buscaCand(userAtual.getCPF()); //verifica se candidato j� n�o est� inscrito no processo
										//caso, retornar� um null
										
										switch(option){
										case 1:
											
											if (candAtual == null){
												if (faseProcesso == 1){
													novoCand = inscricaoProcesso(userAtual.getNome(), userAtual.getCPF());//novo candidato aproveitando os dados do usu�rio logado, s� vai pedir e-mail e gerar o protocolo de inscri��o
													lCand.AdicionaFinal(novoCand);
												} else {
													JOptionPane.showMessageDialog(null, "Prazo para inscri��es encerrado");
												}
												
											} else {
												JOptionPane.showMessageDialog(null, "Inscri��o j� foi realizada!");
											}
											break;
											
										case 2:
											
											//mostra o status do candidato em cada fase, se aprovado ou reprovado e pontua��o final
											if (candAtual != null){
												JOptionPane.showMessageDialog(null, "FASE 1 - INSCRI��O        \t\t --> " + candAtual.getFase1() + 
																					"\nFASE 2 - ENTREVISTA      \t\t --> " + candAtual.getFase2() +
																					"\nFASE 3 - PONTUA��O FINAL \t\t --> " + candAtual.getPontuacao());
											}else {
												//isso se ele estiver inscrito no processo
												JOptionPane.showMessageDialog(null, "Candidato n�o inscrito no processo");
											}
											break;
										
										case 3:
											//remover candidato do processo
											if (candAtual != null){
												int conf = Integer.parseInt(JOptionPane.showInputDialog("Tem certeza? \n 1. SIM \n 2. CANCELAR"));
												
												if (conf == 1){
													lCand.removerCand(userAtual.getCPF());
												}
											//se estiver inscrito
											} else {
												JOptionPane.showMessageDialog(null, "Candidato n�o inscrito no processo");
											}
											break;
										}
									}
								} else {
									while (option != 9){
										//"F" de Funcion�rio
										option = Integer.parseInt(JOptionPane.showInputDialog("PORTAL FUNCION�RIO \n 1. Avalia��o "
												+ "Candidato \n 2. Emitir lista de candidatos \n 3. Lista Final \n 4.Controle Fase \n 9.SAIR"));
										
										switch (option){
										case 1:
											
											Candidato candAval = buscarCandidato(); //busca candidato a ser avaliado pelo n�mero do cpf
											
											if (candAval != null){
												switch(faseProcesso){ //a avalia��o se dar� pela fase em que o processo se encontra
												case 1: 
													if (lCand.confirmarDoc(candAval.getCPF())){ //retornar� um booleano, caso o arquivo txt, que simboliza a documenta��o
																								//do candidato, tenha sido entregue/salvo na pasta
														candAval.setFase1("APROVADO");
													} else {
														candAval.setFase1("REPROVADO");
														JOptionPane.showMessageDialog(null, "Entrega de documentos n�o confirmada!");
													}
													
													break;
													
												case 2:
													String avaliacao;
													int resultado = Integer.parseInt(JOptionPane.showInputDialog("RESULTADO \n 1.Aprovado \n 2.Reprovado"));
													
													//informa se reprovado ou aprovado na entrevista
													if (resultado == 1){
														avaliacao = "APROVADO";
													} else {
														avaliacao = "REPROVADO";
													}
													candAval.setFase2(avaliacao);
													
													JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
													break;
													
												case 3:
													candAval.setPontuacao(pontuarCand()); //atribui uma nota aleat�ria ao candidato
													JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
													break;
												}
												
												//atualizar arquivo txt com a avalia��o feita
												lCand.atualizarDados();
											
											
											} else {
												//caso o avaliador digite um CPF inexistente
												JOptionPane.showMessageDialog(null, "CPF inv�lido! Nenhum candidato encontrado! \n Verifique!");
											}
											
											break;
										case 2:
											//emiss�o de "relat�rio" com todos os candidatos inscritos no processo, em ordem alfab�tica
											//o arquivo � gerado e exibido
											Candidato[] candidatos = lCand.ordenarLista();
											candidatos = lCand.quickSortAlfa(candidatos, 0, candidatos.length - 1);
											
											String name = "OrdemAlfabetica[" + numRelatorio + "].txt";
											String path = "C:\\Users\\DaniloKevin\\Desktop";
											
											
											String conteudo1 = "CANDIDATOS INSCRITOS NO PROCESSO 'X' - ORDEM ALFAB�TICA";
											
											
											try {
												
												lCand.createFile(conteudo1, name);
												
												for (int i = 0; i < candidatos.length; i++){
													conteudo1 = "\r\n" + candidatos[i].getNome() + "," + candidatos[i].getCPF() + "," + candidatos[i].getEmail() + "," + candidatos[i].getNumInscricao();
													lCand.createFile(conteudo1, name);
												}
												
											} catch (IOException e) {
												e.printStackTrace();
											}
											
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
											
											path = "C:\\Users\\DaniloKevin\\Desktop";
											name = "OrdemPontuacao[" + numRelatorio + "].txt";
											int posicao = 1;
											
											String conteudo2 = "LISTA DE CANDIDATOS INSCRITOS E PONTUA��O FINAL - ORDEM NUM�RICA";
											
											
											
											try {
												
												lCand.createFile(conteudo2, name);
												
												for (int i = 0; i < resultadoFinal.length; i++){
													conteudo2 = "\r\n" + posicao++ + "� - " + resultadoFinal[i].getNome() + "," + resultadoFinal[i].getCPF() + "," + resultadoFinal[i].getEmail() + "," + resultadoFinal[i].getNumInscricao() + "," + resultadoFinal[i].getPontuacao();
													lCand.createFile(conteudo2, name);
												}
												
											} catch (IOException e) {
												e.printStackTrace();
											}
											
											
											
											
											try {
												lCand.openFile(path, name);
											} catch (IOException e) {
												e.printStackTrace();
											}
											
											numRelatorio++;
											break;
											
										case 4:
											faseProcesso = Integer.parseInt(JOptionPane.showInputDialog("Alterar para fase: "));
											
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
							JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso! Acesso liberado � plataforma!");
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

	private static void sincronizarDados() {
		//sincroniza��o dos dados. Carrega aquilo que j� foi feito anteriormente asism que o programa inicia (ver console)
		try {
			lUser.readFile();
			lCand.readFile();
		} catch (IOException e) {
			e.printStackTrace();
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
			JOptionPane.showMessageDialog(null, "CPF j� cadastrado!");
		} else {
			
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
			
			
			nome = JOptionPane.showInputDialog("NOME");
			
			while (!(senha.equals(auxSenha))){
				senha = JOptionPane.showInputDialog("SENHA");
				auxSenha = JOptionPane.showInputDialog("CONFIRME A SENHA");
				
				if (!(senha.equals(auxSenha))){
					JOptionPane.showMessageDialog(null, "Senhas n�o conferem! Tente novamente");
				}
			}
			
			user = new Usuario(categoria, nome, CPF, senha);
			
		}
		
		return user;
		
	}

}
