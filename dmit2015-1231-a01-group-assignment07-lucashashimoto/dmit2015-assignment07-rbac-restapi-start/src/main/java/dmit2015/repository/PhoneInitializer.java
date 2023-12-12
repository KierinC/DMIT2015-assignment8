package dmit2015.repository;

import dmit2015.entity.Phone;
import dmit2015.entity.TodoItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class PhoneInitializer {
    private final Logger _logger = Logger.getLogger(PhoneInitializer.class.getName());

    @Inject
    private PhoneRepository _phoneRepository;


    /**
     * Using the combination of `@Observes` and `@Initialized` annotations, you can
     * intercept and perform additional processing during the phase of beans or events
     * in a CDI container.
     * <p>
     * The @Observers is used to specify this method is in observer for an event
     * The @Initialized is used to specify the method should be invoked when a bean type of `ApplicationScoped` is being
     * initialized
     * <p>
     * Execute code to create the test data for the entity.
     * This is an alternative to using a @WebListener that implements a ServletContext listener.
     * <p>
     * ]    * @param event
     */
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        _logger.info("Initializing phones");

        if (_phoneRepository.count() == 0) {

            // You could hard code the test data
            try {
                Phone phone1 = new Phone();
                phone1.setModel("OnePlus 7");
                phone1.setReleaseDate(LocalDate.parse("2020-10-10"));
                phone1.setBrand("OnePlus");
                phone1.setPrice(BigDecimal.valueOf(799.99));
                phone1.setOperatingSystem("Android 13");
                _phoneRepository.add(phone1);

                Phone phone2 = new Phone();
                phone2.setModel("Galaxy S23");
                phone2.setReleaseDate(LocalDate.parse("2022-10-10"));
                phone2.setBrand("Samsung");
                phone2.setPrice(BigDecimal.valueOf(899.99));
                phone2.setOperatingSystem("Android 14");
                _phoneRepository.add(phone2);



            } catch (Exception ex) {
                _logger.fine(ex.getMessage());
            }

            _logger.info("Created " + _phoneRepository.count() + " records.");
        }
    }
}