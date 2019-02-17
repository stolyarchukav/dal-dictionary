package forzaverita.daldic.core.event;

import java.util.Date;

public abstract class BaseEvents {

	private Date eventDate;
	
	private String deviceModel;
	
	private String country;
	
	public BaseEvents() {
	}
	
	public BaseEvents(Date eventDate, String deviceModel, String country) {
		this.eventDate = eventDate;
		this.deviceModel = deviceModel;
		this.country = country;
	}

	@Override
	public String toString() {
		return "BaseEvent [eventDate=" + eventDate + ", deviceModel="
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
	
}
