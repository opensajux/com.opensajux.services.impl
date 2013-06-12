package com.opensajux.service.impl;

import java.util.ArrayList;
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
import com.opensajux.entity.MenuItem;
import com.opensajux.service.MenuItemService;
import com.opensajux.service.MenuService;
import com.opensajux.service.PaginationParameters;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class MenuItemServiceImpl implements MenuItemService {
	private static final long serialVersionUID = -1855982945173023120L;
	private static Logger LOGGER = Logger.getLogger(MenuItemServiceImpl.class.getName());

	@Inject
	private transient PersistenceManagerFactory pmf;
	@Inject
	private MenuService menuService;

	public void saveMenuItem(MenuItem menuItem, String menuName) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Menu menu = menuService.getMenuByName(menuName);
		menuItem.setMenu(menu);
		pm.makePersistent(menuItem);
		if (menu.getMenuItems() != null)
			menu.getMenuItems().add(menuItem);
		else {
			menu.setMenuItems(new ArrayList<MenuItem>());
			menu.getMenuItems().add(menuItem);
		}
		pm.makePersistent(menu);
		if (LOGGER.isLoggable(Level.INFO))
			LOGGER.info("Saving menu item: " + menuItem.getTitle());
	}

	public void removeMenuItem(String id) {
		MenuItem menuItem = getById(id);
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistent(menuItem);
	}

	public void removeMenuItem(Collection<MenuItem> coll) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(coll);
	}

	public void removeMenuItem(MenuItem[] selectedMenuItems) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.deletePersistentAll(selectedMenuItems);
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> getMenuItems(PaginationParameters params, String menuName) {
		List<MenuItem> menuItems = null;
		Menu menu = menuService.getMenuByName(menuName);
		if (menu != null) {
			PersistenceManager pm = pmf.getPersistenceManagerProxy();
			Query query = pm.newQuery(MenuItem.class);
			query.setFilter("menu == m");
			query.declareParameters(Menu.class.getName() + " m");

			if (params != null) {
				query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
				if (params.getSortField() != null)
					query.setOrdering(params.getSortField() + " " + params.getSortOrder());
			}

			Object object = query.execute(menu);
			menuItems = (List<MenuItem>) object;
			menuItems = menuItems.subList(0, menuItems.size());
		}
		return menuItems;
	}

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + MenuItem.class.getName()).execute();
		return count;
	}

	public MenuItem getById(String id) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Key k = KeyFactory.createKey(MenuItem.class.getSimpleName(), Long.valueOf(id));
		MenuItem menuItem = pm.getObjectById(MenuItem.class, k);
		return menuItem;
	}
}
