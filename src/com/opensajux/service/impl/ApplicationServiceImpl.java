package com.opensajux.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.opensajux.entity.SiteInfo;
import com.opensajux.service.ApplicationService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class ApplicationServiceImpl implements ApplicationService {
	private static final long serialVersionUID = 6326369951773532396L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	@SuppressWarnings("unchecked")
	public SiteInfo getSiteInfo() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery("select from " + SiteInfo.class.getName());
		List<SiteInfo> list = (List<SiteInfo>) query.execute();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public void saveSiteInfo(SiteInfo siteInfo) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.makePersistent(siteInfo);
	}
}
