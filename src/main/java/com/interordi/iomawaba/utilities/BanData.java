package com.interordi.iomawaba.utilities;

import java.time.LocalDateTime;
import java.util.UUID;

public class BanData {

	public UUID uuid;
	public String ip;
	public String reason;
	public String server;
	public LocalDateTime end;


	public BanData(UUID uuid, String ip, String reason, String server, LocalDateTime end) {
		this.uuid = uuid;
		this.ip = ip;
		this.reason = reason;
		this.server = server;
		this.end = end;
	}
	
}
