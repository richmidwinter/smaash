package eu.wansdyke.smaash.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.mindrot.jbcrypt.BCrypt;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import eu.wansdyke.smaash.SmaashConfiguration;
import eu.wansdyke.smaash.core.Permission;
import eu.wansdyke.smaash.core.Position;
import eu.wansdyke.smaash.core.Principal;
import eu.wansdyke.smaash.core.Resource;
import eu.wansdyke.smaash.core.User;
import eu.wansdyke.smaash.db.PositionDAO;
import eu.wansdyke.smaash.db.PrincipalDAO;
import eu.wansdyke.smaash.db.ResourceDAO;
import eu.wansdyke.smaash.db.UserDAO;
import eu.wansdyke.smaash.logger.Audit;

@Path("/smaash")
@Produces(MediaType.APPLICATION_JSON)
public class SmaashResource {

	private PrincipalDAO pDao;
	private ResourceDAO rDao;
	private PositionDAO posDao;
	private UserDAO uDao;
	private SmaashConfiguration configuration;
	
	private LoadingCache<String, Permission> cache;

	public SmaashResource(final SmaashConfiguration configuration,
			final PrincipalDAO pDao, final ResourceDAO rDao,
			final PositionDAO posDao, final UserDAO uDao) {
		this.configuration = configuration;
		
		this.pDao = pDao;
		this.rDao = rDao;
		this.posDao = posDao;
		this.uDao = uDao;
		
		buildCache();
	}
	
	private void buildCache() {
        cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build(new CacheLoader<String, Permission>() {
                    @Override
                    public Permission load(final String queryKey) throws Exception {
                    		final String[] kv = queryKey.split("\\|");
		                	final Principal p = pDao.findByUsername(kv[0]);
		            		final Resource r = rDao.findByUri(kv[1]);
		            		
		            		if (p == null || r == null) {
		            			throw new WebApplicationException(Status.NOT_FOUND);
		            		}
		            		
		            		return new Permission(
		            				r.getUri(),
		            				p.getUsername(),
		            				((r.getLabel() & p.getLabel()) == r.getLabel())
		            				);
                    }
                });
	}

	@GET
	@UnitOfWork
	@Path("/{resource}/{principal}")
	public Response hasAccess(@Auth final User user,
			@PathParam("resource") final String uri,
			@PathParam("principal") final String principal) throws IOException, ExecutionException {
		Response response = null;
		
		try {
			final Permission result = cache.get(String.format("%s|%s", principal, uri));
			
			Audit.log(configuration.getAuditLog(),
					String.format("[%s] requested [%s] for [%s]. Access %s.\n",
					user.getUsername(),
					uri,
					principal,
					result.hasAccess() ? "granted" : "denied"));
			
			response = Response.status(Status.OK).entity(result).build();
		} catch (final Exception e) {
			if (e.getCause() instanceof WebApplicationException) {
				response = Response.status(Status.NOT_FOUND).build();
			} else throw e;
		}
		
		return response;
	}

	@GET
	@UnitOfWork
	@Path("/r")
	public List<Resource> listResources(@Auth final User user) {
		assertAdmin(user);
		
		return rDao.findAll();
	}
	
	@PUT
	@UnitOfWork
	@Path("/r/{resource}")
	public void setResourceLabel(@Auth final User user,
			final @PathParam("resource") String resource,
			final String body) {
		final String[] groups = body.trim().split(",");
		
		rDao.setLabel(resource, getLabel(groups));
	}
	
	@DELETE
	@UnitOfWork
	@Path("/r/{resource}")
	public void deleteResource(@Auth final User user,
			final @PathParam("resource") String resource) {
		pDao.delete(resource);
	}

	@GET
	@UnitOfWork
	@Path("/p")
	public List<Principal> listPrincipals(@Auth final User user) {
		assertAdmin(user);
		
		assertAdmin(user);
		
		return pDao.findAll();
	}
	
	@PUT
	@UnitOfWork
	@Path("/p/{principal}")
	public void setPrincipalLabel(@Auth final User user,
			final @PathParam("principal") String principal,
			final String body) {
		assertAdmin(user);
		
		final String[] groups = body.trim().split(",");
		
		pDao.setLabel(principal, getLabel(groups));
	}
	
	@DELETE
	@UnitOfWork
	@Path("/p/{principal}")
	public void deletePrincipal(@Auth final User user,
			final @PathParam("principal") String principal) {
		assertAdmin(user);
		
		pDao.delete(principal);
	}

	@GET
	@UnitOfWork
	@Path("/u")
	public List<User> listUsers(@Auth final User user) {
		assertAdmin(user);
		
		return uDao.findAll();
	}
	
	@PUT
	@UnitOfWork
	@Path("/u/{username}/{password}")
	public void setUserPassword(@Auth final User user,
			final @PathParam("username") String username,
			final @PathParam("password") String password) {
		assertAdmin(user);
		
		uDao.setPassword(username, BCrypt.hashpw(password, BCrypt.gensalt(12)));
	}
	
	@DELETE
	@UnitOfWork
	@Path("/u/{username}")
	public void deleteUser(@Auth final User user,
			final @PathParam("username") String username) {
		assertAdmin(user);
		
		uDao.delete(username);
	}
	
	@GET
	@UnitOfWork
	@Path("/positions")
	public List<Position> listPositions(@Auth final User user) {
		assertAdmin(user);
		
		return posDao.findAll();
	}
	
	@PUT
	@UnitOfWork
	@Path("/positions")
	public void setPositions(@Auth final User user, String body) {
		assertAdmin(user);
		
		final String[] positions = body.trim().split(",");
		
		Arrays.stream(positions).forEach(value -> {
			if (value.indexOf(":") > 0) {
				final String[] pair = value.split(":");
				final long index = Long.parseLong(pair[0].trim());
				final String name = pair[1].trim();
				
				posDao.setPosition(index, name);
			}
		});
	}
	
	private static void assertAdmin(final User user) {
		if (!user.isAdmin()) {
			throw new WebApplicationException(
					Response.status(Status.FORBIDDEN).entity("Forbidden.\n").build());
		}
	}
	
	private long getLabel(final String[] groups) {
		long label = 0;
		
		for (final String group : groups) {
			final String name = group.trim().toUpperCase();
			final Position p = posDao.findByName(name);
			
			if (p != null) {
				final long pos = p.getPosition();
			
				label = label | (1 << (pos -1));
			}
		};
		
		return label;
	}
}
