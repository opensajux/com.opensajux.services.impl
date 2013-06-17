package com.opensajux.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.opensajux.entity.Weblet;
import com.opensajux.service.GenericService;
import com.opensajux.service.WebletService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class WebletServiceImpl extends GenericService<Weblet> implements WebletService, Serializable {
	private static final long serialVersionUID = -3564748695113507892L;
	private static final Logger LOGGER = Logger.getLogger(WebletServiceImpl.class.getName());

	public void setPmf(PersistenceManagerFactory pmf) {
		super.pmf = pmf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Weblet getByWebletId(String webletId) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Weblet.class);
		query.setFilter("webletId == id");
		query.declareParameters("String id");
		List<Weblet> list = (List<Weblet>) query.execute(webletId);
		Weblet weblet = null;
		if (list != null) {
			if (list.size() > 1)
				throw new RuntimeException("Multiple weblets with same id exists");
			if (list.size() == 1)
				weblet = list.get(0);
		}
		return weblet;
	}
}
