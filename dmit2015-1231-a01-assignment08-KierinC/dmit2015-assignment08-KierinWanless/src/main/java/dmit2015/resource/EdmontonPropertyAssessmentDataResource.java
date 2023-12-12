package dmit2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.EdmontonPropertyAssessmentData;
import dmit2015.repository.EdmontonPropertyAssessmentDataRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@ApplicationScoped
@Path("EdmontonPropertyAssessmentDatas")                    // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)    // All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)    // All methods returns data that has been converted to JSON format
public class EdmontonPropertyAssessmentDataResource {

    @Inject
    private EdmontonPropertyAssessmentDataRepository _edmontonPropertyAssessmentDataRepository;

    @GET    // This method only accepts HTTP GET requests.
    public Response listEdmontonPropertyAssessmentDatas() {
        return Response.ok(_edmontonPropertyAssessmentDataRepository.findAll()).build();
    }

    @Path("{id}")
    @GET    // This method only accepts HTTP GET requests.
    public Response findEdmontonPropertyAssessmentDataById(@PathParam("id") String accountNumber) {
        EdmontonPropertyAssessmentData existingEdmontonPropertyAssessmentData = _edmontonPropertyAssessmentDataRepository.findById(accountNumber).orElseThrow(NotFoundException::new);

        return Response.ok(existingEdmontonPropertyAssessmentData).build();
    }

    @POST    // This method only accepts HTTP POST requests.
    public Response addEdmontonPropertyAssessmentData(EdmontonPropertyAssessmentData newEdmontonPropertyAssessmentData, @Context UriInfo uriInfo) {

        String errorMessage = BeanValidator.validateBean(newEdmontonPropertyAssessmentData);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        try {
            // Persist the new EdmontonPropertyAssessmentData into the database
            _edmontonPropertyAssessmentDataRepository.add(newEdmontonPropertyAssessmentData);
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // userInfo is injected via @Context parameter to this method
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newEdmontonPropertyAssessmentData.getAccountNumber()))
                .build();

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the EdmontonPropertyAssessmentData was successfully persisted
        return Response
                .created(location)
                .build();
    }

    @PUT            // This method only accepts HTTP PUT requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response updateEdmontonPropertyAssessmentData(@PathParam("id") String id, EdmontonPropertyAssessmentData updatedEdmontonPropertyAssessmentData) {
        if (!id.equals(updatedEdmontonPropertyAssessmentData.getAccountNumber())) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(updatedEdmontonPropertyAssessmentData);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        EdmontonPropertyAssessmentData existingEdmontonPropertyAssessmentData = _edmontonPropertyAssessmentDataRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        // TODO: copy properties from the updated entity to the existing entity such as copy the version property shown below
        existingEdmontonPropertyAssessmentData.setAccountNumber(updatedEdmontonPropertyAssessmentData.getAccountNumber());
        existingEdmontonPropertyAssessmentData.setSuite(updatedEdmontonPropertyAssessmentData.getSuite());
        existingEdmontonPropertyAssessmentData.setHouseNumber(updatedEdmontonPropertyAssessmentData.getHouseNumber());
        existingEdmontonPropertyAssessmentData.setStreetName(updatedEdmontonPropertyAssessmentData.getStreetName());
        existingEdmontonPropertyAssessmentData.setGarage(updatedEdmontonPropertyAssessmentData.isGarage());
        existingEdmontonPropertyAssessmentData.setAssessedValue(updatedEdmontonPropertyAssessmentData.getAssessedValue());
        existingEdmontonPropertyAssessmentData.setNeighbourhood(updatedEdmontonPropertyAssessmentData.getNeighbourhood());
        existingEdmontonPropertyAssessmentData.setNeighbourhoodID(updatedEdmontonPropertyAssessmentData.getNeighbourhoodID());
        existingEdmontonPropertyAssessmentData.setPointLocation(updatedEdmontonPropertyAssessmentData.getPointLocation());

        try {
            _edmontonPropertyAssessmentDataRepository.update(existingEdmontonPropertyAssessmentData);
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
        return Response.ok(existingEdmontonPropertyAssessmentData).build();
    }

    @DELETE            // This method only accepts HTTP DELETE requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response delete(@PathParam("id") String id) {

        EdmontonPropertyAssessmentData existingEdmontonPropertyAssessmentData = _edmontonPropertyAssessmentDataRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        try {
            _edmontonPropertyAssessmentDataRepository.delete(existingEdmontonPropertyAssessmentData);    // Removes the EdmontonPropertyAssessmentData from being persisted
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

    @GET
    @Path("/query/within")
    public Response within(
            @QueryParam("longitude") double longitude,
            @QueryParam("latitude") double latitude,
            @QueryParam("distance") double distanceMetre
    ) {
        List<EdmontonPropertyAssessmentData> queryResultList = _edmontonPropertyAssessmentDataRepository
                .findWithinDistance(longitude, latitude, distanceMetre);
        return Response.ok(queryResultList).build();
    }

    @GET
    @Path("/query/byHouseNumberAndStreetNameAndSuite")
    public Response findByHouseNumberAndStreetName(
            @QueryParam("houseNumber") String houseNumber,
            @QueryParam("streetName") String streetName,
            @QueryParam("suite") String suite) {
        EdmontonPropertyAssessmentData querySingleResult = _edmontonPropertyAssessmentDataRepository
                .findByHouseNumberAndStreetNameAndSuite(houseNumber, streetName, suite)
                .orElseThrow(NotFoundException::new);
        return Response.ok(querySingleResult).build();
    }

    @GET
    @Path("count")
    public Response countEdmontonPropertyAssessmentData() {
        return Response.ok(_edmontonPropertyAssessmentDataRepository.count()).build();
    }

}