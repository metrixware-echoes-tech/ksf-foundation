package fr.echoes.lab.ksf.users.security.api;

import com.tocea.corolla.users.dto.UserDto;
/**
 * This interface provides methods to get informations about the authenticated user.
 * @author sleroy
 *
 */
public interface IAuthenticatedUserService extends ICurrentUserService {
	public UserDto getCurrentUser() ;
}
