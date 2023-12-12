package dmit2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.Phone;
import dmit2015.repository.PhoneRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.jwt.Claims;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@Path("Phones")                    // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)    // All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)    // All methods returns data that has been converted to JSON format
public class PhoneResource {
    @Inject
    @Claim(standard = Claims.sub)   // The unique identifier for the user.
    private ClaimValue<String> subject;

    @Inject
    @Claim(standard = Claims.upn)   // The username for the user.
    private ClaimValue<Optional<String>> optionalUsername;
    @Inject
    @Claim(standard = Claims.groups)    // The roles that the subject is a member of.
    private ClaimValue<Set<String>> optionalGroups;

    @Inject
    private UriInfo uriInfo;

    @Inject
    private PhoneRepository _phoneRepository;

    @Path("all")
    @GET    // This method only accepts HTTP GET requests.
    public Response listAllPhones() {
        return Response.ok(_phoneRepository.findAll()).build();
    }
    
    @RolesAllowed("**")
    @GET    // GET: /restapi/Phones
    public Response getPhones() {
        String username = optionalUsername.getValue().orElseThrow();
        return Response.ok(_phoneRepository.findAllByUsername(username)).build();
    }
    @RolesAllowed("**")
    @Path("{id}")
    @GET    // This method only accepts HTTP GET requests.
    public Response findPhoneById(@PathParam("id") Long Id) {
        Phone existingPhone = _phoneRepository.findById(Id).orElseThrow(NotFoundException::new);
        String username = optionalUsername.getValue().orElseThrow();
        if (!existingPhone.getUsername().equalsIgnoreCase(username)) {
            final String message = "Access denied. You do not have permission to get data owned by another user.";
            throw new NotAuthorizedException(message);
        }
        return Response.ok(existingPhone).build();
    }

    @POST    // This method only accepts HTTP POST requests.
    public Response addPhone(Phone newPhone) {
        if (newPhone == null) {
            throw new BadRequestException();
        }
        String errorMessage = BeanValidator.validateBean(newPhone);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }
        String username = optionalUsername.getValue().orElseThrow();
        newPhone.setUsername(username);
        try {
            // Persist the new Phone into the database
            _phoneRepository.add(newPhone);
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // userInfo is injected via @Context parameter to this method

        URI phonesUri = uriInfo.getAbsolutePathBuilder().path(newPhone.getId().toString()).build();

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the Phone was successfully persisted
        return Response
                .created(phonesUri)
                .build();
    }

    @RolesAllowed("**")
    @PUT            // This method only accepts HTTP PUT requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response updatePhone(@PathParam("id") Long id, Phone updatedPhone) {
        if (!id.equals(updatedPhone.getId())) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(updatedPhone);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }
        
        Phone existingPhone = _phoneRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        String username = optionalUsername.getValue().orElseThrow();
        if (!existingPhone.getUsername().equalsIgnoreCase(username)) {
            final String message = "Access denied. You do not have permission to update data owned by another user.";
            throw new NotAuthorizedException(message);
        }

        existingPhone.setVersion(updatedPhone.getVersion());
        existingPhone.setModel(updatedPhone.getModel());
        existingPhone.setBrand(updatedPhone.getBrand());
        existingPhone.setOperatingSystem(updatedPhone.getOperatingSystem());
        existingPhone.setReleaseDate(updatedPhone.getReleaseDate());
        existingPhone.setPrice(updatedPhone.getPrice());

        try {
            _phoneRepository.update(existingPhone);
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
        return Response.ok(existingPhone).build();
    }

    @RolesAllowed("**")
    @DELETE            // This method only accepts HTTP DELETE requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response delete(@PathParam("id") Long id) {

        Phone existingPhone = _phoneRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        
        String username = optionalUsername.getValue().orElseThrow();
        if (!existingPhone.getUsername().equalsIgnoreCase(username)) {
            final String message = "Access denied. You do not have permission to get delete owned by another user.";
            throw new NotAuthorizedException(message);
        }
        try {
            _phoneRepository.delete(existingPhone);    // Removes the Phone from being persisted
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