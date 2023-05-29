package toggle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;
import toggle.dto.Product;
import toggle.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class ToggleApplication {

    private final InventoryService service;
    private final FeatureManager manager;

    public static final Feature DISCOUNT_APPLIED = new NamedFeature("DISCOUNT_APPLIED");

    public ToggleApplication(FeatureManager manager, InventoryService service) {
        this.manager = manager;
        this.service = service;
    }


    @GetMapping("/orders")
    public List<Product> showAvailableProducts() {
        if (manager.isActive(DISCOUNT_APPLIED)) {
            return applyDiscount();
        } else {
            return service.getAllProducts();
        }
    }

    private List<Product> applyDiscount() {
        List<Product> orderListAfterDiscount = new ArrayList<>();
        service.getAllProducts().forEach(order -> {
            order.setPrice(order.getPrice() - (order.getPrice() * 5 / 100));
            orderListAfterDiscount.add(order);
        });
        return orderListAfterDiscount;
    }



    public static void main(String[] args) {
        SpringApplication.run(ToggleApplication.class, args);
    }

}
