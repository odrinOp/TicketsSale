package validators;

import model.Sale;

public class SaleValidator implements Validator<Sale> {
    @Override
    public void validate(Sale sale) throws ValidatorException {
        String err = "";
        if(sale == null)
            throw new ValidatorException("The object is null!");

        if(sale.getName().equals(""))
            err += "You need to enter a name!\n";

        if(sale.getNo_of_seats() <= 0)
            err += "You need to reserve at least 1 seat!\n";

        if(!err.equals(""))
            throw new ValidatorException(err);
    }
}
