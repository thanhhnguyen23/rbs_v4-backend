package com.revature.dao;

import java.util.List;

/**
 * Serves as a contract for all DAO implementations to provide consistent method names
 * to common CRUD operations.
 * 
 * @author Wezley
 *
 * @param <T>
 */
public interface DAO<T> {
	
	/**
	 * Queries the DB for all of the records of type T in the DB
	 * 
	 * @return
	 * 		A list of all T records in the DB, with their password data obfuscated.
	 */
	List<T> getAll();
	
	/**
	 * Queries the DB for a record of type T with the provided id.
	 * 
	 * @param id
	 * 		The id (unique in the DB) of the sought record.
	 * 
	 * @return
	 * 		A retrieved object from the DB with an id matching the one provided. Could
	 * 		return null if no record was found with the given id.
	 */
	T getById(int id);
	
	/**
	 * Inserts a record of type T into the DB.
	 * 
	 * @param obj
	 * 		The potential new object to be added to the DB 
	 * 
	 * @return
	 * 		The newly added object with its generated primary keys.
	 */
	T add(T obj);
	
	/**
	 * Updates a record of type T in the DB.
	 * 
	 * @param updatedObj
	 * 		The updated object, with a valid id, with will be used to update the DB record.
	 * 
	 * @return
	 * 		The newly updated object if successful, or null if the record could not be updated.
	 */
	T update(T updatedObj);
	
	/**
	 * Removes a record of type T from the DB.
	 * 
	 * @param id
	 * 		The id (unique in the DB) of the targeted record.
	 * 
	 * @return
	 * 		Returns true if the record with the given id was successfully deleted. Returns
	 * 		false if the record could not be deleted.
	 */
	boolean delete(int id);
	
}
