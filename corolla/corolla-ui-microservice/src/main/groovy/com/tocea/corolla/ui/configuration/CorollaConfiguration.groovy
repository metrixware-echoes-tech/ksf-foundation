/**
 *
 */
package com.tocea.corolla.ui.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Configuration of the server.
 *
 * @author sleroy
 *
 */
@Component
@ConfigurationProperties(prefix = "corolla.settings")
public class CorollaConfiguration {
	def boolean	localAddressAllowed
	

}
