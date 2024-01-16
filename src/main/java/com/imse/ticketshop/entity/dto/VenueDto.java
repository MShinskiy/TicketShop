package com.imse.ticketshop.entity.dto;

import java.util.UUID;

public record VenueDto(UUID id,
                       String email,
                       String password,
                       String name,
                       String address,
                       String city,
                       String country
) { }
