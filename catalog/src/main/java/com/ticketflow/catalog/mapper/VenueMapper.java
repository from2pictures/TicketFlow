package com.ticketflow.catalog.mapper;

import com.ticketflow.catalog.entity.Venue;
import com.ticketflow.contracts.dto.request.CreateVenueRequest;
import com.ticketflow.contracts.dto.request.UpdateVenueRequest;
import com.ticketflow.contracts.dto.response.VenueSmallResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "events", ignore = true)
    Venue toEntity(CreateVenueRequest request);

    VenueSmallResponse toResponse(Venue venue);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateVenueRequest request, @MappingTarget Venue venue);
}
