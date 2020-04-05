package objectprotocol;

import model.Sale;

public class SaleRequest implements Request {
    private Sale sale;

    public SaleRequest(Sale sale) {
        this.sale = sale;
    }

    public Sale getSale() {
        return sale;
    }
}
