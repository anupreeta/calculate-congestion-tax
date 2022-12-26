package congestion.calculator.controller;

import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxCalculatorRequest;
import congestion.calculator.model.TaxCalculatorResponse;
import congestion.calculator.model.Vehicle;
import congestion.calculator.service.CongestionTaxCalculatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TaxControllerTest {

    @InjectMocks
    private TaxController taxController;

    @Mock
    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Test
    public void shouldReturnSuccessForValidInput() throws Exception {

        TaxCalculatorRequest request = constructRequest("Car");
        TaxCalculatorResponse taxCalculatorResponse = TaxCalculatorResponse.builder().taxAmount(new BigDecimal(10)).build();
        Mockito.when(congestionTaxCalculatorService.getTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg")).thenReturn(taxCalculatorResponse);
        ResponseEntity<TaxCalculatorResponse> response = taxController.calculateCongestionTax(request, "Gothenburg");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTaxAmount()).isEqualTo(new BigDecimal(10));
    }

    @Test(expected = CustomException.class)
    public void shouldThrowErrorForInvalidVehicle() throws Exception {

        TaxCalculatorRequest request = constructRequest("Space Ship");
        Mockito.doThrow(new CustomException("Invalid Vehicle")).when(congestionTaxCalculatorService).isValidVehicle(request.getVehicle());
        taxController.calculateCongestionTax(request, "Gothenburg");
    }

    @Test(expected = CustomException.class)
    public void shouldThrowErrorForInvalidCity() throws Exception {

        TaxCalculatorRequest request = constructRequest("Car");
        Mockito.doThrow(new CustomException("Invalid City")).when(congestionTaxCalculatorService).isValidCity("Lund");
        taxController.calculateCongestionTax(request, "Lund");
    }

    private TaxCalculatorRequest constructRequest(String vehicleType) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date());
        TaxCalculatorRequest request = new TaxCalculatorRequest();
        request.setVehicle(vehicle);
        request.setCheckInTime(dateList);
        return request;
    }
}