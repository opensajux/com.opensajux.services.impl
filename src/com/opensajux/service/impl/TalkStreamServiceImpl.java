package com.opensajux.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.opensajux.entity.TalkStream;
import com.opensajux.service.PaginationParameters;
import com.opensajux.service.TalkStreamService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class TalkStreamServiceImpl implements TalkStreamService {
	private static final long serialVersionUID = -3854704220446440968L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + TalkStream.class.getName()).execute();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<TalkStream> getTalkStreams(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(TalkStream.class);

		if (params != null) {
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
			if (params.getSortField() != null)
				query.setOrdering(params.getSortField() + " " + params.getSortOrder());
		}

		Object object = query.execute();
		List<TalkStream> list = (List<TalkStream>) object;
		list = list.subList(0, list.size());
		return list;
	}
}
