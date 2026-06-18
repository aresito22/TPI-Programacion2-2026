package dao;

import java.util.List;
import java.util.Optional;

public interface IBaseDAO<T> {
    void save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void update(T entity);
    void delete(Long id);
}
