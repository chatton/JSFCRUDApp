package managers;

import java.util.List;

public interface BeanManager<T> {

	void add(T item); // adds the item to the database.
	List<T> getAll(); // returns all items of the type in the database.
	void delete(T item); // deletes the item from the database.
	
}
