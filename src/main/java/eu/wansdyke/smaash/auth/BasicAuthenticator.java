package eu.wansdyke.smaash.auth;

import org.mindrot.jbcrypt.BCrypt;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import com.google.common.base.Optional;

import eu.wansdyke.smaash.core.User;
import eu.wansdyke.smaash.db.UserDAO;

public class BasicAuthenticator implements Authenticator<BasicCredentials, User> {
	
	private UserDAO userDao;
	
	public BasicAuthenticator(UserDAO userDao) {
		this.userDao = userDao;
	}
	
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
    		final User user = userDao.findByUsername(credentials.getUsername());
    	
        if (user != null && BCrypt.checkpw(credentials.getPassword(), user.getPassword())) {
            return Optional.of(user);
        }
        
        return Optional.absent();
    }
}