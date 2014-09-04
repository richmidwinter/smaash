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
@Table(name = "principals")
@NamedQueries({
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Principal.findAll",
        query = "SELECT p FROM Principal p"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Principal.deleteByUsername",
        query = "DELETE FROM Principal WHERE username = :username"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Principal.findByUsername",
        query = "SELECT p FROM Principal p WHERE p.username = :username"
    )
})
@NamedNativeQueries({
	@NamedNativeQuery(
        name = "eu.wansdyke.smaash.core.Principal.setLabel",
        query = "merge into principals (username, label) key(username) values (:username, :label)"
    )
})
public class Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "label", nullable = false)
    private long label;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public long getLabel() {
        return label;
    }

    public void setLabel(final long label) {
        this.label = label;
    }
}
