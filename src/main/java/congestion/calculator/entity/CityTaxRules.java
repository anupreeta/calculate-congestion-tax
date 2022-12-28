package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_tax_rules")
public class CityTaxRules {

    @Id
    private Long id;

    @Column(name = "max_tax_per_day", nullable = false, columnDefinition="int default 60")
    private BigDecimal maxTaxPerDay;

    @Column(name = "single_charge_interval_in_min", nullable = false, columnDefinition="int default 0")
    private Integer singleChargeIntervalInMin;

    @Column(name = "number_of_tax_free_days_before_holiday", nullable = false, columnDefinition="int default 0")
    private Integer numberOfTaxFreeDaysBeforeHoliday;

    @Column(name = "number_of_tax_free_days_after_holiday", nullable = false, columnDefinition="int default 0")
    private Integer numberOfTaxFreeDaysAfterHoliday;

    @OneToOne
    @MapsId
    private CityEntity cityEntity;

}
