package congestion.calculator.service;

import congestion.calculator.entity.*;
import congestion.calculator.model.TaxCalculatorRequest;
import congestion.calculator.model.TaxCalculatorResponse;
import congestion.calculator.model.Vehicle;
import congestion.calculator.repo.CityRepository;
import congestion.calculator.repo.VehicleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CongestionTaxCalculatorServiceTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Test
    public void when_get_tax_called_with_empty_input_then_return_zero_result() throws ParseException {
        TaxCalculatorRequest request = constructRequest("Car");
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getEmptyVehicleResponse());
        TaxCalculatorResponse result = congestionTaxCalculatorService.getTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTaxAmount()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void when_get_tax_called_with_valid_input_then_return_result() throws ParseException {
        TaxCalculatorRequest request = constructRequest("Car");
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getVehicleResponse());
        TaxCalculatorResponse result = congestionTaxCalculatorService.getTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTaxAmount()).isEqualTo(new BigDecimal(8));
    }

    private TaxCalculatorRequest constructRequest(String vehicleType) throws ParseException {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        List<Date> dateList = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = formatter.parse("2013-01-14 06:01:00");
        dateList.add(dateTime);
        TaxCalculatorRequest request = new TaxCalculatorRequest();
        request.setCheckInTime(dateList);
        request.setVehicle(vehicle);
        return request;
    }

    private Optional<CityEntity> getEmptyVehicleResponse() {
        CityEntity cityEntity = CityEntity.builder().name("Gothenburg").id(1L).build();
        return Optional.of(cityEntity);
    }

    private Optional<CityEntity> getVehicleResponse() {
        CityEntity cityEntity = CityEntity.builder().name("Gothenburg").id(1L).build();

        WorkingCalendarEntity workingCalendarEntity = WorkingCalendarEntity.builder()
                .isSunday(false).isSaturday(false)
                .isMonday(true).isTuesday(true).isWednesday(true).isThursday(true).isFriday(true).build();
        cityEntity.setWorkingCalendarEntity(workingCalendarEntity);

        HolidayMonthsEntity holidayMonthsEntity = HolidayMonthsEntity.builder()
                .isJanuary(false).isFebruary(false).isMarch(false).isApril(false).isMay(false).isJune(false)
                .isJuly(true).isAugust(false).isSeptember(false).isOctober(false).isNovember(false).isDecember(false).build();
        cityEntity.setHolidayMonthsEntity(holidayMonthsEntity);

        Set<TariffEntity> tariffEntities = new HashSet<>();
        TariffEntity tariffEntity1 = TariffEntity.builder()
                .cityEntity(cityEntity)
                .fromTime(LocalTime.parse("06:00"))
                .toTime(LocalTime.parse("06:29:59"))
                .charge(BigDecimal.valueOf(8))
                .build();
        tariffEntities.add(tariffEntity1);

        cityEntity.setTariffEntities(tariffEntities);

        CityPreferenceEntity cityPreferenceEntity = CityPreferenceEntity.builder()
                .numberOfTaxFreeDaysBeforeHoliday(1)
                .numberOfTaxFreeDaysAfterHoliday(1)
                .maxTaxPerDay(new BigDecimal(60))
                .singleChargeIntervalInMin(60)
                .build();
        cityEntity.setCityPreferenceEntity(cityPreferenceEntity);

        return Optional.of(cityEntity);
    }
}