package fr.echoes.labs.ksf.extensions.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomDataDaoFactoryService implements ICustomDataDAOFactory {
	
	private final MongoTemplate mongoTemplate;
	
	// TODO:: To be replaced by a mean later
	private final Map<String, ICustomDataDao> daos = new HashMap<>();
	
	@Autowired
	public CustomDataDaoFactoryService(final MongoTemplate _mongoTemplate) {
		super();
		mongoTemplate = _mongoTemplate;
		
	}
	
	@Override
	public synchronized <D, K extends Serializable> ICustomDataDao<D, K> declareDAO(final Class<D> _dataType,
			final Class<K> _keyType) {
		Validate.notNull(_keyType);
		Validate.notNull(_keyType);
		ICustomDataDao<D, K> res = daos.get(_dataType.getName());
		if (res == null) {
			res = new CustomDataDao(mongoTemplate, _dataType, _keyType);
			daos.put(_dataType.getName(), res);
		}
		return res;
	}
	//
	//	@Override
	//	public synchronized <K extends Serializable> ICustomDataDao<Object, K> declareDAOFromTypeName(final String _dataType,
	//			final Class<K> _keyType) {
	//		Validate.notNull(_keyType);
	//		Validate.notNull(_keyType);
	//		ICustomDataDao<Object, K> res = daos.get(_dataType);
	//		if (res == null) {
	//			res = new CustomDataDao(mongoTemplate, _dataType, _keyType);
	//			daos.put(_dataType, res);
	//		}
	//		return res;
	//	}
	
	
}
