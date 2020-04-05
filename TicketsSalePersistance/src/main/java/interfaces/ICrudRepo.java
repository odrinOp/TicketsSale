package interfaces;

import model.Entity;
import validators.ValidatorException;

import java.util.List;

public interface ICrudRepo<ID,E extends Entity> {

    public E add(E e) throws ValidatorException;
    public E remove(E e);
    public E findOne(ID id);
    public E update(E e) throws ValidatorException;
    public List<E> findAll();
}
