package com.jahan.usermanagement.dao;

import com.jahan.usermanagement.model.User;

import java.sql.*;
import java.util.*;

// this class provides CRUD DB operations for the table users in the DB
// this is the model in MVC
// process DB connections, insert, delete, update, select SQL statements
public class UserDAO {
	
	private String user = "jah";
	private String password = "123";
	private String dbname = "Skyhawk";
	private String url = "jdbc:mysql://194.168.2.125:3306/"+dbname;
	
	// --------------  SQL statements ----------------------------------------------------- //
	private String INSERT_USER = "insert into users(name, email, country) values(?,?,?);";
	
	private String SELECT_ALL = "select * from users;";
	
	private String UPDATE_USER = "update users set name = ?, email=?, country=? where id=?;";
	
	private String SELECT_USER = "select * from users where id=?;";
	
	private String Delete_user = "delete from users where id=?;";
	
	// -----------------------------------------------------------------------------------

	public Connection getConnection() {
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		}catch(SQLException e) {
			System.err.println(e);
		}catch(ClassNotFoundException e) {
			System.err.println(e);
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return conn;		
	}
	
	// create user
	public boolean insertUser(User u) {
		boolean success = false;
		
		try {
			Connection con = getConnection(); // get a connection
			// need to check if record already exists in the DB
			// we will check email, as it's the primary key
			int count=0;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from users where email = '" + u.getEmail() + "'"     );
			while(rs.next()) {
				count++;
			}
				if(count>0) {
					System.out.println("email already exists");
					success = false;
				}else {
					PreparedStatement pt = con.prepareStatement(INSERT_USER);
					pt.setString(1, u.getName());
					pt.setString(2, u.getEmail());
					pt.setString(3, u.getCountry());
					boolean ok = pt.executeUpdate() > 0;
					if(ok) {
						System.out.println("user inserted into DB " + u);
						success = true;
					}
					else {
						System.out.println("not inserted into db ");
						success = false;
					}
				}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return success;
	}
	
	// update user
	public boolean updateUser(User u) throws SQLException{
		boolean ok = false;
		
		Connection con = getConnection();
		PreparedStatement st = con.prepareStatement(UPDATE_USER);
		
		st.setString(1, u.getName());
		st.setString(2, u.getEmail());
		st.setString(3, u.getCountry());
		st.setInt(4, u.getId());
		
		ok = st.executeUpdate() > 0;
		
		return ok;
	}
	
	
	// select user
	public User selectUser(int id) {
		
		User user = null;
		
		try {
			// get connection to DB
			Connection con = getConnection();
			// create statement using connection
			PreparedStatement st = con.prepareStatement(SELECT_USER);
			st.setInt(1, id);
			// execute query
			ResultSet rs = st.executeQuery();
			// process results
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(name, email, country);
			}
			
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return user;
	}
	
	
	// grab all users
	public List<User> selectAllUsers() {
		
		List<User> users = new ArrayList<>();
		
		System.out.println("selectAllUsers called");
		try {
			// get connection to DB
			
			Connection con = getConnection();
			// create statement using connection
			PreparedStatement st = con.prepareStatement(SELECT_ALL);
			// execute query
			ResultSet rs = st.executeQuery();
			// process results
			while(rs.next()) {
				//int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add( new User(name, email, country));
				System.out.println("name: " + name);
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return users;
	}
	
	
	// delete user
	public boolean deleteUser(int id) {
		
		boolean ok = false;
		
		try {
			// get connection to DB
			Connection con = getConnection();
			// create statement using connection
			PreparedStatement st = con.prepareStatement(Delete_user);
			st.setInt(1, id);
			
			ok = st.executeUpdate() > 0;
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return ok;
	}
	
}
