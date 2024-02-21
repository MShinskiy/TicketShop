package com.lavkatech.townofgames.entity.report.dto;

import lombok.Getter;

//var1/2/3 are going to be used to be inserted into a string
@Getter
public class TasksImportDto extends ImportDto {

    private final int houseMapId;
    private final int taskCode;
    private final boolean isTaskComplete;
    private final String var1;
    private final String var2;
    private final String var3;

    public TasksImportDto(String dtprf, int houseMapId, int taskCode, boolean isTaskComplete, String var1, String var2, String var3) {
        super(dtprf);
        this.houseMapId = houseMapId;
        this.taskCode = taskCode;
        this.isTaskComplete = isTaskComplete;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }
}
