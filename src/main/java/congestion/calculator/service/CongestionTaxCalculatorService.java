package congestion.calculator.service;

import congestion.calculator.DateTimeUtil;
import congestion.calculator.entity.CityEntity;
import congestion.calculator.entity.TariffEntity;
import congestion.calculator.entity.VehicleEntity;
import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxCalculatorResponse;
import congestion.calculator.model.Vehicle;
import congestion.calculator.repo.CityRepository;
import congestion.calculator.repo.VehicleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CongestionTaxCalculatorService {

    private CityRepository cityRepository;
    private VehicleRepository vehicleRepository;

    @Autowired
    public CongestionTaxCalculatorService(CityRepository cityRepository, VehicleRepository vehicleRepository) {
        this.cityRepository = cityRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public TaxCalculatorResponse getTax(Vehicle vehicle, List<Date> dates, String city)
    {
        Map<String, BigDecimal> chargerHistoryPerDay = new HashMap<>();
        CityEntity cityEntity = cityRepository.findByName(city).get();

        if(dates == null || dates.isEmpty()) return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();

        // check for tax-free vehicle
        if(isTollFreeVehicle(cityEntity.getTaxExemptVehicles(), vehicle)) return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();

        DateTimeUtil.sortDateByAsc(dates);

        // remove tax free days and holiday month
        dates.removeIf(date -> IsTollFreeDate(date, cityEntity));

        // apply single charge rule
        Map<String, List<BigDecimal>> chargesPerDay = getSingleChargeRule(dates, cityEntity);

        // calculate total tax
        BigDecimal totalFee = calculateTotalTaxBySingleChargeRule(chargerHistoryPerDay, cityEntity, chargesPerDay);

        return TaxCalculatorResponse.builder().taxAmount(totalFee).chargesHistoryByDate(chargerHistoryPerDay).build();
    }

    private BigDecimal calculateTotalTaxBySingleChargeRule(Map<String, BigDecimal> chargerHistoryPerDay, CityEntity cityEntity, Map<String, List<BigDecimal>> chargesPerDay) {
        BigDecimal totalFee = new BigDecimal(0);
        for (Map.Entry<String, List<BigDecimal>> entry : chargesPerDay.entrySet()) {
            BigDecimal totalChargePerDay = entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if(cityEntity.getCityPreferenceEntity() != null &&
                    cityEntity.getCityPreferenceEntity().getMaxTaxPerDay() != null &&
                    totalChargePerDay.compareTo(cityEntity.getCityPreferenceEntity().getMaxTaxPerDay()) == 1)
                totalChargePerDay = cityEntity.getCityPreferenceEntity().getMaxTaxPerDay();
            chargerHistoryPerDay.put(entry.getKey(), totalChargePerDay);
            totalFee = totalFee.add(totalChargePerDay);
        }
        return totalFee;
    }

    @SneakyThrows
    private Map<String, List<BigDecimal>> getSingleChargeRule(List<Date> dates, CityEntity cityEntity) {
        List<Date> visitedSlots = new ArrayList<>();
        Map<String, List<BigDecimal>> result = new HashMap<>();
        for(int start = 0; start< dates.size(); start++) {
            //skip duplicate date entries
            if(visitedSlots.contains(dates.get(start))) continue;
            // calculate first date entry which has nor previous entry so single charge rule doesn't apply
            BigDecimal charge = getTollFeeByTariffAndDate(dates.get(start), cityEntity.getTariffEntities());
            for (int end = start + 1; end < dates.size(); end++) {
                long duration  = dates.get(end).getTime() - dates.get(start).getTime();
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                if(diffInMinutes <= cityEntity.getCityPreferenceEntity().getSingleChargeIntervalInMin()) {
                    visitedSlots.add(dates.get(end));
                    BigDecimal temp = getTollFeeByTariffAndDate(dates.get(end), cityEntity.getTariffEntities());
                    if(temp.compareTo(charge) == 1) charge = temp;
                } else break;
            }
            calculateChargesByDate(dates, result, start, charge);
        }
        return result;
    }

    private void calculateChargesByDate(List<Date> dates, Map<String, List<BigDecimal>> result, int start, BigDecimal charge) {
        String dateString = DateTimeUtil.removeTime(dates.get(start));
        List<BigDecimal> chargeLists;
        if(result.containsKey(dateString)) {
            chargeLists = result.get(dateString);
        } else {
            chargeLists = new ArrayList<>();
        }
        chargeLists.add(charge);
        result.put(dateString, chargeLists);
    }

    private BigDecimal getTollFeeByTariffAndDate(Date date, Set<TariffEntity> tariffs) {
        BigDecimal totalFee = new BigDecimal(0);
        if(tariffs == null || tariffs.isEmpty()) return totalFee;

        for (TariffEntity tariffEntity : tariffs) {
            LocalTime fromTime = tariffEntity.getFromTime();
            LocalTime toTime = tariffEntity.getToTime();
            LocalTime source = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if(!source.isBefore(fromTime) && source.isBefore(toTime)) {
                return totalFee.add(tariffEntity.getCharge());
            }
        }

        return totalFee;
    }

    private boolean isTollFreeVehicle(Set<VehicleEntity> taxExemptVehicles, Vehicle vehicle) {
        if (taxExemptVehicles == null) return false;
        if(taxExemptVehicles.stream()
                .filter(taxExemptVehicle -> taxExemptVehicle.getName().equalsIgnoreCase(vehicle.getType())).count() > 0) return true;
        return false;
    }

    private Boolean IsTollFreeDate(Date date, CityEntity cityEntity)
    {
        int month = date.getMonth() + 1;
        int day = date.getDay() + 1;

        if (DateTimeUtil.isWeekend(cityEntity.getWorkingCalendarEntity(), day)) return true;
        if (DateTimeUtil.isHolidayMonth(cityEntity.getHolidayMonthsEntity(), month)) return true;
        if(DateTimeUtil.isPerOrPostOrInPublicHoliday(date, cityEntity)) return true;

        return false;
    }

    public void isValidCity(String city) throws CustomException {
        if(cityRepository.findByName(city).isEmpty()) {
            throw new CustomException("City not found in our records. Please enter a valid City!", HttpStatus.NOT_FOUND);
        }
    }

    public void isValidVehicle(Vehicle vehicle) throws CustomException {
        if(vehicleRepository.findByName(vehicle.getType()).isEmpty()) {
            throw new CustomException("Vehicle type not found in our records. Please enter a valid Vehicle!", HttpStatus.NOT_FOUND);
        }
    }
}