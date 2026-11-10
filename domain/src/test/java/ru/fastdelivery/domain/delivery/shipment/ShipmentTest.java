package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {
    private static final Volume VOLUME = new Volume(BigInteger.valueOf(1000));
    private static final Weight WEIGHT = new Weight(BigInteger.TEN);

    @Test
    @DisplayName("При расчете веса всех упаковок возвращается корректная сумма")
    void whenSummarizeWeightAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var packages = List.of(new Pack(weight1, VOLUME), new Pack(weight2, VOLUME));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    @DisplayName("При расчете объема всех упаковок возвращается корректная сумма")
    void whenMeasureVolumeAllPackage_thenReturnSum() {
        Volume vol1 = new Volume(BigInteger.valueOf(1500));
        Volume vol2 = new Volume(BigInteger.valueOf(500));

        List<Pack> packs = List.of(new Pack(WEIGHT, vol1), new Pack(WEIGHT, vol2));
        Shipment shipment = new Shipment(packs, new CurrencyFactory(code -> true).create("RUB"));

        Volume resultVolume = shipment.measureAllPackages();

        assertThat(resultVolume.volumeCubicMillimeters()).isEqualByComparingTo(BigInteger.valueOf(2000));
    }
}