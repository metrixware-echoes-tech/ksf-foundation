/**
 *
 */
package com.tocea.corolla.utils.serviceapi;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This marker interface defines a service with write operations on database
 *
 * @author sleroy
 *
 */
@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = false, propagation = Propagation.REQUIRED)
public interface IWriteService {
	// Marker interface
}
