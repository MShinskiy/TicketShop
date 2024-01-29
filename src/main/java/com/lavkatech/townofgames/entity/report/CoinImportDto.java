package com.lavkatech.townofgames.entity.report;

import lombok.Getter;

@Getter
public class CoinImportDto extends ImportDto{
    private final int houseMapId;
    private final int maxValue;
    private final int newValue;

    public CoinImportDto(String dtprf, int houseMapId, int maxValue, int newValue) {
        super(dtprf);
        this.houseMapId = houseMapId;
        this.maxValue = maxValue;
        this.newValue = newValue;
    }
}
