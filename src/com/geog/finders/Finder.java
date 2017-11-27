package com.geog.finders;

import java.util.List;

import com.geog.model.SearchQueryOptions;

public interface Finder<T> {
	List<T> find(final SearchQueryOptions options) throws DatabaseException;

}
