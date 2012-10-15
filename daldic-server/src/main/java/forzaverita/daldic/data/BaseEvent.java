package forzaverita.daldic.data;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class BaseEvent {

	@Id
	private UUID id;
	
	private Date eventDate;
	
	private String deviceModel;
	
	private String country;
	
	@Override
	public String toString() {
		return "BaseEvent [id=" + id + ", eventDate=" + eventDate + ", deviceModel="
				+ deviceModel + ", country=" + country + "]";
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
}
