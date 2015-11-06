/**
 *
 */
package com.tocea.corolla.utils.serviceapi;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This marker interface defines a service with read-only operations on database
 * @author sleroy
 *
 */
@Transactional(isolation=Isolation.DEFAULT, readOnly=true, propagation=Propagation.REQUIRED)
public interface IReadonlyService {

}
