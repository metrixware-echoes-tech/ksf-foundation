package fr.echoes.lab.ksf.extensions.properties;

import java.io.Serializable;

/**
 * This interface allows a plugin to requires a new DAO to store its data.
 *
 * @author sleroy
 *
 */
public interface ICustomDataDAOFactory {
	/**
	 * Declares a new DAO (or obtain it). It requires to provide two
	 * informations : the type of data stored and the key type.
	 *
	 * @param _dataType
	 *            the data type
	 * @param _keyType
	 *            the key type.
	 * @return the DAO
	 */
	<D, K extends Serializable> ICustomDataDao<D, K> declareDAO(Class<D> _dataType, Class<K> _keyType);
	
	//	/**
	//	 * Declares a new DAO (or obtain it). It requires to provide two
	//	 * informations : the type of data stored and the key type.
	//	 *
	//	 * @param _dataType
	//	 *            the data type name
	//	 * @param _keyType
	//	 *            the key type.
	//	 * @return the DAO
	//	 */
	//	<K extends Serializable> ICustomDataDao<Object, K> declareDAOFromTypeName(String _dataType, Class<K> _keyType);
}
