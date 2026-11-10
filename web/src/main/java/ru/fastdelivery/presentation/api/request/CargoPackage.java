package ru.fastdelivery.presentation.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.fastdelivery.domain.common.volume.Volume;

import java.math.BigDecimal;
import java.math.BigInteger;

public record CargoPackage(
        @Schema(description = "Вес упаковки, граммы", example = "4056")
        BigInteger weight,
        @Schema(description = "Длина упаковки, миллиметры", example = "1120")
        BigInteger length,
        @Schema(description = "Ширина упаковки, миллиметры", example = "560")
        BigInteger width,
        @Schema(description = "Высота упаковки, миллиметры", example = "730")
        BigInteger height
) {
}
