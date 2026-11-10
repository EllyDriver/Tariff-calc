package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final VolumePriceProvider volumePriceProvider = mock(VolumePriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase =
            new TariffCalculateUseCase(weightPriceProvider, volumePriceProvider);

    @Test
    @DisplayName("Когда стоимость за вес выше, расчет происходит по тарифу за кг")
    void whenPricePerKgHigher_thenCalculateByWeight() {
        Price minimalPrice = new Price(BigDecimal.ONE, currency);
        Price pricePerKg = new Price(BigDecimal.valueOf(300), currency);
        Price pricePerCubicMeter = new Price(BigDecimal.valueOf(70000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);

        Shipment shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(4000)), new Volume(BigInteger.valueOf(10_000_000)))),
                new CurrencyFactory(code -> true).create("RUB"));

        Price expectedPrice = new Price(BigDecimal.valueOf(1200), currency);
        Price actualPrice = tariffCalculateUseCase.calc(shipment);

        verify(weightPriceProvider).costPerKg();
        verify(volumePriceProvider).costPerCubicMeter();

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Когда стоимость за объем выше, расчет происходит по тарифу за кубометр")
    void whenPricePerCubicMeterHigher_thenCalculateByVolume() {
        Price minimalPrice = new Price(BigDecimal.ONE, currency);
        Price pricePerKg = new Price(BigDecimal.valueOf(300), currency);
        Price pricePerCubicMeter = new Price(BigDecimal.valueOf(70000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);

        Shipment shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(4000)), new Volume(BigInteger.valueOf(20_000_000)))),
                new CurrencyFactory(code -> true).create("RUB"));

        Price expectedPrice = new Price(BigDecimal.valueOf(1400), currency);
        Price actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }
}