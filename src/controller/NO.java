package controller;

public class NO {
	public Object dado;
	public NO prox;

	public NO(Object e){
		dado = e;
		prox = null;
	}

	public Object getDado() {
		return dado;
	}

	public void setDado(Object dado) {
		this.dado = dado;
	}

	public NO getProx() {
		return prox;
	}

	public void setProx(NO prox) {
		this.prox = prox;
	}

	@Override
	public String toString() {
		return "NO [dado=" + dado + "]";
	}
	
	
	
	
	
}