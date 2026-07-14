package com.ticketflow.catalog.mapper;

import com.ticketflow.catalog.entity.Event;
import com.ticketflow.contracts.dto.request.CreateEventRequest;
import com.ticketflow.contracts.dto.request.UpdateEventRequest;
import com.ticketflow.contracts.dto.response.EventResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "venue", target = "venue")
    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true) // venue установим вручную
    @Mapping(target = "availableSeats", source = "totalSeats")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Event toEntity(CreateEventRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateEventRequest request, @MappingTarget Event event);
}