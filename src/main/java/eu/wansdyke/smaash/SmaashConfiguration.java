package eu.wansdyke.smaash;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmaashConfiguration extends Configuration {
	
    @NotEmpty
    private String auditLog;
    
    @NotEmpty
    private String authenticationCachePolicy;
    
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty
    public String getAuditLog() {
        return auditLog;
    }

    @JsonProperty
    public void setAuditLog(String auditLog) {
        this.auditLog = auditLog;
    }

    @Valid
    @NotNull
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("authenticationCachePolicy")
	public String getAuthenticationCachePolicy() {
		return authenticationCachePolicy;
	}
    
    @JsonProperty("authenticationCachePolicy")
    public void setAuthenticationCachePolicy(String authenticationCachePolicy) {
    		this.authenticationCachePolicy = authenticationCachePolicy;
    }
}
