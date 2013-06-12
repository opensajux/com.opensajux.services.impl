package com.opensajux.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.opensajux.entity.News;
import com.opensajux.service.NewsService;
import com.opensajux.service.PaginationParameters;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class NewsServiceImpl implements NewsService {
	private static final long serialVersionUID = -7611808114349663126L;
	private static Logger LOGGER = Logger.getLogger(NewsServiceImpl.class.getName());

	@Inject
	private transient PersistenceManagerFactory pmf;

	public void saveNews(News news) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		news.setUpdatedDate(new Date());
		pm.makePersistent(news);
		if (LOGGER.isLoggable(Level.INFO))
			LOGGER.info("Saving news item: " + news.getTitle());
	}

	public void removeNews(String id) {
		News news = getById(id);
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistent(news);
	}

	public void removeNews(Collection<News> coll) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(coll);
	}

	public void removeNews(News[] selectedNews) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(selectedNews);
	}

	@SuppressWarnings("unchecked")
	public List<News> getNews(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(News.class);

		if (params != null) {
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
			if (params.getSortField() != null)
				query.setOrdering(params.getSortField() + " " + params.getSortOrder());
		}

		Object object = query.execute();
		List<News> news = (List<News>) object;
		news = news.subList(0, news.size());
		return news;
	}

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + News.class.getName()).execute();
		return count;
	}

	public News getById(String id) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Key k = KeyFactory.createKey(News.class.getSimpleName(), Long.valueOf(id));
		News news = pm.getObjectById(News.class, k);
		return news;
	}
}
