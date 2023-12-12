package dmit2015.restclient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.LinkedHashMap;

/**
 * The baseUri for the web AuthMpRestClient be set in either microprofile-config.properties (recommended)
 * or in this file using @RegisterRestClient(baseUri = "http://server/path").
 * <p>
 * To set the baseUri in microprofile-config.properties:
 * 1) Open src/main/resources/META-INF/microprofile-config.properties
 * 2) Add a key/value pair in the following format:
 * package-name.ClassName/mp-rest/url=baseUri
 * For example:
 * package-name:    dmit2015.restclient
 * ClassName:       StudentAuthMpRestClient
 * baseUri:         http://localhost:8080/contextName
 * The key/value pair you need to add is:
 * <code>
 * dmit2015.restclient.StudentAuthMpRestClient/mp-rest/url=http://localhost:8080/contextName
 * </code>
 * <p>
 * To use the client interface from an environment does support CDI, add @Inject and @RestClient before the field declaration such as:
 * <code>
 *
 * @Inject
 * @RestClient private StudentAuthMpRestClient _studentAuthMpRestClient;
 * </code>
 * <p>
 * To use the client interface from an environment that does not support CDI, you can use the RestClientBuilder class to programmatically build an instance as follows:
 * <code>
 * URI apiURI = new URI("http://sever/contextName");
 * StudentAuthMpRestClient _studentAuthMpRestClient = RestClientBuilder.newBuilder().baseUri(apiURi).build(StudentAuthMpRestClient.class);
 * </code>
 * To filter data with firebase you need to add an indexOf security rule
 * <code>
 * {
 * "rules": {
 * "TodoItem": {
 * "$uid": {
 * // Allow only authenticated content owners access to their data
 * ".read": "auth !== null && auth.uid === $uid",
 * ".write": "auth !== null && auth.uid === $uid",
 * ".indexOn": ["done"]
 * }
 * }
 * }
 * }
 * </code>
 */
@RequestScoped
@RegisterRestClient(baseUri = "https://dmit2015-swu-fbp-default-rtdb.firebaseio.com")
public interface StudentAuthMpRestClient {

    String DOCUMENT_URL = "/multiuser-students/{uid}";

    @POST
    @Path(DOCUMENT_URL + ".json")
    JsonObject create(@PathParam("uid") String userId, Student newStudent, @QueryParam("auth") String token);

    @GET
    @Path(DOCUMENT_URL + ".json")
    LinkedHashMap<String, Student> findAll(@PathParam("uid") String userId, @QueryParam("auth") String token);

    @GET
    @Path(DOCUMENT_URL + "/{key}.json")
    Student findById(@PathParam("uid") String userId, @PathParam("key") String key, @QueryParam("auth") String token);

    @PUT
    @Path(DOCUMENT_URL + "/{key}.json")
    Student update(@PathParam("uid") String userId, @PathParam("key") String key, Student updatedStudent, @QueryParam("auth") String token);

    @DELETE
    @Path(DOCUMENT_URL + "/{key}.json")
    void delete(@PathParam("uid") String userId, @PathParam("key") String key, @QueryParam("auth") String token);

}