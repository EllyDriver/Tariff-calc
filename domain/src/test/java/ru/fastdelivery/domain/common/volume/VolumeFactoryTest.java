package ru.fastdelivery.domain.common.volume;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VolumeFactoryTest {

    @ParameterizedTest(name = "Корректное создание объема по 1 валидному параметру")
    @CsvSource({"1500", "18000"})
    void givenValidParameter_createsVolume(BigInteger number) {
        Volume volume = new Volume(number);

        assertNotNull(volume);
        assertThat(volume.volumeCubicMillimeters()).isEqualByComparingTo(number);
    }

    @ParameterizedTest(name = "Корректное создание объема по 3 валидным параметрам с учетом нормализации")
    @CsvSource({"133, 521, 288, 24750000", "51, 52, 53, 1000000"})
    void givenValidParameters_createsVolume(BigInteger a, BigInteger b, BigInteger c, BigInteger result) {
        Volume volume = new Volume(a, b, c);

        Volume expectedVolume = new Volume(result);
        assertNotNull(volume);
        assertThat(volume.volumeCubicMillimeters()).isEqualByComparingTo(expectedVolume.volumeCubicMillimeters());
    }

    @ParameterizedTest(name = "Выбрасывается исключение при невалидном параметре создания объема")
    @CsvSource({"-100", "-10"})
    void givenInvalidParameter_throws(BigInteger number) {
        assertThatThrownBy(() -> new Volume(number))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "Выбрасывается исключение при невалидных параметрах создания объема")
    @CsvSource({"-100, 50, 50", "1000, 1500, -200"})
    void givenInvalidParameters_throws(BigInteger a, BigInteger b, BigInteger c) {
        assertThatThrownBy(() -> new Volume(a, b, c))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
