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
@Table(name = "positions")
@NamedQueries({
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Position.findAll",
        query = "SELECT p FROM Position p"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Position.findByName",
        query = "SELECT p FROM Position p WHERE p.name = :name"
    )
})
@NamedNativeQueries({
	@NamedNativeQuery(
        name = "eu.wansdyke.smaash.core.Position.setPosition",
        query = "merge into positions (name, position) key(position) values (:name, :position)"
    )
})
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position", nullable = false)
    private long position;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(final long position) {
        this.position = position;
    }
}
