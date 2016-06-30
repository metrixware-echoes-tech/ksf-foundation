package fr.echoes.labs.ksf.cc.plugin.dao;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;

public class BasicIdResolver<T> implements IdResolver<T> {

	@Override
	public Integer getID(final T pojo) {
		if (pojo == null) {
			return null;
		}
		final PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(pojo.getClass(), "id");
		if (propertyDescriptor == null) {
			throw new InvalidPojoException("Pojo should have a getter/setter to manipulate the ID");
		}
		try {
			return (Integer) propertyDescriptor.getReadMethod().invoke(pojo); // $NON-NLS-1$

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InvalidPojoException("Cannot obtain the ID", e);
		}
	}

	@Override
	public void setID(final T pojo, final Integer _id) {
		if (pojo == null) {
			return;
		}
		final PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(pojo.getClass(), "id");
		if (propertyDescriptor == null) {
			throw new InvalidPojoException("Pojo should have a getter/setter to manipulate the ID");
		}
		try {
			propertyDescriptor.getWriteMethod().invoke(pojo, _id);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InvalidPojoException("Cannot obtain the ID", e);
		}

	}

}
