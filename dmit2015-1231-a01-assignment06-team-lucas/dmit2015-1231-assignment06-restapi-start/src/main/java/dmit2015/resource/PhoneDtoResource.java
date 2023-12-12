package dmit2015.resource;

import common.validator.BeanValidator;
import dmit2015.dto.PhoneDto;
import dmit2015.dto.PhoneMapper;
import dmit2015.entity.Phone;
import dmit2015.repository.PhoneRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("PhonesDto")                    // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)    // All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)    // All methods return data that has been converted to JSON format
public class PhoneDtoResource {

  @Inject
  private UriInfo uriInfo;
  @Inject
  private PhoneRepository _phoneRepository;

  @GET    // This method only accepts HTTP GET requests.
  public Response getPhones() {
    return Response.ok(_phoneRepository.findAll()
                    .stream().map(PhoneMapper.INSTANCE::toDto)
                    .collect(Collectors.toList()))
            .build();
  }

  @Path("{id}")
  @GET    // This method only accepts HTTP GET requests.
  public Response getPhone(@PathParam("id") Long id) {
    Optional<Phone> optionalPhone = _phoneRepository.findById(id);

    if (optionalPhone.isEmpty()) {
      throw new NotFoundException();
    }
    Phone existingPhone = optionalPhone.get();
    PhoneDto dto = PhoneMapper.INSTANCE.toDto(existingPhone);

    return Response.ok(dto).build();
  }

  @POST    // This method only accepts HTTP POST requests.
  public Response postPhone(@Valid PhoneDto dto) {
    if (dto == null) {
      throw new BadRequestException();
    }

    String errorMessage = BeanValidator.validateBean(dto);
    if (errorMessage != null) {
      return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }

//        TodoItem newTodoItem = mapFromDto(dto);
    Phone newPhone = PhoneMapper.INSTANCE.toEntity(dto);
    _phoneRepository.add(newPhone);

    URI phonesUri = uriInfo.getAbsolutePathBuilder().path(newPhone.getId().toString()).build();
    return Response.created(phonesUri).build();
  }
  @PUT            // This method only accepts HTTP PUT requests.
  @Path("{id}")    // This method accepts a path parameter and gives it a name of id
  public Response updatePhone(@PathParam("id") Long id, PhoneDto updatedPhoneDto) {
    if (!id.equals(updatedPhoneDto.getId())) {
      throw new BadRequestException();
    }
    Optional<Phone> optionalPhone = _phoneRepository.findById(id);
    if (optionalPhone.isEmpty()) {
      throw new NotFoundException();
    }

    String errorMessage = BeanValidator.validateBean(updatedPhoneDto);
    if (errorMessage != null) {
      return Response
              .status(Response.Status.BAD_REQUEST)
              .entity(errorMessage)
              .build();
    }

    Phone existingPhone = optionalPhone
            .orElseThrow(NotFoundException::new);
    existingPhone.setVersion(updatedPhoneDto.getVersion());
    existingPhone.setModel(updatedPhoneDto.getName());
    existingPhone.setBrand(updatedPhoneDto.getBrand());
    existingPhone.setOperatingSystem(updatedPhoneDto.getOperatingSystem());
    existingPhone.setReleaseDate(updatedPhoneDto.getDate());
    existingPhone.setPrice(updatedPhoneDto.getPrice());
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
    PhoneDto dto = PhoneMapper.INSTANCE.toDto(existingPhone);
    return Response.ok(dto).build();
  }

  @DELETE            // This method only accepts HTTP DELETE requests.
  @Path("{id}")    // This method accepts a path parameter and gives it a name of id
  public Response deletePhone(@PathParam("id") Long id) {
    Optional<Phone> optionalPhone = _phoneRepository.findById(id);

    if (optionalPhone.isEmpty()) {
      throw new NotFoundException();
    }

    _phoneRepository.deleteById(id);
// Returns an HTTP status "204 No Content" to indicated that the resource was deleted
    return Response.noContent().build();
    }


//      private PhoneDto mapToDto(Phone phone) {
//        return new PhoneDto(phone.getId(),phone.getModel(), phone.getReleaseDate(), phone.getPrice(), phone.getBrand(), phone.getOperatingSystem(), phone.getVersion());
//    }
//
//    private Phone mapFromDto(PhoneDto dto) {
//        return new Phone(dto.getId(),dto.getName(), dto.getDate(), dto.getPrice(), dto.getBrand(), dto.getOperatingSystem());
//    }
}