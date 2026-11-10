package ru.fastdelivery.domain.common.volume;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Общий класс объема
 *
 * @param volumeCubicMillimeters объем в кубических миллиметрах
 */
public record Volume(BigInteger volumeCubicMillimeters) implements Comparable<Volume> {

    public Volume {
        Objects.requireNonNull(volumeCubicMillimeters, "Volume can not be null.");
        if (isLessThanZero(volumeCubicMillimeters)) {
            throw new IllegalArgumentException("Volume cannot be below zero!");
        }
    }

    public Volume(BigInteger lengthMillimeters,
                  BigInteger widthMillimeters,
                  BigInteger heightMillimeters) {
        this(compute(lengthMillimeters, widthMillimeters, heightMillimeters));
    }

    private static BigInteger compute(BigInteger lengthMillimeters,
                                  BigInteger widthMillimeters,
                                  BigInteger heightMillimeters) {
        Objects.requireNonNull(lengthMillimeters, "Length can not be null.");
        Objects.requireNonNull(widthMillimeters, "Width can not be null.");
        Objects.requireNonNull(heightMillimeters, "Height can not be null.");

        if (isLessThanZero(lengthMillimeters) || isLessThanZero(widthMillimeters) ||
                isLessThanZero(heightMillimeters)) {
            throw new IllegalArgumentException("No dimension can be below zero!");
        }
        if (isHigherThanMaxValue(lengthMillimeters) || isHigherThanMaxValue(widthMillimeters) ||
                isHigherThanMaxValue(heightMillimeters)) {
            throw new IllegalArgumentException("No dimension can be higher than 1500 mm.");
        }
        lengthMillimeters = normalize(lengthMillimeters);
        widthMillimeters = normalize(widthMillimeters);
        heightMillimeters = normalize(heightMillimeters);
        return lengthMillimeters.multiply(widthMillimeters).multiply(heightMillimeters);
    }

    private static final BigInteger MAX_DIMENSION = BigInteger.valueOf(1500);

    private static boolean isLessThanZero(BigInteger number) {
        return BigInteger.ZERO.compareTo(number) > 0;
    }
    private static boolean isHigherThanMaxValue(BigInteger number) {
        return MAX_DIMENSION.compareTo(number) < 0;
    }

    private static BigInteger normalize(BigInteger number) {
        BigInteger[] array = number.divideAndRemainder(BigInteger.valueOf(50));
        BigInteger a = array[0];
        BigInteger b = array[1];
        if (!b.equals(BigInteger.ZERO)) {
            a = a.add(BigInteger.ONE);
        }
        return a.multiply(BigInteger.valueOf(50));
    }

    public BigDecimal volumeInCubicMeters() {
        return new BigDecimal(volumeCubicMillimeters)
                .divide(BigDecimal.valueOf(1000_000_000), 4, RoundingMode.HALF_UP);
    }

    public static Volume zero() {
        return new Volume(BigInteger.ZERO);
    }

    public Volume add(Volume additionalVolume) {
        return new Volume(this.volumeCubicMillimeters
                .add(additionalVolume.volumeCubicMillimeters));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Volume vol = (Volume) o;
        return volumeCubicMillimeters.compareTo(vol.volumeCubicMillimeters) == 0;
    }

    @Override
    public int compareTo(Volume volume) {
        return volume.volumeCubicMillimeters().compareTo(volumeCubicMillimeters());
    }
}
