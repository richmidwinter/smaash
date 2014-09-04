package eu.wansdyke.smaash.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.User.findAll",
        query = "SELECT u FROM User u"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.User.deleteByUsername",
        query = "DELETE FROM User WHERE username = :username"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.User.findByUsername",
        query = "SELECT u FROM User u WHERE u.username = :username"
    )
})
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "eu.wansdyke.smaash.core.User.setPassword",
        query = "merge into users (username, password) key(username) values (:username, :password)"
    )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false)
    private String username;
	
    @Column(name = "password", nullable = false)
	private String password;
	
    @Column(name = "isadmin", nullable = false)
	private Boolean isAdmin;
    
	public User() {
	}

    public long getId() {
        return id;
    }

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Boolean isAdmin() {
		return isAdmin;
	}
}
