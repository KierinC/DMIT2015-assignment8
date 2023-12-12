package dmit2015.repository;

import common.config.ApplicationConfig;
import dmit2015.entity.Phone;
import dmit2015.repository.PhoneRepository;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)
public class PhoneRepositoryIT { // The class must be declared as public

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core:3.24.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.2.220").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:11.2.3.jre17").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:23.2.0.0").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial:6.2.3.Final").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson:3.0.3").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(Phone.class, PhoneRepository.class, AbstractJpaRepository.class, PhoneInitializer.class)
//                .addAsLibraries(pomFile.resolve("jakarta.platform:jakarta.jakartaee-api:10.0.0").withTransitivity().asFile())
                // .addClasses(ApplicationStartupListener.class)
                // .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private PhoneRepository _phoneRepository;

    @Resource
    private UserTransaction _beanManagedTransaction;

    @BeforeAll
    static void beforeAllTestMethod() {
        // code to execute before test methods are executed
    }

    @BeforeEach
    void beforeEachTestMethod() {
        // Code to execute before each method such as creating the test data
        if (_phoneRepository.count() == 0) {
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
        }
    }

    @AfterEach
    void afterEachTestMethod() {
        // code to execute after each test method such as deleteing the test data
//        _phoneRepository.deleteAll();
    }


    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "2,OnePlus 7,2020-10-10,Galaxy S23,2022-10-10"
    })
    void findAll_Size_BoundaryValues(int expectedSize,
                                     String expectedFirstRecordProperty1,
                                     LocalDate expectedFirstRecordProperty2,
                                     String expectedLastRecordProperty1,
                                     LocalDate expectedLastRecordProperty2) {
        assertThat(_phoneRepository).isNotNull();
        // Arrange and Act
        List<Phone> phoneList = _phoneRepository.findAll();
        // Assert
        assertThat(phoneList.size())
                .isEqualTo(expectedSize);

        // Get the first entity and compare with expected results
        var firstPhone = phoneList.get(0);
        assertThat(firstPhone.getModel()).isEqualTo(expectedFirstRecordProperty1);
        assertThat(firstPhone.getReleaseDate()).isEqualTo(expectedFirstRecordProperty2);

        // Get the last entity and compare with expected results
        var lastPhone = phoneList.get(phoneList.size() - 1);
        assertThat(lastPhone.getModel()).isEqualTo(expectedLastRecordProperty1);
        assertThat(lastPhone.getReleaseDate()).isEqualTo(expectedLastRecordProperty2);

    }


    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "1,OnePlus 7,2020-10-10,OnePlus,799.99,Android 13",
            "2,Galaxy S23,2022-10-10,Samsung,899.99,Android 14"
    })
    void findById_ExistingId_IsPresent(Long phoneId,
                                       String expectedProperty1,
                                       LocalDate expectedProperty2,
                                       String expectedProperty3,
                                       BigDecimal expectedProperty4,
                                       String expectedProperty5) {
        // Arrange and Act
        Optional<Phone> optionalPhone = _phoneRepository.findById(phoneId);
        assertThat(optionalPhone.isPresent())
                .isTrue();
        Phone existingPhone = optionalPhone.orElseThrow();

        // Assert
        assertThat(existingPhone)
                .isNotNull();

         assertThat(existingPhone.getModel())
             .isEqualTo(expectedProperty1);
         assertThat(existingPhone.getReleaseDate())
             .isEqualTo(expectedProperty2);
         assertThat(existingPhone.getBrand())
             .isEqualTo(expectedProperty3);
         assertThat(existingPhone.getPrice())
             .isEqualTo(expectedProperty4);
         assertThat(existingPhone.getOperatingSystem())
             .isEqualTo(expectedProperty5);


    }


    @Order(3)
    @ParameterizedTest

    @CsvSource(value = {
            "iPhone 14,2023-09-07,Apple,999.99,iOS 16",
            "iPhone 14 Pro Max,2023-09-07,Apple,1299.99,iOS 16",
    })
    void add_ValidData_Added(String property1, LocalDate property2, String property3, BigDecimal property4, String property5)  throws SystemException, NotSupportedException {
        // Arrange
        Phone newPhone = new Phone();

        newPhone.setModel(property1);
        newPhone.setReleaseDate(property2);
        newPhone.setBrand(property3);
        newPhone.setPrice(property4);
        newPhone.setOperatingSystem(property5);

        _beanManagedTransaction.begin();

        try {
            // Act
            _phoneRepository.add(newPhone);

            // Assert

             Optional<Phone> optionalPhone = _phoneRepository.findById(newPhone.getId());
             assertThat(optionalPhone.isPresent())
                 .isTrue();

        } catch (Exception ex) {
            fail("Failed to add entity with exception %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(4)
    @ParameterizedTest
    @CsvSource(value = {
            "1,OnePlus 7,2020-10-10,OnePlus,799.99,Android 12",
            "2,Galaxy S23,2022-10-10,Samsung,999.99,Android 14"
    })
    void update_ExistingId_UpdatedData(Long phoneId, String property1, LocalDate property2, String property3, BigDecimal property4, String property5) throws SystemException, NotSupportedException {
        // Arrange
        Optional<Phone> optionalPhone = _phoneRepository.findById(phoneId);
        assertThat(optionalPhone.isPresent()).isTrue();

        Phone existingPhone = optionalPhone.orElseThrow();
        assertThat(existingPhone).isNotNull();

        // Act
         existingPhone.setModel(property1);
         existingPhone.setReleaseDate(property2);
         existingPhone.setBrand(property3);
         existingPhone.setPrice(property4);
         existingPhone.setOperatingSystem(property5);

        _beanManagedTransaction.begin();

        try {
            Phone updatedPhone = _phoneRepository.update(existingPhone);

            // Assert
            assertThat(existingPhone)
                    .usingRecursiveComparison()
                     .ignoringFields("updateTime", "version", "createTime")
                    .isEqualTo(updatedPhone);
        } catch (Exception ex) {
            fail("Failed to update entity with exception %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(5)
    @ParameterizedTest
    @CsvSource(value = {
            "1",
            "2",
    })
    void deleteById_ExistingId_DeletedData(Long phoneId) throws SystemException, NotSupportedException {
        _beanManagedTransaction.begin();

        try {
            // Arrange and Act
            _phoneRepository.deleteById(phoneId);

            // Assert
            assertThat(_phoneRepository.findById(phoneId))
                    .isEmpty();

        } catch (Exception ex) {
            fail("Failed to delete entity with exception message %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(6)
    @ParameterizedTest
    @CsvSource(value = {
            "123",
            "321"
    })
    void findById_NonExistingId_IsEmpty(Long phoneId) {
        // Arrange and Act
        Optional<Phone> optionalPhone = _phoneRepository.findById(phoneId);

        // Assert
        assertThat(optionalPhone.isEmpty())
                .isTrue();

    }


    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {
            "   ,2020-10-10,OnePlus,799.99,Android 12, The Model field is required",
            "null,2022-10-10,Samsung,999.99,Android 14, The Model field is required",
    }, nullValues = {"null"})
    void create_beanValidation_throwsException(String property1, LocalDate property2, String property3, BigDecimal property4, String property5, String expectedExceptionMessage) throws SystemException, NotSupportedException {
        // Arrange
        Phone newPhone = new Phone();
        newPhone.setModel(property1);
        newPhone.setReleaseDate(property2);
        newPhone.setBrand(property3);
        newPhone.setPrice(property4);
        newPhone.setOperatingSystem(property5);


        _beanManagedTransaction.begin();
        try {
            // Act
            _phoneRepository.add(newPhone);
            fail("An bean validation constraint should have been thrown");
        } catch (Exception ex) {
            // Assert
            assertThat(ex)
                    .hasMessageContaining(expectedExceptionMessage);
        } finally {
            _beanManagedTransaction.rollback();
        }

    }

}