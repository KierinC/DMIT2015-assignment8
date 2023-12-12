package dmit2015.persistence;

import dmit2015.entity.Todo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoRepository {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager _entityManager;

    @Transactional
    public void add(@Valid Todo newTodo) {
        // If the primary key is not an identity column then write code below here to generate a new primary key value

        _entityManager.persist(newTodo);
    }

    public Optional<Todo> findById(Long todoId) {
        Optional<Todo> optionalTodo = Optional.empty();
        try {
            Todo querySingleResult = _entityManager.find(Todo.class, todoId);
            if (querySingleResult != null) {
                optionalTodo = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return optionalTodo;
    }

    public List<Todo> findAll() {
        return _entityManager.createQuery("SELECT o FROM Todo o ", Todo.class)
                .getResultList();
    }

    @Transactional
    public Todo update(Long id, @Valid Todo updatedTodo) {
        Optional<Todo> optionalTodo = findById(id);
        if (optionalTodo.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", id);
            throw new RuntimeException(errorMessage);
        }
        // The @Version field will be ignored and no OptimisticLockException thrown if you update an entity that was fetched in this methhod.
        return _entityManager.merge(updatedTodo);
    }

    @Transactional
    public void delete(Todo existingTodo) {
        // Write code to throw a RuntimeException if this entity contains child records

        if (_entityManager.contains(existingTodo)) {
            _entityManager.remove(existingTodo);
        } else {
            _entityManager.remove(_entityManager.merge(existingTodo));
        }
    }

    @Transactional
    public void deleteById(Long todoId) {
        Optional<Todo> optionalTodo = findById(todoId);
        if (optionalTodo.isPresent()) {
            Todo existingTodo = optionalTodo.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records

            _entityManager.remove(existingTodo);
        }
    }

    public long count() {
        return _entityManager.createQuery("SELECT COUNT(o) FROM Todo o", Long.class).getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        _entityManager.flush();
        _entityManager.clear();
        _entityManager.createQuery("DELETE FROM Todo").executeUpdate();
    }

}