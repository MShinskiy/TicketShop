package com.lavkatech.townofgames.entity.report;

import com.lavkatech.townofgames.entity.enums.HouseName;

public record CoinImportDto(String dtprf, HouseName houseName, int maxValue, int newValue) {
}
