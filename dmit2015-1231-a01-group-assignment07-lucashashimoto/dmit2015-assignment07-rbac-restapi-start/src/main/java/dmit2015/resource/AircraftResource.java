package dmit2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.Aircraft;
import dmit2015.repository.AircraftRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@ApplicationScoped
@Path("Aircrafts")	                // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)	// All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)	// All methods returns data that has been converted to JSON format
public class AircraftResource {

    @Inject
    private AircraftRepository _aircraftRepository;

    @GET	// This method only accepts HTTP GET requests.
    public Response listAircrafts() {
        return Response.ok(_aircraftRepository.findAll()).build();
    }

    @Path("{id}")
    @GET	// This method only accepts HTTP GET requests.
    public Response findAircraftById(@PathParam("id") Long id) {
       Aircraft existingAircraft = _aircraftRepository.findById(id).orElseThrow(NotFoundException::new);

       return Response.ok(existingAircraft).build();
    }

    @POST	// This method only accepts HTTP POST requests.
    public Response addAircraft(Aircraft newAircraft, @Context UriInfo uriInfo) {

        String errorMessage = BeanValidator.validateBean(newAircraft);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        try {
            // Persist the new Aircraft into the database
            _aircraftRepository.add(newAircraft);
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // userInfo is injected via @Context parameter to this method
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newAircraft.getId()))
                .build();

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the Aircraft was successfully persisted
        return Response
                .created(location)
                .build();
    }

    @PUT 			// This method only accepts HTTP PUT requests.
    @Path("{id}")	// This method accepts a path parameter and gives it a name of id
    public Response updateAircraft(@PathParam("id") Long id, Aircraft updatedAircraft) {
        if (!id.equals(updatedAircraft.getId())) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(updatedAircraft);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        Aircraft existingAircraft = _aircraftRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        // TODO: copy properties from the updated entity to the existing entity such as copy the version property shown below
        existingAircraft.setVersion(updatedAircraft.getVersion());

        try {
            _aircraftRepository.update(existingAircraft);
        } catch (OptimisticLockException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("The data you are trying to update has changed since your last read request.")
                    .build();
        } catch (Exception ex) {
            // Return an HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "200 OK" and include in the body of the response the object that was updated
        return Response.ok(existingAircraft).build();
    }

    @DELETE 			// This method only accepts HTTP DELETE requests.
    @Path("{id}")	// This method accepts a path parameter and gives it a name of id
    public Response delete(@PathParam("id") Long id) {

         Aircraft existingAircraft = _aircraftRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        try {
            _aircraftRepository.delete(existingAircraft);	// Removes the Aircraft from being persisted
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response
                    .serverError()
                    .encoding(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "204 No Content" to indicated that the resource was deleted
        return Response.noContent().build();
    }

}