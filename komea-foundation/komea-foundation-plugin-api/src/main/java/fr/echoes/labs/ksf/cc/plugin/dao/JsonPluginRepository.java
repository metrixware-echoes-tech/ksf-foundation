package fr.echoes.labs.ksf.cc.plugin.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

/**
 * The Class JsonPluginRepository is an implementation of DAO storing objects into JSON.
 *
 * @param <T>
 *            the generic type
 */
public class JsonPluginRepository<T> implements CrudRepository<T, Integer> {

	/**
	 * The Class DAOIndex.
	 *
	 * @param <T>
	 *            the generic type
	 */
	static class DAOIndex<T> {

		/** The data. */
		private Map<Integer, T>	data			= new ConcurrentHashMap<>();

		/** The primary index. */
		private int				primaryIndex	= 1;

		/**
		 * Count.
		 *
		 * @return the long
		 */
		public long count() {
			return this.data.size();
		}

		/**
		 * Delete.
		 *
		 * @param id
		 *            the id
		 */
		public void delete(final Integer id) {
			this.data.remove(id);

		}

		/**
		 * Delete all.
		 */
		public void deleteAll() {
			this.data.clear();

		}

		/**
		 * Exists.
		 *
		 * @param id
		 *            the id
		 * @return true, if successful
		 */
		public boolean exists(final Integer id) {
			return this.data.containsKey(id);
		}

		/**
		 * Find one.
		 *
		 * @param id
		 *            the id
		 * @return the t
		 */
		public T findOne(final Integer id) {
			return this.data.get(id);
		}

		/**
		 * Gets the data.
		 *
		 * @return the data
		 */
		public Map<Integer, T> getData() {
			return this.data;
		}

		public int save(final T entity) {
			this.data.put(this.primaryIndex, entity);
			return this.primaryIndex++;
		}

		/**
		 * Sets the data.
		 *
		 * @param data
		 *            the data
		 */
		public void setData(final Map<Integer, T> data) {
			this.data = data;
		}

		public T update(final Integer id, final T convertedEntity) {
			Validate.notNull(id);
			Validate.isTrue(this.data.containsKey(id));
			return convertedEntity;
		}

		/**
		 * Values.
		 *
		 * @return the iterable
		 */
		public Iterable<T> values() {
			return this.data.values();
		}
	}

	private final Logger					LOGGER;

	/** The plugin ID. */
	private final String					pluginID;

	/** The storage class. */
	private final Class<T>					storageClass;

	/** The configuration file. */
	private final File						configurationFile;

	/** The document. */
	private final DAOIndex<T>				document;

	/** The id resolver. */
	private final IdResolver<T>				idResolver;

	private final ReentrantReadWriteLock	lock	= new ReentrantReadWriteLock();

	private ExecutorService					executorService;

	/**
	 * Instantiates a new json plugin repository.
	 *
	 * @param _pluginID
	 *            the plugin ID
	 * @param _storageClass
	 *            the storage class
	 * @param _configurationFile
	 *            the configuration file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public JsonPluginRepository(final String _pluginID, final Class<T> _storageClass, final File _configurationFile) throws IOException {
		super();
		this.pluginID = _pluginID;
		this.storageClass = _storageClass;
		this.configurationFile = _configurationFile;
		this.LOGGER = LoggerFactory.getLogger("plugin.configuration." + FilenameUtils.getBaseName(_configurationFile.getName()));
		this.LOGGER.info("Creation of the JsonRepositoryPlugin in {}", _configurationFile);

		if (this.configurationFile.exists()) {
			this.document = this.read();
		} else {
			this.document = new DAOIndex<>();
		}
		this.idResolver = new BasicIdResolver<>();
		this.executorService = Executors.newSingleThreadExecutor();

	}

	/**
	 * Instantiates a new json plugin repository.
	 *
	 * @param _pluginID
	 *            the plugin ID
	 * @param _storageClass
	 *            the storage class
	 * @param _pluginPropertyStorage
	 *            the plugin property storage
	 */
	public JsonPluginRepository(final String _pluginID, final Class<T> _storageClass, final PluginPropertyStorage _pluginPropertyStorage) throws IOException {
		this(_pluginID, _storageClass, new File(_pluginPropertyStorage.getPluginFileStorageFolder(_pluginID), _storageClass.getName() + ".json"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#count()
	 */
	@Override
	public long count() {
		long res;
		try {
			this.lock.readLock().lock();
			res = this.document.count();
		} finally {
			this.lock.readLock().unlock();
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
	 */
	@Override
	public void delete(final Integer id) {
		try {
			this.lock.writeLock().lock();
			this.document.delete(id);
			this.asyncSave();
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(final Iterable<? extends T> entities) {
		try {
			this.lock.writeLock().lock();
			for (final T entity : entities) {
				this.document.delete(this.idResolver.getID(entity));
			}
			this.asyncSave();
		} finally {
			this.lock.writeLock().unlock();

		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(final T entity) {
		this.delete(this.idResolver.getID(entity));

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#deleteAll()
	 */
	@Override
	public void deleteAll() {
		try {
			this.lock.writeLock().lock();
			this.document.deleteAll();
			this.asyncSave();
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(final Integer id) {
		boolean found;
		try {
			this.lock.readLock().lock();
			found = this.document.exists(id);
		} finally {
			this.lock.readLock().unlock();
		}
		return found;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	@Override
	public Iterable<T> findAll() {
		Iterable<T> found;
		try {
			this.lock.readLock().lock();
			found = this.document.values();
			;
		} finally {
			this.lock.readLock().unlock();
		}
		return found;

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<T> findAll(final Iterable<Integer> ids) {
		final Set<T> entities = new HashSet<>();
		try {
			this.lock.readLock().lock();

			for (final Integer id : ids) {
				final T entity = this.document.findOne(id);
				if (entity != null) {
					entities.add(entity);
				}
			}
		} finally {
			this.lock.readLock().unlock();
		}
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)
	 */
	@Override
	public T findOne(final Integer id) {
		try {
			this.lock.readLock().lock();

			return this.document.findOne(id);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
	 */
	@Override
	public <S extends T> Iterable<S> save(final Iterable<S> _entities) {
		final List<S> entities = new ArrayList<>(30);
		try {
			this.lock.writeLock().lock();
			for (final S curEntity : _entities) {
				entities.add(this.saveInternal(curEntity));
			}
			this.asyncSave();
		} finally {
			this.lock.writeLock().unlock();
		}
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(S)
	 */
	@Override
	public <S extends T> S save(final S entity) {
		S internal = null;
		try {
			this.lock.writeLock().lock();

			internal = this.saveInternal(entity);
			this.asyncSave();

		} finally {
			this.lock.writeLock().unlock();
		}
		return internal;
	}

	/**
	 * Async save the data of the DAO.
	 */
	private void asyncSave() {
		final String name = "dao-save-" + this.pluginID + ":" + this.storageClass.getName();
		final Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				JsonPluginRepository.this.LOGGER.info("Saving configuration");
				final ObjectMapper objectMapper = new ObjectMapper();
				try {
					objectMapper.writeValue(JsonPluginRepository.this.configurationFile, DAOIndex.class);
				} catch (final IOException e) {
					JsonPluginRepository.this.LOGGER.error("Could not save the informations of the DAO " + JsonPluginRepository.this, e);
				}
			}

		}, name);
		thread.start();
		try {
			thread.join();
		} catch (final InterruptedException e) {
			this.LOGGER.error("Could not save the DAO", e);
		}
	}

	/**
	 * Read the informations of the DAO.
	 *
	 * @return the DAO index
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private DAOIndex<T> read() throws JsonParseException, JsonMappingException, IOException {
		try {
			this.lock.readLock().lock();
			final ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(this.configurationFile, DAOIndex.class);
		} catch (final IOException e) {
			throw new JsonDAOException("Could not load the DAO", e);
		} finally {
			this.lock.readLock().unlock();
		}

	}

	private <S extends T> S saveInternal(final S entity) {
		if (entity == null) {
			return null;
		}
		if (this.storageClass.isAssignableFrom(entity.getClass())) {
			final T convertedEntity = entity;
			Integer id = this.idResolver.getID(convertedEntity);
			S savedPojo;
			if (id == null) {
				id = this.document.save(convertedEntity);
				this.idResolver.setID(entity, id);

				savedPojo = entity;
			} else {
				savedPojo = (S) this.document.update(id, convertedEntity);
			}
			return savedPojo;
		} else {
			throw new InvalidPojoException("Could not convert the pojo");
		}
	}

}
