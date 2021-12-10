package com.jahan.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jahan.usermanagement.dao.UserDAO;
import com.jahan.usermanagement.model.User;

/**
 * Servlet implementation class UserServlet
 */

public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UserDAO userdao;
	
	
    
    
    public void init() {
    	userdao = new UserDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		System.out.println("we are called " + action);
		switch(action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			insertUser(request, response);
			break;
		case "/delete":
			deleteUser(request, response);
			break;
		case "/edit":
			showEditForm(request, response);
			break;
		case "/update":
			updateUser(request, response);
			break;
		case "/list":
			listUser(request, response);
			break;
		default:
			showNewForm(request, response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	// list user
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		List<User> listuser = userdao.selectAllUsers();
		
		request.setAttribute("listuser", listuser);
		
		RequestDispatcher dispatch = request.getRequestDispatcher("user-list.jsp");
		dispatch.forward(request, response);
	}
	
	// create new user
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		RequestDispatcher dispatch = request.getRequestDispatcher("user-form.jsp");
		dispatch.forward(request, response);
	}
	
	
	// show edit form, edit user
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		int id = Integer.parseInt( request.getParameter("id") ); // get id from request parameter
		User existinguser = userdao.selectUser(id); // select the user from the DB
		
		RequestDispatcher dispatch = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existinguser); // set the user object
		dispatch.forward(request, response); // send to client to the user-form to edit
	}
	
	
	// update user
	private void updateUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User nu = new User(id, name, email, country);
		try {
			userdao.updateUser(nu);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.sendRedirect("list");
	}

	
	// insert user
	private void insertUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		System.out.println("insert user called..");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		String message = "";
		System.out.println("insert name: " + name);
		User nu = new User(name, email, country);
		if(userdao.insertUser(nu)) {
			message = "success, added " + name;
			RequestDispatcher dispatch = request.getRequestDispatcher("user-list.jsp");
			request.setAttribute("message", message);
			dispatch.forward(request, response);
		}else {
		//response.sendRedirect("list");
			message = "user email already exists " + email;
			RequestDispatcher dispatch = request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("message", message);
			dispatch.forward(request, response);
		}
	}
	
	
	// delete user
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		int id = Integer.parseInt( request.getParameter("id") );
		userdao.deleteUser(id);
		response.sendRedirect("list");
	}
}
