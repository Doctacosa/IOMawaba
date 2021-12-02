package com.interordi.iomawaba.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Database {
	
	private String database = "";


	public Database(String dbHost, int dbPort, String dbUsername, String dbPassword, String dbBase) {
		database = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword + "&useSSL=false";
	}
	
	
	//Initialize the database
	public boolean init() {

		//Create or update the required database table
		//A failure indicates that the database wasn't configured properly
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `io__bans` ( " +
				"  `id` int(11) NOT NULL AUTO_INCREMENT, " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `ip` varchar(50) DEFAULT NULL, " +
				"  `by_uuid` varchar(36) NOT NULL, " +
				"  `by_name` varchar(30) NOT NULL, " +
				"  `reason` varchar(100) DEFAULT NULL, " +
				"  `server` varchar(30) NOT NULL, " +
				"  `begin` timestamp NOT NULL DEFAULT current_timestamp(), " +
				"  `end` timestamp NULL DEFAULT NULL, " +
				"  `active` tinyint(1) NOT NULL DEFAULT 1, " +
				"  `unban_date` timestamp NULL DEFAULT NULL, " +
				"  `unban_by_uuid` varchar(36) DEFAULT NULL, " +
				"  `unban_by_name` varchar(30) DEFAULT NULL, " +
				"  `unban_reason` varchar(200) DEFAULT NULL, " +
				"  PRIMARY KEY (`id`), " +
				"  KEY `uuid` (`uuid`), " +
				"  KEY `ip` (`ip`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8; "
			);

			pstmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.err.println("Query: " + query);
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
			return false;
		}

		return true;
	}


	public void addFromBat() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"INSERT INTO `io__bans` (id, uuid, ip, by_name, reason, server, begin, end, active, unban_date, unban_by_name, unban_reason) " +
				"( " +
				"	SELECT ban_id, CONCAT(SUBSTRING(UUID, 1, 8), '-', SUBSTRING(UUID, 9, 4), '-', SUBSTRING(UUID, 13, 4), '-', SUBSTRING(UUID, 17, 4), '-', SUBSTRING(UUID, 21, 12)) AS uuid, ban_ip, ban_staff, ban_reason, ban_server, ban_begin, ban_end, ban_state, ban_unbandate, ban_unbanstaff, ban_unbanreason " +
				"	FROM BAT_ban " +
				")"
			);

			pstmt.executeUpdate();
			
		} catch (SQLException ex) {
			System.err.println("Query: " + query);
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	
	//Get the active warnings on a player
	public Map< Instant, String > getWarnings(UUID player) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		Map< Instant, String > warnings = new HashMap< Instant, String >();

		LocalDate date = LocalDate.now();
		date = date.minusDays(180);
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"SELECT message, date " + 
				"FROM players__warnings " +
				"WHERE player = ? " +
				"  AND `date` >= ?"
			);
			
			pstmt.setString(1, player.toString());
			pstmt.setString(2, date.toString());
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
	
	
	//Ban a player
	//TODO: Switch to own table
	public boolean banPlayer(UUID target, String from, int duration, String message) {
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);

			LocalDateTime startTime = LocalDateTime.now();
			LocalDateTime endTime = LocalDateTime.now();
			endTime = endTime.plusDays(duration);
			
			//Record today's visit
			query = "" +
				"INSERT INTO BAT_ban (UUID, ban_staff, ban_reason, ban_server, ban_begin, ban_end, ban_state) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, target.toString().replace("-", ""));
			pstmt.setString(2, from);
			pstmt.setString(3, message);
			pstmt.setString(4, "(global)");
			pstmt.setString(5, startTime.toString());
			pstmt.setString(6, endTime.toString());
			pstmt.setInt(1, 1);
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
