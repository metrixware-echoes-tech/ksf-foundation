package fr.echoes.labs.ksf.cc.plugin.dao;

/**
 * The Interface IdResolver helps to retrieves the ID of a pojo.
 *
 * @param <T>
 *            the generic type
 */
public interface IdResolver<T> {

	/**
	 * Gets the id.
	 *
	 * @param pojo
	 *            the pojo
	 * @return the id
	 */
	public Integer getID(T pojo);

	/**
	 * Sets the id.
	 *
	 * @param pojo
	 *            the pojo
	 * @param _id
	 *            the new id
	 */
	public void setID(T pojo, Integer _id);
}
