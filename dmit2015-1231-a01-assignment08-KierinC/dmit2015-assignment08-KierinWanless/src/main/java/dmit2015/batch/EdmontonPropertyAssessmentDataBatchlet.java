package dmit2015.batch;

import dmit2015.entity.EdmontonPropertyAssessmentData;
import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.runtime.context.JobContext;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Batchlets are task oriented step that is called once.
 * It either succeeds or fails. If it fails, it CAN be restarted and it runs again.
 */
@Named
@Dependent
public class EdmontonPropertyAssessmentDataBatchlet extends AbstractBatchlet {

    @PersistenceContext//(unitName = "mssql-jpa-pu")
    private EntityManager _entityManager;

    @Inject
    private JobContext _jobContext;

    private Logger _logger = Logger.getLogger(EdmontonPropertyAssessmentDataBatchlet.class.getName());

    @Inject
    @BatchProperty(name = "input_file")
    private String inputFile;

    /**
     * Perform a task and return "COMPLETED" if the job has successfully completed
     * otherwise return "FAILED" to indicate the job failed to complete.
     */
    @Transactional
    @Override
    public String process() throws Exception {
        String batchStatus = BatchStatus.COMPLETED.toString();

        try {
            HttpClient client = HttpClient.newHttpClient();
            final String downloadUriString = "https://data.edmonton.ca/api/views/q7d6-ambg/rows.csv?accessType=DOWNLOAD";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(downloadUriString))
                    .build();
            Path downloadPath = Path.of("/home/user2015/Downloads");	// On a Windows double back-slash such as C:\\DMIT2015\\Downloads
            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFileDownload(downloadPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
            System.out.println("Status code: " + response.statusCode());
            System.out.println("\n Body: " + response.body());

        } catch (Exception ex) {
            batchStatus = BatchStatus.FAILED.toString();
            ex.printStackTrace();
            _logger.fine("Batchlet failed with exception: " + ex.getMessage());
        }

        return batchStatus;
    }
}