package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy="cityEntity")
    private Set<HolidayCalendarEntity> holidayCalendarEntities;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private WorkingCalendarEntity workingCalendarEntity;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private HolidayMonthsEntity holidayMonthsEntity;

    @OneToMany(mappedBy="cityEntity")
    private Set<TariffEntity> tariffEntities;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "city_vehicle",
            joinColumns = @JoinColumn(name = "city_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<VehicleEntity> taxExemptVehicles;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private CityPreferenceEntity cityPreferenceEntity;
}
