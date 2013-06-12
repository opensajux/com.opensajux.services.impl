package com.opensajux.service.impl;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.opensajux.entity.BlogPost;
import com.opensajux.entity.MyBlog;
import com.opensajux.entity.SocialSource;
import com.opensajux.service.BlogService;
import com.opensajux.service.PaginationParameters;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class BlogServiceImpl implements BlogService {
	private static final long serialVersionUID = 6004592779983773367L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	public Long getCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + MyBlog.class.getName()).execute();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<MyBlog> getBlogs(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(MyBlog.class);

		if (params != null) {
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
			if (params.getSortField() != null)
				query.setOrdering(params.getSortField() + " " + params.getSortOrder());
		}

		Object object = query.execute();
		List<MyBlog> blogList = (List<MyBlog>) object;
		blogList = blogList.subList(0, blogList.size());
		return blogList;
	}

	public MyBlog getById(String id) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Key k = KeyFactory.createKey(MyBlog.class.getSimpleName(), Long.valueOf(id));
		MyBlog blog = pm.getObjectById(MyBlog.class, k);
		return blog;
	}

	public void saveBlog(String url, String id, String blogName, Date publishedDate, Date updatedDate) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		// check if blog by the same url exists
		Query query = pm.newQuery(MyBlog.class);
		query.setFilter("name == s1");
		query.declareParameters("String s1");
		List<MyBlog> list = (List<MyBlog>) query.execute(url);
		if (list != null && list.size() > 0)
			throw new RuntimeException("Duplicate url");

		MyBlog b = new MyBlog();
		b.setId(id);
		b.setName(new Text(blogName));
		b.setPublishDate(publishedDate);
		b.setSource(SocialSource.BLOGGER);
		b.setUpdatedDate(updatedDate);
		b.setUrl(new Text(url));
		pm.makePersistent(b);
	}

	public void removeBlog(String id) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Query query = pm.newQuery(MyBlog.class);
		query.setFilter("id == id1");
		query.declareParameters("String id1");
		List<MyBlog> list = (List<MyBlog>) query.execute(id);
		pm.deletePersistent(list.get(0));
	}

	public Long getBlogPostCount() {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Long count = (Long) pm.newQuery("select count(key) from " + BlogPost.class.getName()).execute();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<BlogPost> getBlogPosts(PaginationParameters params) {
		PersistenceManager pm = pmf.getPersistenceManagerProxy();
		Query query = pm.newQuery(BlogPost.class);

		if (params != null) {
			query.setRange(params.getFirst(), params.getFirst() + params.getPageSize());
			if (params.getSortField() != null)
				query.setOrdering(params.getSortField() + " " + params.getSortOrder());
		}

		Object object = query.execute();
		List<BlogPost> blogPostList = (List<BlogPost>) object;
		blogPostList = blogPostList.subList(0, blogPostList.size());
		return blogPostList;
	}
}
