package ipn.escom.webide.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
@Entity
@Table(name="users")
public class User {

	@NotEmpty(message="El campo es requerido.")
	private String username;

	@NotEmpty(message="El campo es requerido.")
	private String password;

	@NotEmpty(message="El campo es requerido.")
	private String confirmPassword;

	@NotEmpty(message="El campo es requerido.")
    @Email(message="El formato es incorrecto.")
	private String email;

	@NotEmpty(message="El campo es requerido.")
	private String country;

	@NotEmpty(message="El campo es requerido.")
	private String language;

	@NotEmpty
	private int enabled = 1;

	@Id
	@Column(name="username", length=255)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name="password", nullable=false, length=255)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="confirm_password", nullable=false, length=255)
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Column(name="email", nullable=false, length=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email= email;
	}

	@Column(name="country", nullable=false, length=50)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="language", nullable=false, length=50)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name="enabled", nullable=false, columnDefinition = "TINYINT(1)")
	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public void clearFields() {
		username = "";
		email = "";
		country = "";
		language = "";
	}
	
}
