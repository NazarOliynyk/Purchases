package oktenweb.purchases.controllers;

import oktenweb.purchases.models.CurrencyType;
import oktenweb.purchases.models.Purchase;
import oktenweb.purchases.models.ResponseTransfer;
import oktenweb.purchases.models.User;
import oktenweb.purchases.services.CountRatesService;
import oktenweb.purchases.services.PurchaseService;
import oktenweb.purchases.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
public class MainRestController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    CountRatesService countRatesService;

    @CrossOrigin(origins = "*")
    @PostMapping("/saveUser")
    public ResponseTransfer saveUser(@RequestBody User user){

        return userServiceImpl.saveUser(user);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseTransfer deleteUser(@PathVariable("id") int id) {

        return userServiceImpl.deleteById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/admin/findAllUsers")
    public List<User> findAllUsers(){
        return userServiceImpl.findAll();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/savePurchase/{id}")
    public ResponseTransfer savePurchase(@PathVariable("id") int id,
                                         @RequestBody Purchase purchase){
        return purchaseService.savePurchase(id, purchase);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getPurchases/{id}")
    public List<Purchase> getPurchases(@PathVariable("id") int id){

        return purchaseService.findAllByUserId(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/deleteByDate/{id}")
    public ResponseTransfer deleteByDate(@PathVariable("id") int id,
                                         @RequestBody ResponseTransfer responseTransfer){
        Date date = responseTransfer.getDate();
        return purchaseService.deleteAllByUserIdAndDate(id, date);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/report/{id}")
    public ResponseTransfer<java.io.Serializable> report(@PathVariable("id") int id,
                                                         @RequestBody ResponseTransfer responseTransfer){
        String year = (String) responseTransfer.getText();
        CurrencyType currency = responseTransfer.getCurrency();

        double sum = countRatesService.count(year, id, currency);
        if(sum==0){
            return new ResponseTransfer<>
                    ("No purchases of: "+year+" year to summarize");
        }else {
            return new ResponseTransfer<>(sum);
        }

    }
}
