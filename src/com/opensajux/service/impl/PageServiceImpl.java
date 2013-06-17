package com.opensajux.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.opensajux.entity.Layout;
import com.opensajux.entity.LayoutColumn;
import com.opensajux.entity.LayoutRow;
import com.opensajux.entity.Page;
import com.opensajux.service.PageService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
public class PageServiceImpl implements PageService {
	private static final long serialVersionUID = -5042080870679113971L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	/**
	 * @return the pmf
	 */
	public PersistenceManagerFactory getPmf() {
		return pmf;
	}

	/**
	 * @param pmf
	 *            the pmf to set
	 */
	public void setPmf(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPage(String friendlyUrl) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Page.class);
		query.setFilter("friendlyUrl == url");
		query.declareParameters("String url");
		Page page = (Page) ((List<Page>) query.execute(friendlyUrl)).get(0);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getTopPages() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Page.class);
		// query.setFilter("parentPage == null");
		List<Page> list = (List<Page>) query.execute();
		if (list == null)
			list = new ArrayList<>();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Page> getPages() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(Page.class);
		List<Page> list = (List<Page>) query.execute();
		if (list == null)
			list = new ArrayList<>();
		return list;
	}

	public Page addPage(String name, String title, String friendlyUrl, Page parentPage, Layout layout, int priority) {
		Date now = Calendar.getInstance().getTime();
		Page page = new Page(name, title, friendlyUrl, parentPage, layout, priority, now, now);
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		pm.makePersistent(page);
		return page;
	}

	@Override
	public Page addDefaultPage() {
		String name = "New Page";
		String friendlyUrl = createFriendlyUrl(name);
		Layout layout = createDefaultLayout();
		int priority = 0;
		return addPage(name, name, friendlyUrl, null, layout, priority);
	}

	private Layout createDefaultLayout() {
		String name = "2-column";
		Date now = Calendar.getInstance().getTime();
		Layout layout = new Layout(name, now, now);
		LayoutRow row = new LayoutRow();
		LayoutColumn col1 = new LayoutColumn();
		row.setColumns(new ArrayList<LayoutColumn>());
		row.getColumns().add(col1);
		LayoutColumn col2 = new LayoutColumn();
		row.getColumns().add(col2);
		layout.setRows(new ArrayList<LayoutRow>());
		layout.getRows().add(row);
		row = new LayoutRow();
		col1 = new LayoutColumn();
		row.setColumns(new ArrayList<LayoutColumn>());
		row.getColumns().add(col1);
		layout.getRows().add(row);
		return layout;
	}

	private String createFriendlyUrl(String name) {
		String url = name.replace(" ", "").toLowerCase();
		List<Page> pages = getPages();
		int count = 0;
		String friendlyUrl = url;
		for (int i = 0; i < pages.size(); ++i) {
			if (pages.get(i).getFriendlyUrl().equals(friendlyUrl)) {
				++count;
				friendlyUrl = url + count;
				i = 0;
			}
		}
		return friendlyUrl;
	}

}
