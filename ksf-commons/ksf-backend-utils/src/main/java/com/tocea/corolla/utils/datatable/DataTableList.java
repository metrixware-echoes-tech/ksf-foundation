/**
 *
 */
package com.tocea.corolla.utils.datatable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sleroy
 *
 */
public class DataTableList<T> {
	private final List<T>	data	= new ArrayList<>();

	public void add(final T _item) {
		this.data.add(_item);
	}

	public void addAll(final List<T> _dataList) {
		this.data.addAll(_dataList);
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return this.data;
	}
}
