package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Questa classe ha lo scopo di fornire gli strumenti base per effettuare ORM
 * Stabilita la connessione è in grado di eseguire query
 * E restituire in caso di necessità una Lista di mappe o una mappa che descrive un resulset 
 * La connessione di questo dao è sempre aperta!
 * @author lucaf
 *
 */
public abstract class BasicDao {

	private Connection connection;

	public BasicDao(String dbAddress, String user, String password) {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(dbAddress, user, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'oggetto di tipo Statement per effettuare query
	 *
	 * String sql = "SELECT * FROM tabella where id = ?" => params[0] => 1
	 * ?(1) => params[0]
	 * ?(2) => params[1] 
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStm(String sql, Object...params) throws SQLException {
		PreparedStatement stm = connection.prepareStatement(sql);
		
		for (int i = 0; i < params.length; i++) {
			stm.setObject(i + 1, params[i]); // setObject(1, params[0])
		}
		
		return stm;
	}
	
	/**
	 * Restituisce il resultSet risultato di una query
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private ResultSet executeQuery(String sql, Object...conditions) throws SQLException {
		return prepareStm(sql, conditions)
				.executeQuery();
	}
	
	private Map<String, String> mapFromRS(ResultSet rs) throws SQLException {
		Map<String, String> ris = new HashMap<>();
		
		ResultSetMetaData meta = rs.getMetaData();
		
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			ris.put(meta.getColumnName(i), rs.getString(i));
		}
		
		return ris;
	}

	/**
	 * Lista contenente mappe che descrivono un'entity nella persistenza <br>
	 * La mappa è praticamente la rappresentazione di una <strong>RIGA</strong> di una tabella <br>
	 * La lista quindi è l'insieme delle righe di una tabella.
	 * @param sql
	 * @return
	 */
	public List<Map<String, String>> getAll(String sql, Object...conditions) {
		List<Map<String, String>> ris = new ArrayList<>();
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			while (rs.next()) {
				Map<String, String> map = mapFromRS(rs);
				ris.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ris;
	}
	
	/**
	 * Restituisce una mappa che descrive una <strong>RIGA</strong> della tabella
	 * @param sql
	 * @param conditions
	 * @return
	 */
	public Map<String, String> getOne(String sql, Object...conditions) {
		// ... è un operatore per dire che questo metodo può accettare da 0 a infiniti parametri di tipo
		// Object
		Map<String, String> ris = null;
		
		try {
			ResultSet rs = executeQuery(sql, conditions);
			
			if (rs.next()) {
				ris = mapFromRS(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ris;
	}
	/**
	 * Esegue una query impostando prima le condizioni
	 * @param sql
	 * @param params
	 */
	public void execute(String sql, Object...params) {
		try {
			prepareStm(sql, params).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce un prepared statement che è in grado di fornire l'id dell'inserimento
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStatatementWithGeneratedKey(String sql, Object...params) throws SQLException {
		PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < params.length; i++) {
			stm.setObject(i + 1, params[i]);
		}
		
		return stm;
	}
	
	/**
	 * Metodo che effettua un insert e restituisce l'id che viene auto generato dal DB
	 * @param sql
	 * @param params
	 * @return
	 */
	public int insertAndGetId(String sql, Object...params) {
		int id = 0;
		try {
			PreparedStatement stm = prepareStatatementWithGeneratedKey(sql, params);
			
			stm.executeUpdate();
			
			// Grazie al fatto che abbiamo specificato al prepared statement di darci indietro l'ID
			// che viene generato dal database
			// possiamo prendere il resultset dal metodo getGeneratedKeys
			ResultSet rs = stm.getGeneratedKeys();
			// devo spostare il cursore del resultset in giù
			// e recupero alla prima colonna l'id generato
			if (rs.next()) {				
				id = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

}

