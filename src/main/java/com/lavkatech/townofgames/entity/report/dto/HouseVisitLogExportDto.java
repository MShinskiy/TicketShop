package com.lavkatech.townofgames.entity.report.dto;

import lombok.Getter;

@Getter
public class HouseVisitLogExportDto extends ExportDto{

    private final String group;
    private final String level;
    private final String login;
    private final String h1;
    private final String h2;
    private final String h3;
    private final String h4;
    private final String h5;
    private final String h6;
    private final String h7;
    private final String h8;
    private final String h9;

    public HouseVisitLogExportDto(String dtprf,
                                  String group,
                                  String level,
                                  String login,
                                  String h1,
                                  String h2,
                                  String h3,
                                  String h4,
                                  String h5,
                                  String h6,
                                  String h7,
                                  String h8,
                                  String h9) {
        super(dtprf);
        this.group = group;
        this.level = level;
        this.login = login;
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.h4 = h4;
        this.h5 = h5;
        this.h6 = h6;
        this.h7 = h7;
        this.h8 = h8;
        this.h9 = h9;
    }
}
