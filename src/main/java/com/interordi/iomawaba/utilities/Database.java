package com.interordi.iomawaba.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PluginLogger;


public class Database {
	
	private PluginLogger logger = null;

	private String database = "";
	private Set< BanData > bans = new HashSet< BanData >();


	public Database(String dbHost, int dbPort, String dbUsername, String dbPassword, String dbBase, PluginLogger logger) {
		database = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword + "&useSSL=false";
		this.logger = logger;
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
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `io__warnings` ( " +
				"  `id` int(11) NOT NULL AUTO_INCREMENT, " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `message` varchar(100) DEFAULT NULL, " +
				"  `by_uuid` varchar(36) NOT NULL, " +
				"  `by_name` varchar(30) NOT NULL, " +
				"  `date` datetime NOT NULL DEFAULT current_timestamp(), " +
				"  PRIMARY KEY (`id`), " +
				"  KEY `uuid` (`uuid`) " +
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
				"FROM io__warnings " +
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
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}

		return warnings;
	}


	//Load the list of active bans
	public void getActiveBans() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";

		LocalDateTime datetime = LocalDateTime.now();
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"SELECT uuid, ip, reason, server, end, active " + 
				"FROM io__warnings " +
				"WHERE unban_date IS NULL " +
				"  AND `end` < ?"
			);
			
			pstmt.setString(1, datetime.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BanData ban = new BanData(
					UUID.fromString(rs.getString("uuid")),
					null,
					rs.getString("ip"),
					rs.getString("reason"),
					rs.getString("server"),
					LocalDateTime.parse(
						rs.getString("end"),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
					)
				);

				bans.add(ban);
			}
			rs.close();
		} catch (SQLException ex) {
			// handle any errors
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}
		

	}
	
	
	//Log a new warning
	public boolean logWarning(UUID uuid, UUID sourceUuid, String sourceName, String message) {
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);

			LocalDateTime date = LocalDateTime.now();

			String sUuid = (uuid != null) ? uuid.toString() : null;
			String sSourceUuid = (sourceUuid != null) ? sourceUuid.toString() : null;
			
			//Record today's visit
			query = "" +
				"INSERT INTO io__warnings (uuid, by_uuid, by_name, message, date) " +
				"VALUES (?, ?, ?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sUuid);
			pstmt.setString(2, message);
			pstmt.setString(3, sSourceUuid);
			pstmt.setString(4, sourceName);
			pstmt.setString(5, date.toString());
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			// handle any errors
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}

		return true;
	}
	
	
	//Ban a target
	public BanData banTarget(UUID targetUuid, String targetName, String ip, UUID sourceUuid, String sourceName, String server, LocalDateTime endTime, String message) {
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);

			LocalDateTime startTime = LocalDateTime.now();

			String sTargetUuid = (targetUuid != null) ? targetUuid.toString() : null;
			String sSourceUuid = (sourceUuid != null) ? sourceUuid.toString() : null;
			String sStartTime = (startTime != null) ? startTime.toString() : null;
			String sEndTime = (endTime != null) ? endTime.toString() : null;
			
			//Record today's visit
			query = "" +
				"INSERT INTO io__bans (uuid, by_uuid, by_name, reason, server, begin, end, active) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sTargetUuid);
			pstmt.setString(2, sSourceUuid);
			pstmt.setString(3, sourceName);
			pstmt.setString(4, message);
			pstmt.setString(5, server);
			pstmt.setString(6, sStartTime);
			pstmt.setString(7, sEndTime);
			pstmt.setInt(8, 1);
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			// handle any errors
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}

		//Save locally too
		BanData ban = new BanData(
			targetUuid,
			targetName,
			ip,
			message,
			server,
			endTime
		);
		bans.add(ban);

		return ban;
	}


	//Unban a target
	public boolean unbanTarget(UUID targetUuid, String targetName, String ip, UUID sourceUuid, String sourceName, String server, String message) {
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);

			LocalDateTime clearTime = LocalDateTime.now();

			String sSourceUuid = (sourceUuid != null) ? sourceUuid.toString() : null;
			String sTargetUuid = (targetUuid != null) ? targetUuid.toString() : null;

			if (ip != null) {
				query = "" +
					"UPDATE io__bans " +
					"SET active = 0, unban_date = ?, unban_by_uuid = ?, unban_by_name = ?, unban_reason = ? " +
					"WHERE ip = ? ";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, clearTime.toString());
				pstmt.setString(2, sSourceUuid);
				pstmt.setString(3, sourceName);
				pstmt.setString(4, message);
				pstmt.setString(5, ip);
				pstmt.executeUpdate();
			} else {
				targetUuid = getUuidFromUsername(targetName);
				if (targetUuid == null)
					return false;
				sTargetUuid = (targetUuid != null) ? targetUuid.toString() : null;

				query = "" +
					"UPDATE io__bans " +
					"SET active = 0, unban_date = ?, unban_by_uuid = ?, unban_by_name = ?, unban_reason = ? " +
					"WHERE uuid = ? ";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, clearTime.toString());
				pstmt.setString(2, sSourceUuid);
				pstmt.setString(3, sourceName);
				pstmt.setString(4, message);
				pstmt.setString(5, sTargetUuid);
				pstmt.executeUpdate();
			}

		} catch (SQLException ex) {
			// handle any errors
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}

		return true;
	}


	//Get the UUID for the given username
	//TODO: Consider using another source, or a Mojang lookup
	public UUID getUuidFromUsername(String username) {

		Connection conn = null;
		String query = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UUID targetUuid = null;

		try {
			conn = DriverManager.getConnection(database);

			query = "" +
				"SELECT uuid " + 
				"FROM stats_io_players " +
				"WHERE name = ? ";
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, username.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				targetUuid = UUID.fromString(rs.getString("uuid"));
			}
			rs.close();
		} catch (SQLException ex) {
			// handle any errors
			logger.warning("Query: " + query);
			logger.warning("SQLException: " + ex.getMessage());
			logger.warning("SQLState: " + ex.getSQLState());
			logger.warning("VendorError: " + ex.getErrorCode());
		}
		
		return targetUuid;
	}


	//Get a ban for this player, if set
	public BanData getBan(UUID uuid, String ip) {

		LocalDateTime now = LocalDateTime.now();

		for (BanData ban : bans) {
			//TODO: Limit per server if wanted
			if ((uuid == ban.uuid || ip == ban.ip) &&
				(ban.end == null || ban.end.compareTo(now) > 0))
				return ban;
		}

		return null;

	}


	public void setLogger(PluginLogger logger) {
		this.logger = logger;
	}
}
