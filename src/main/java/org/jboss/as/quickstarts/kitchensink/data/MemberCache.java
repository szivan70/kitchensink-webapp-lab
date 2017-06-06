package org.jboss.as.quickstarts.kitchensink.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.as.quickstarts.kitchensink.model.Member;

@ApplicationScoped
public class MemberCache {

	private final static String MEMBERS = "members";

	private final static String HOTROD_SERVICE = "HOTROD_SERVICE";
	private final static String HOTROD_SERVICE_PORT = "HOTROD_SERVICE_PORT";

	private RemoteCacheManager cacheManager;
	private RemoteCache<String,List<Member>> cache;

	private ConfigurationBuilder configureBuilder() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder
			.addServer()
				.host(System.getenv(HOTROD_SERVICE))
				.port(Integer.parseInt(System.getenv(HOTROD_SERVICE_PORT)));
		return builder;
	}

	@PostConstruct
	public void init() {
		cacheManager = new RemoteCacheManager(configureBuilder().build());
		cache = cacheManager.getCache("default");
	}

	private RemoteCache<String,List<Member>> getCache() {
		return cache;
	}

    public List<Member> loadFromCache() {
        return getCache().get(MEMBERS);
    }

    public void loadIntoCache(List<Member> members) {
        getCache().put(MEMBERS, members);
    }

    public void invalidateCache() {
        getCache().clear();
    }
}
