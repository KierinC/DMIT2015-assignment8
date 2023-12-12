package dmit2015.repository;

import dmit2015.entity.TodoItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class TodoItemRepository extends AbstractJpaRepository<TodoItem, Long> {
    public TodoItemRepository() {
        super(TodoItem.class);
    }

    public List<TodoItem> findAllByUsername(String username) {
        return getEntityManager().createQuery("select o from TodoItem o where o.username = :usernameValue", TodoItem.class)
                .setParameter("usernameValue", username)
                .getResultList();
    }
}