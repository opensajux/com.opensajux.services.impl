package com.opensajux.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.opensajux.entity.Portfolio;
import com.opensajux.service.PortfolioService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class PortfolioServiceImpl implements PortfolioService {
	private static final long serialVersionUID = -5484094961332737765L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	@SuppressWarnings("unchecked")
	public Portfolio getPortfolio() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Portfolio.class);
		List<Portfolio> list = (List<Portfolio>) query.execute();
		Portfolio portfolio = list.get(0);
		return portfolio;
	}

}
