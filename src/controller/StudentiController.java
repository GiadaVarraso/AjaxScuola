package controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.Dao;
import dao.IDaoScuola;
import entities.Studente;

/**
 * Servlet implementation class StudentiController
 */
@WebServlet({ "/studenti", "/studenti/*" })
public class StudentiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private IDaoScuola dao;   
    private Gson gson;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentiController() {
        super();
      dao= new Dao("jdbc:mysql://localhost:3306/ajaxscuola?", "root", "Giadina96");
      gson= new Gson();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ris="";
		String path=request.getPathInfo();
		
		if(path ==null|| path.equals("/")) {
			ris=gson.toJson(dao.studente());
		}else {
			int id=Integer.parseInt(path.substring(1));
			ris=gson.toJson(dao.studente(id));
		}
		response.setContentType("application/json");		
		response.getWriter().append(ris);
		
	}

	private String body(HttpServletRequest request) throws IOException {
		String ris="";
		BufferedReader br=request.getReader();
		String line=null;
		
		while((line=br.readLine()) !=null) {
			ris+=line;
		}
		
		
		return ris;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ris="";
		String body=body(request);
		if (body==null|| body.equals("")) {
			ris="{\"msg\":\"ko\" }";
		}else {
			Studente s = gson.fromJson(body, Studente.class);
			dao.addStudente(s);
			ris="{\"msg\":\"ok\" }";
		}
		
		response.setContentType("application/json");
		response.getWriter().append(ris);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ris="";
		String body=body(request);
		if (body==null|| body.equals("")) {
			ris="{\"msg\":\"ko\"}";
		}else {
			Studente mod=gson.fromJson(body, Studente.class);
			dao.updateStudente(mod);
			ris="{\"msg\":\"ok\"}";
		}
		
		response.setContentType("application/json");
		response.getWriter().append(ris);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ris="";
		String path=request.getPathInfo();
		if(path==null||path.equals("/")) {
			ris="{\"msg\":\"cannot delete all students\"}";
		}else {
			int id=Integer.parseInt(path.substring(1));
			dao.deleteStudente(id);
			ris="{\"msg\":\"ok\"}";
		}		
		response.setContentType("application/json");
		response.getWriter().append(ris);
	}

}
