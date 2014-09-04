package eu.wansdyke.smaash.core;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permission {

    @Length(min = 3)
    private String uri;
    
    @Length(min = 3)
    private String principal;
    
    private boolean access;
    
    public Permission() {}
    
    public Permission(final String uri, final String principal, final boolean access) {
    		this.uri = uri;
    		this.principal = principal;
    		this.access = access;
    }
    
    @JsonProperty
    public String getUri() {
    		return uri;
    }
    
    @JsonProperty
    public String getPrincipal() {
    		return principal;
    }
    
    @JsonProperty
    public boolean hasAccess() {
    		return access;
    }
}
