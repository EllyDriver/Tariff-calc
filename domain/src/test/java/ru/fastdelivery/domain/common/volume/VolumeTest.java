package ru.fastdelivery.domain.common.volume;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class VolumeTest {

    @ParameterizedTest(name = "Корректный подсчет объема в кубических метрах")
    @CsvSource({"30000000000, 30", "200000, 0.0002"})
    void whenGetVolumeCubicMeters_thenCorrectAnswer(BigInteger a, BigDecimal b) {
        Volume volume = new Volume(a);
        BigDecimal volumeInCubicMeters = volume.volumeInCubicMeters();

        BigDecimal expectedVolumeInCubicMeters = b;

        assertThat(volumeInCubicMeters).isEqualByComparingTo(expectedVolumeInCubicMeters);

    }

    @ParameterizedTest(name = "Корректное добавление дополнительного объема")
    @CsvSource({"10000, 5000, 15000", "29778, 222, 30000"})
    void whenAddVolume_thenCorrectAnswer(BigInteger a, BigInteger b, BigInteger c) {
        Volume volumeInit = new Volume(a);
        Volume volumeSum = volumeInit.add(new Volume(b));

        assertThat(volumeSum).isEqualTo(new Volume(c));

    }
}