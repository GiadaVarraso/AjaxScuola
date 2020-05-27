package dao;

import java.util.List;

import entities.Studente;

public interface IDaoScuola {

	 List<Studente> studente()             ;
	 Studente studente(int id)             ;
	 void deleteStudente(int id)           ;
	 void addStudente(Studente studente)   ;
	 void updateStudente(Studente studente);
	
}
