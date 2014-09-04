package eu.wansdyke.smaash;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.hibernate.SessionFactory;

import eu.wansdyke.smaash.auth.BasicAuthenticator;
import eu.wansdyke.smaash.core.Position;
import eu.wansdyke.smaash.core.Principal;
import eu.wansdyke.smaash.core.Resource;
import eu.wansdyke.smaash.core.User;
import eu.wansdyke.smaash.db.PositionDAO;
import eu.wansdyke.smaash.db.PrincipalDAO;
import eu.wansdyke.smaash.db.ResourceDAO;
import eu.wansdyke.smaash.db.UserDAO;
import eu.wansdyke.smaash.resources.SmaashResource;

public class SmaashApplication extends Application<SmaashConfiguration> {
    private final HibernateBundle<SmaashConfiguration> hibernateBundle =
        new HibernateBundle<SmaashConfiguration>(Principal.class,
        			Resource.class, Position.class, User.class) {
            public DataSourceFactory getDataSourceFactory(SmaashConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
	
    public static void main(final String[] args) throws Exception {
        new SmaashApplication().run(args);
    }

    @Override
    public String getName() {
        return "smaash";
    }

    @Override
    public void initialize(final Bootstrap<SmaashConfiguration> bootstrap) {
    		bootstrap.addBundle(new MigrationsBundle<SmaashConfiguration>() {
            public DataSourceFactory getDataSourceFactory(SmaashConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final SmaashConfiguration configuration,
                    final Environment environment) {
    		final SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    		final ResourceDAO rDao = new ResourceDAO(sessionFactory);
    		final PositionDAO posDao = new PositionDAO(sessionFactory);
		final PrincipalDAO pDao = new PrincipalDAO(sessionFactory);
		final UserDAO uDao = new UserDAO(sessionFactory);
		
		final SmaashResource resource = new SmaashResource(configuration,
				pDao, rDao, posDao, uDao);
		
		final BasicAuthProvider<User> auth = new BasicAuthProvider<>(new BasicAuthenticator(uDao), "smaash-realm");
		
//        final TemplateHealthCheck healthCheck =
//                new TemplateHealthCheck(configuration.getTemplate());
        
//        environment.jersey().register(CachingAuthenticator.wrap(auth, CacheBuilderSpec.parse(configuration.getAuthenticationCachePolicy())));
        environment.jersey().register(auth);
//        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }
}
