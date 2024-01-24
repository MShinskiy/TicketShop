package com.lavkatech.townofgames.entity.report;

import com.lavkatech.townofgames.entity.enums.HouseName;

//var1/2/3 are going to be used to be inserted into a string
public record TasksImportDto(String dtprf,
                             HouseName houseName,
                             int taskCode,
                             boolean isTaskComplete,
                             String var1,
                             String var2,
                             String var3) {
}
