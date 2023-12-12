package dmit2015.persistence;

import common.config.ApplicationConfig;
import dmit2015.entity.Todo;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)
public class TodoArquillianIT { // The class must be declared as public

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
                .addClasses(Todo.class, TodoRepository.class)
                // TODO Add any additional libraries, packages, classes or resource files required
//                .addAsLibraries(pomFile.resolve("jakarta.platform:jakarta.jakartaee-api:10.0.0").withTransitivity().asFile())
                // .addClasses(ApplicationStartupListener.class)
                // .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private TodoRepository _todoRepository;

    @Resource
    private UserTransaction _beanManagedTransaction;

    @BeforeAll
    static void beforeAllTestMethod() {
        // code to execute before test methods are executed
    }

    @BeforeEach
    void beforeEachTestMethod() {
        // Code to execute before each method such as creating the test data
        if (_todoRepository.count() == 0) {
            Todo todo1 = new Todo();
            todo1.setTask("Task 1");
            _todoRepository.add(todo1);

            Todo todo2 = new Todo();
            todo2.setTask("Task 2");
            todo2.setDone(false);
            _todoRepository.add(todo2);

            Todo todo3 = new Todo();
            todo3.setTask("Task 3");
            todo3.setDone(true);
            _todoRepository.add(todo3);
        }
    }

    @AfterEach
    void afterEachTestMethod() {
        // code to execute after each test method such as deleting the test data
//        _todoRepository.deleteAll();
    }


    @Order(1)
    @ParameterizedTest
    // TODO Changed the expected results
    @CsvSource(value = {
            "3,Task 1,false,Task 3,true"
    })
    void findAll_Size_BoundaryValues(
            int expectedSize,
            String expectedFirstTask,
            boolean expectedFirstDone,
            String expectedLastTask,
            boolean expectedLastDone) {
        assertThat(_todoRepository).isNotNull();
        // Arrange and Act
        List<Todo> todoList = _todoRepository.findAll();
        // Assert
        assertThat(todoList.size())
                .isEqualTo(expectedSize);

        // Get the first entity and compare with expected results
        var firstTodo = todoList.get(0);
        // TODO Assert that each property match expected results for the first record
        assertThat(firstTodo.getTask()).isEqualTo(expectedFirstTask);
        assertThat(firstTodo.isDone()).isEqualTo(expectedFirstDone);

        // Get the last entity and compare with expected results
        var lastTodo = todoList.get(todoList.size() - 1);
        // TODO Assert that each property match expected results for the last record
        assertThat(lastTodo.getTask()).isEqualTo(expectedLastTask);
        assertThat(lastTodo.isDone()).isEqualTo(expectedLastDone);

    }


    @Order(2)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "2,Task 2,false",
            "3,Task 3,true"
    })
    void findById_ExistingId_IsPresent(Long todoId, String expectedTask, boolean expectedDone) {
        // Arrange and Act
        Optional<Todo> optionalTodo = _todoRepository.findById(todoId);
        assertThat(optionalTodo.isPresent())
                .isTrue();
        Todo existingTodo = optionalTodo.orElseThrow();

        // Assert
        assertThat(existingTodo)
                .isNotNull();
        // TODO Assert that each property matches the expected result
        assertThat(existingTodo.getTask())
             .isEqualTo(expectedTask);
        assertThat(existingTodo.isDone())
            .isEqualTo(expectedDone);

    }


    @Order(3)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "Buy Large Coffee, true",
            "Drink Coffee, false",
            "Throw Coffee cup away, false",
    })
    void add_ValidData_Added(String task, boolean done) throws SystemException, NotSupportedException {
        // Arrange
        Todo newTodo = new Todo();
        newTodo.setTask(task);
        newTodo.setDone(done);

        _beanManagedTransaction.begin();

        try {
            // Act
            _todoRepository.add(newTodo);

            // Assert
            Optional<Todo> optionalTodo = _todoRepository.findById(newTodo.getId());
            assertThat(optionalTodo.isPresent())
                 .isTrue();

        } catch (Exception ex) {
            fail("Failed to add entity with exception %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(4)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "1, Buy Venti size Coffee from Starbucks,true",
            "2, Drink Coffee,true",
    })
    void update_ExistingId_UpdatedData(Long todoId, String task, boolean done) throws SystemException, NotSupportedException {
        // Arrange
        Optional<Todo> optionalTodo = _todoRepository.findById(todoId);
        assertThat(optionalTodo.isPresent()).isTrue();

        Todo existingTodo = optionalTodo.orElseThrow();
        assertThat(existingTodo).isNotNull();

        // Act
        // TODO Uncomment code below and assign new value to each property
        existingTodo.setTask(task);
        existingTodo.setDone(done);

        _beanManagedTransaction.begin();

        try {
            Todo updatedTodo = _todoRepository.update(todoId, existingTodo);

            // Assert
            assertThat(existingTodo)
                    .usingRecursiveComparison()
                    .ignoringFields("updateTime", "version")
                    .isEqualTo(updatedTodo);
        } catch (Exception ex) {
            fail("Failed to update entity with exception %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(5)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "1",
            "3",
    })
    void deleteById_ExistingId_DeletedData(Long todoId) throws SystemException, NotSupportedException {
        _beanManagedTransaction.begin();

        try {
            // Arrange and Act
            _todoRepository.deleteById(todoId);

            // Assert
            assertThat(_todoRepository.findById(todoId))
                    .isEmpty();

        } catch (Exception ex) {
            fail("Failed to delete entity with exception message %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(6)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "123",
            "321"
    })
    void findById_NonExistingId_IsEmpty(Long todoId) {
        // Arrange and Act
        Optional<Todo> optionalTodo = _todoRepository.findById(todoId);

        // Assert
        assertThat(optionalTodo.isEmpty())
                .isTrue();

    }


    @Order(7)
    @ParameterizedTest
    // TODO Change the value below
    @CsvSource(value = {
            "   ,true,Task cannot be blank",
            "null,false,Task cannot be blank",
    }, nullValues = {"null"})
    void create_beanValidation_throwsException(String task, boolean done, String expectedExceptionMessage) throws SystemException, NotSupportedException {
        // Arrange
        Todo newTodo = new Todo();
        // TODO Change the code below to set each property
        newTodo.setTask(task);
        newTodo.setDone(done);

        _beanManagedTransaction.begin();
        try {
            // Act
            _todoRepository.add(newTodo);
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