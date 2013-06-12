package com.opensajux.service.impl;

import java.util.Collection;
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
import com.opensajux.entity.Menu;
import com.opensajux.service.MenuService;
import com.opensajux.service.PaginationParameters;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class MenuServiceImpl implements MenuService {
	private static final long serialVersionUID = -3564748695113507892L;
	private static final Logger LOGGER = Logger.getLogger(MenuServiceImpl.class.getName());

	@Inject
	private transient PersistenceManagerFactory pmf;

	public void saveMenu(Menu menu) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.makePersistent(menu);
		if (LOGGER.isLoggable(Level.INFO))
			LOGGER.info("Saving menu: " + menu.getTitle());
	}

	public void removeMenu(String id) {
		Menu menu = getById(id);
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistent(menu);
	}

	public void removeMenu(Collection<Menu> coll) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(coll);
	}

	public void removeMenu(Menu[] selectedMenus) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(selectedMenus);
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getMenus(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Menu.class);

		if (params != null) {
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
			if (params.getSortField() != null)
				query.setOrdering(params.getSortField() + " " + params.getSortOrder());
		}

		Object object = query.execute();
		List<Menu> menuList = (List<Menu>) object;
		menuList = menuList.subList(0, menuList.size());
		return menuList;
	}

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + Menu.class.getName()).execute();
		return count;
	}

	public Menu getById(String id) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Key k = KeyFactory.createKey(Menu.class.getSimpleName(), Long.valueOf(id));
		Menu menu = pm.getObjectById(Menu.class, k);
		return menu;
	}

	@SuppressWarnings("unchecked")
	public Menu getMenuByName(String name) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery("select from " + Menu.class.getName() + " where name == n && isPublished == true");

		query.declareParameters("String n");
		List<Menu> m = (List<Menu>) query.execute(name);
		return m != null && m.size() > 0 ? m.get(0) : null;
	}
}
