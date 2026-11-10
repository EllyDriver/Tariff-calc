package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.RoundingMode;

@Slf4j
@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        log.info("weightAllPackagesKg = {}", weightAllPackagesKg);

        var measureAllPackagesCubicMeters = shipment.measureAllPackages().volumeInCubicMeters();
        log.info("measureAllPackagesCubicMeters = {}", measureAllPackagesCubicMeters);

        var minimalPrice = weightPriceProvider.minimalPrice();

        var pricePerKg = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);
        log.info("pricePerKg.max(minimalPrice) = {}",
                pricePerKg.amount().setScale(2, RoundingMode.HALF_UP));

        var pricePerCubicMeter = volumePriceProvider
                .costPerCubicMeter()
                .multiply(measureAllPackagesCubicMeters);
        log.info("pricePerCubicMeter = {}",
                pricePerCubicMeter.amount().setScale(2, RoundingMode.HALF_UP));

        return pricePerCubicMeter.max(pricePerKg);
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}
