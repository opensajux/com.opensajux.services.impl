package com.opensajux.service.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;

import com.opensajux.service.PlusService;

/**
 * @author Sheikh Mohammad Sajid
 * 
 */
@ApplicationScoped
public class PlusServiceImpl implements PlusService {
	private static final long serialVersionUID = 6004592779983773367L;

	@Inject
	private transient PersistenceManagerFactory pmf;

	public void saveBlog(String url) {
	}

	public void removeBlog(String id) {
	}
}
