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
@Table(name = "resources")
@NamedQueries({
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Resource.findAll",
        query = "SELECT r FROM Resource r"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Resource.deleteByUri",
        query = "DELETE FROM Resource WHERE uri = :uri"
    ),
    @NamedQuery(
        name = "eu.wansdyke.smaash.core.Resource.findByUri",
        query = "SELECT r FROM Resource r WHERE r.uri = :uri"
    )
})
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "eu.wansdyke.smaash.core.Resource.setLabel",
        query = "merge into resources (uri, label) key(uri) values (:uri, :label)"
    )
})
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "label", nullable = false)
    private long label;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public long getLabel() {
        return label;
    }

    public void setLabel(final long label) {
        this.label = label;
    }
}
