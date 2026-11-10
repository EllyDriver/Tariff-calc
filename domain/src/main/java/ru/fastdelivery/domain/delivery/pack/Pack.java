package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке (в граммах)
 * @param volume объем товаров в упаковке (в кубических миллиметрах)
 */
public record Pack(Weight weight, Volume volume) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));

    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package can't be more than " + maxWeight);
        }
    }
}
