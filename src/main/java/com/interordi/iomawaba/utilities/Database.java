package com.interordi.iomawaba.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Database {
	
	private String database = "";


	public Database(String dbHost, int dbPort, String dbUsername, String dbPassword, String dbBase) {
		database = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword + "&useSSL=false";
	}
	
	
	//Get all active perks of a player
	public Map< Instant, String > getWarnings(UUID player) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		Map< Instant, String > warnings = new HashMap< Instant, String >();
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"SELECT message, date " + 
				"FROM players__warnings " +
				"WHERE player = ? "
			);
			
			pstmt.setString(1, player.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Instant i = Instant.ofEpochSecond(rs.getLong("date"));
				warnings.put(i, rs.getString("message"));
			}
			rs.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("Query: " + query);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return warnings;
	}
	
	
	//Log a new warning
	public boolean logWarning(UUID uuid, String message) {
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);

			LocalDate date = LocalDate.now();
			
			//Record today's visit
			query = "" +
				"INSERT INTO players__warnings (uuid, message, date) " +
				"VALUES (?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, uuid.toString());
			pstmt.setString(2, message);
			pstmt.setString(3, date.toString());
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("Query: " + query);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return true;
	}
}
