package com.opensajux.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.opensajux.entity.SiteLink;
import com.opensajux.service.PaginationParameters;
import com.opensajux.service.SiteLinkService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class SiteLinkServiceImpl implements SiteLinkService {
	private static final long serialVersionUID = 6004592779983773367L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	@SuppressWarnings("unchecked")
	public void saveSiteLink(String title, String url) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		// check if link by the same url exists
		Query query = pm.newQuery(SiteLink.class);
		query.setFilter("content == s1");
		query.declareParameters("String s1");
		List<SiteLink> list = (List<SiteLink>) query.execute(url);
		if (list != null && list.size() > 0)
			throw new RuntimeException("Duplicate url");

		SiteLink siteLink = new SiteLink();
		siteLink.setTitle(new Text(title));
		siteLink.setContent(new Text(url));
		pm.makePersistent(siteLink);
	}

	@SuppressWarnings("unchecked")
	public void removeSiteLink(Key key) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(SiteLink.class);
		query.setFilter("key == k");
		query.declareParameters(Key.class.getName() + " k");
		List<SiteLink> list = (List<SiteLink>) query.execute(key);
		pm.deletePersistent(list.get(0));
	}

	@SuppressWarnings("unchecked")
	public List<SiteLink> getLinks(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(SiteLink.class);
		if (params != null)
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());

		List<SiteLink> siteLinks = (List<SiteLink>) query.execute();
		return siteLinks;
	}

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + SiteLink.class.getName()).execute();
		return count;
	}

	public SiteLink getById(String id) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Key k = KeyFactory.createKey(SiteLink.class.getSimpleName(), Long.valueOf(id));
		SiteLink siteLink = pm.getObjectById(SiteLink.class, k);
		return siteLink;
	}
}
