package fr.echoes.labs.ksf.extensions.properties;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class CustomDataDao<D, K extends Serializable> implements ICustomDataDao<D, K> {

	private final MongoTemplate	mongoTemplate;
	private final Class<K>		keyType;
	private final Class<D>		dataType;

	public CustomDataDao(final MongoTemplate _mongoTemplate, final Class<K> _keyType, final Class<D> _dataType) {
		mongoTemplate = _mongoTemplate;
		keyType = _keyType;
		dataType = _dataType;
	}

	// public CustomDataDao(final MongoTemplate _mongoTemplate, final String
	// _dataType, final Class<K> _keyType) {
	// mongoTemplate = _mongoTemplate;
	// dataTypeName = _dataType;
	// keyType = _keyType;
	// dataType = null;
	//
	// }

	@Override
	public long count() {
		return mongoTemplate.count(new Query(), dataType);
	}

	@Override
	public void delete(final D _entity) {
		mongoTemplate.remove(_entity);
	}

	@Override
	public void delete(final Iterable<? extends D> _entities) {
		mongoTemplate.remove(_entities);
	}

	@Override
	public void delete(final K _id) {
		final D findById = mongoTemplate.findById(_id, dataType);
		if (findById != null) {
			mongoTemplate.remove(findById);
		}

	}

	@Override
	public void deleteAll() {
		//
	}

	@Override
	public boolean exists(final K _id) {
		return false;
	}

	@Override
	public Iterable<D> findAll() {
		return null;
	}

	@Override
	public Iterable<D> findAll(final Iterable<K> _ids) {
		return null;
	}

	@Override
	public D findOne(final K _id) {
		return null;
	}

	@Override
	public <S extends D> Iterable<S> save(final Iterable<S> _entities) {
		//TODO::
		return null;
	}

	@Override
	public <S extends D> S save(final S _entity) {
		mongoTemplate.save(_entity);
		return _entity;
	}

}
