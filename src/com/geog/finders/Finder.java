package com.geog.finders;

import java.util.List;

import com.geog.model.SearchQueryOptions;

/*
 * any "Finder" implementation must take a SearchQueryOptions and return
 * a list of type T. There is only one implementation in this project however.
 */
public interface Finder<T> {
	List<T> find(final SearchQueryOptions options);
}
