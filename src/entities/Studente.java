package entities;

import util.IMappable;

public class Studente implements IMappable {
	private int id         ;
	private String nome    ;
	private String cognome ;
	private String email   ;
	private String nTel    ;
	private int classe     ;
	private String sezione ;
	public Studente(int id, String nome, String cognome, String email, String nTel, int classe, String sezione) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.nTel = nTel;
		this.classe = classe;
		this.sezione = sezione;
	}
	public Studente(String nome, String cognome, String email, String nTel, int classe, String sezione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.nTel = nTel;
		this.classe = classe;
		this.sezione = sezione;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getnTel() {
		return nTel;
	}
	public void setnTel(String nTel) {
		this.nTel = nTel;
	}
	public int getClasse() {
		return classe;
	}
	public void setClasse(int classe) {
		this.classe = classe;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	@Override
	public String toString() {
		return "Studente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", nTel=" + nTel
				+ ", classe=" + classe + ", sezione=" + sezione + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
