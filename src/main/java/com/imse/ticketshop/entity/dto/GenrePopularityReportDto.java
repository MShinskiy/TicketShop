package com.imse.ticketshop.entity.dto;

import lombok.Builder;

@Builder
public record GenrePopularityReportDto(
        String cityVenue,
        String nameVenue,
        String bandConcert,
        String genreConcert,
        int nTickets
) {}
