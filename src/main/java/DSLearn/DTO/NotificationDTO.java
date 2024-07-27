package DSLearn.DTO;

import java.io.Serializable;
import java.time.Instant;

import DSLearn.entities.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NotificationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 6, message = "Minimum 6 characters")
	@NotBlank(message = "Required field")
	private String text;

	@NotNull(message = "Moment is required")
	private Instant moment;

	private boolean read;

	@NotBlank(message = "Route is required")
	private String route;

	private UserNotificationDTO user;

	public NotificationDTO() {
	}

	public NotificationDTO(Long id, String text, Instant moment, boolean read, String route, UserNotificationDTO user) {
		this.id = id;
		this.text = text;
		this.moment = moment;
		this.read = read;
		this.route = route;
		this.user = user;
	}

	public NotificationDTO(Notification entity) {
		this.id = entity.getId();
		this.text = entity.getText();
		this.moment = entity.getMoment();
		this.read = entity.isRead();
		this.route = entity.getRoute();
		if (entity.getUser() != null) {
			this.user = new UserNotificationDTO(entity.getUser());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public UserNotificationDTO getUser() {
		return user;
	}

	public void UserNotificationDTO(UserNotificationDTO user) {
		this.user = user;
	}
}
