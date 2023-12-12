package dmit2015.repository;

import dmit2015.entity.Aircraft;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class AircraftInitializer {
    private final Logger _logger = Logger.getLogger(AircraftInitializer.class.getName());

    @Inject
    private AircraftRepository _aircraftRepository;


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
        _logger.info("Initializing aircrafts");

        if (_aircraftRepository.count() == 0) {

            // You could hard code the test data
            try {
                // TODO: Create a new entity instance
                // TODO: Set the properties of the entity instance
                // TODO: Add the entity instance to the JPA repository
                Aircraft aircraft1 = new Aircraft();
                aircraft1.setModel("A-10 Thunderbolt II");
                aircraft1.setManufacturer("Fairchild Republic");
                aircraft1.setTailNumber("C459M");
                _aircraftRepository.add(aircraft1);

                Aircraft aircraft2 = new Aircraft();
                aircraft2.setModel("F-18 Hornet");
                aircraft2.setManufacturer("McDonnell Douglas");
                aircraft2.setTailNumber("C599A");
                _aircraftRepository.add(aircraft2);

                Aircraft aircraft3 = new Aircraft();
                aircraft3.setModel("SR-71 Blackbird");
                aircraft3.setManufacturer("Lockheed");
                aircraft3.setTailNumber("C896S");
                _aircraftRepository.add(aircraft3);


            } catch (Exception ex) {
                _logger.fine(ex.getMessage());
            }

            _logger.info("Created " + _aircraftRepository.count() + " records.");
        }
    }
}
