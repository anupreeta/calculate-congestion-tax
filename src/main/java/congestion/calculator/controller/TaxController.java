package congestion.calculator.controller;

import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxCalculatorRequest;
import congestion.calculator.model.TaxCalculatorResponse;
import congestion.calculator.service.CongestionTaxCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tax-calculate")
public class TaxController {

    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Autowired
    public TaxController(CongestionTaxCalculatorService congestionTaxCalculatorService) {
        this.congestionTaxCalculatorService = congestionTaxCalculatorService;
    }

    @PostMapping
    public ResponseEntity<TaxCalculatorResponse> calculateCongestionTax(@RequestBody TaxCalculatorRequest taxCalculatorRequest,
                                              @RequestHeader("city") String city) throws CustomException {
        congestionTaxCalculatorService.isValidCity(city);
        congestionTaxCalculatorService.isValidVehicle(taxCalculatorRequest.getVehicle());
        TaxCalculatorResponse result = congestionTaxCalculatorService.getTax(taxCalculatorRequest.getVehicle(), taxCalculatorRequest.getCheckInTime(), city);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}