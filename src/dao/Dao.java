package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Studente;
import util.BasicDao;

public class Dao extends BasicDao implements IDaoScuola  {

	public Dao(String dbAddress, String user, String password) {
		super(dbAddress, user, password);
	}

	@Override
	public List<Studente> studente() {
		List<Studente> ris=new ArrayList<>();
		List<Map<String,String>> maps=getAll("select * from studenti;");
		for (Map<String, String> map : maps) {
			ris.add(extracted(map));
		}
		
		return ris;
	}

	private Studente extracted(Map<String, String> map) {
		return new Studente(Integer.parseInt(map.get("id")),map.get("nome"),map.get("cognome")
				, map.get("email"), map.get("nTel"),
				Integer.parseInt(map.get("classe")), map.get("sezione"));
	}

	@Override
	public Studente studente(int id) {
		Studente ris=null;
		Map<String,String> map=getOne("select * from studenti where id=?;", id);
		ris=extracted(map);
		return ris;
	}

	@Override
	public void deleteStudente(int id) {
		execute("delete from studenti where id=?", id);
	}

	@Override
	public void addStudente(Studente studente) {
	execute("insert into studenti (nome, cognome, email, nTel, classe, sezione) value (?,?,?,?,?,?);",
			studente.getNome(),studente.getCognome()
			, studente.getEmail(),studente.getnTel(),
			studente.getClasse(), studente.getSezione());
		
	}

	@Override
	public void updateStudente(Studente studente) {
		execute("update studenti set nome =? , cognome =?, email=?, nTel=?, classe=?, sezione=? where id=?;",
				studente.getNome(),studente.getCognome()
				, studente.getEmail(),studente.getnTel(),
				studente.getClasse(), studente.getSezione(),
				studente.getId());
			
	}

}
