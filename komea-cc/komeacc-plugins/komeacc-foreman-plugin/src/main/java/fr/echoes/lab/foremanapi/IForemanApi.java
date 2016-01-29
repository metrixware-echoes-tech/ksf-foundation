package fr.echoes.lab.foremanapi;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.echoes.lab.foremanapi.model.ComputeProfiles;
import fr.echoes.lab.foremanapi.model.ComputeResources;
import fr.echoes.lab.foremanapi.model.Environment;
import fr.echoes.lab.foremanapi.model.Environments;
import fr.echoes.lab.foremanapi.model.Filter;
import fr.echoes.lab.foremanapi.model.FilterWrapper;
import fr.echoes.lab.foremanapi.model.Filters;
import fr.echoes.lab.foremanapi.model.Host;
import fr.echoes.lab.foremanapi.model.HostGroup;
import fr.echoes.lab.foremanapi.model.HostGroupWrapper;
import fr.echoes.lab.foremanapi.model.HostPowerController;
import fr.echoes.lab.foremanapi.model.HostPuppetClasses;
import fr.echoes.lab.foremanapi.model.HostWrapper;
import fr.echoes.lab.foremanapi.model.Hostgroups;
import fr.echoes.lab.foremanapi.model.Hosts;
import fr.echoes.lab.foremanapi.model.OperatingSystems;
import fr.echoes.lab.foremanapi.model.OverrideValueWrapper;
import fr.echoes.lab.foremanapi.model.OverrideValues;
import fr.echoes.lab.foremanapi.model.Permissions;
import fr.echoes.lab.foremanapi.model.PuppetClassParameters;
import fr.echoes.lab.foremanapi.model.PuppetClasses;
import fr.echoes.lab.foremanapi.model.Role;
import fr.echoes.lab.foremanapi.model.RoleWrapper;
import fr.echoes.lab.foremanapi.model.Roles;
import fr.echoes.lab.foremanapi.model.SmartClassParameterWrapper;
import fr.echoes.lab.foremanapi.model.SmartClassParameters;
import fr.echoes.lab.foremanapi.model.SmartVariable;
import fr.echoes.lab.foremanapi.model.User;
import fr.echoes.lab.foremanapi.model.UserWrapper;
import fr.echoes.lab.foremanapi.model.Users;
import fr.echoes.lab.foremanclient.model.PowerAction;


public interface IForemanApi {

	@GET
	@Path("/api/hostgroups")
	@Produces(MediaType.APPLICATION_JSON)
	Hostgroups getHostGroups(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/hostgroups/{id}/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	Hosts getHostsByHostGroup(
			@PathParam("id") String id,
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/filters")
	@Produces(MediaType.APPLICATION_JSON)
	Filters getFilters(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@POST
	@Path("/api/filters")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Filter createFilter(
			FilterWrapper filterWrapper);

	@POST
	@Path("/api/roles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Role createRole(
			RoleWrapper roleWrapper);

	@GET
	@Path("/api/roles")
	@Produces(MediaType.APPLICATION_JSON)
	Roles getRoles(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

    @DELETE
    @Path("/api/roles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Roles deleteRoles(
            @PathParam("id") String id
    );

	@GET
	@Path("/api/roles/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Role getRole(
			@PathParam("id") String id);

	@GET
	@Path("/api/filters/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Filter getFilter(
			@PathParam("id") String id);


    @DELETE
    @Path("/api/filters/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Filter deleteFilter(
            @PathParam("id") String id);


	@POST
	@Path("/api/hostgroups")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	HostGroup createHostGroups(
			HostGroupWrapper hostGroup);

    @DELETE
    @Path("/api/hostgroups/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    HostGroup deleteHostGroups(
            @PathParam("id") String id);


	@GET
	@Path("/api/permissions")
	@Produces(MediaType.APPLICATION_JSON)
	Permissions getPermissions(
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage,
			@QueryParam("resource_type") String resourceType,
			@QueryParam("name") String name);

	@GET
	@Path("/api/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	User getUser(
			@PathParam("id") String id);

    @GET
    @Path("/api/users/")
    @Produces(MediaType.APPLICATION_JSON)
    Users getUsers(@QueryParam("per_page") String perPage);

	@PUT
	@Path("/api/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	User updateUser(
			@PathParam("id") String id,
			UserWrapper userWrapper);

	@GET
	@Path("/api/operatingsystems")
	@Produces(MediaType.APPLICATION_JSON)
	OperatingSystems getOperatingSystems(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/compute_resources")
	@Produces(MediaType.APPLICATION_JSON)
	ComputeResources getComputeResources(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/compute_profiles")
	@Produces(MediaType.APPLICATION_JSON)
	ComputeProfiles getComputeProfiles(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);


	@POST
	@Path("/api/smart_proxies/{id}/import_puppetclasses")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	void importPuppetClasses(
			@PathParam("id") String id,
			String bodyContent);

	@POST
	@Path("/api/environments/{id}/puppetclasses")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	PuppetClasses getEnvironmentPuppetClasses(
			@PathParam("id") String environmentId,
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@POST
	@Path("/api/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Host createHost(HostWrapper hostWrapper);

	@GET
	@Path("/api/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	Hosts getHosts(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/environments")
	@Produces(MediaType.APPLICATION_JSON)
	Environments getEnvironments(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/smart_class_parameters")
	@Produces(MediaType.APPLICATION_JSON)
	Environments getSmartClassParameters(
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@POST
	@Path("/api/smart_variables/{smart_variable_id}/override_values")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	SmartVariable overrideSmartVariable(
			@PathParam("smart_variable_id") String smart_variable_id,
			@PathParam("override_value") Map<String, String> override_value);


	@PUT
	@Path("/api/hosts/{id}/power")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	HostPowerController.PowerStatus hostPower(
			@PathParam("id") String id,
			PowerAction powerAction);


	@GET
	@Path("/api/environments/{environment_id}")
	@Produces(MediaType.APPLICATION_JSON)
	Environment getEnvironment(
			@PathParam("environment_id") String environment_id);

	@GET
	@Path("/api/puppetclasses/{puppet_class_id}/smart_class_parameters")
	@Produces(MediaType.APPLICATION_JSON)
	PuppetClassParameters getPuppetClassParameters(
			@PathParam("puppet_class_id") String puppet_class_id,
			@QueryParam("search") String name,
			@QueryParam("order") String order,
			@QueryParam("page") String page,
			@QueryParam("per_page") String perPage);

	@GET
	@Path("/api/hosts/{id}/puppetclasses")
	@Produces(MediaType.APPLICATION_JSON)
	HostPuppetClasses getHostPuppetClasses(@PathParam("id") String id);

	@GET
	@Path("/api/puppetclasses/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	SmartClassParameters getPuppetClasses(@PathParam("id") String id);

	@GET
	@Path("/api/smart_class_parameters/{smartClassParametesId}/override_values")
	@Produces(MediaType.APPLICATION_JSON)
	OverrideValues getOverrideValues(@PathParam("smartClassParametesId") String smartClassParametesId);

	@POST
	@Path("/api/smart_class_parameters/{smartClassParametesId}/override_values")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	OverrideValues createOverrideValues(@PathParam("smartClassParametesId") String smartClassParametesId, OverrideValueWrapper overrideValueWrapper);


	@PUT
	@Path("/api/smart_class_parameters/{smartClassParametersId}/override_values/{overrideValuesId}")
	@Consumes(MediaType.APPLICATION_JSON)
	void overrideSmartClassParameter(@PathParam("smartClassParametersId") String smartClassParametersId, @PathParam("overrideValuesId") String overrideValuesId, SmartClassParameterWrapper smartClassParameterWrapper);

}
