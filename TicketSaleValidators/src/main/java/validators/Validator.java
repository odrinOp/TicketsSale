package validators;

import model.Entity;

public interface Validator<E extends Entity> {
    public void validate(E e) throws ValidatorException;
}
