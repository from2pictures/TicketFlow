package com.ticketflow.catalog.mapper;

import com.ticketflow.catalog.entity.Event;
import com.ticketflow.contracts.dto.request.CreateEventRequest;
import com.ticketflow.contracts.dto.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponse toResponse(Event event);

    Event toEntity(CreateEventRequest request);

    void updateEntity(CreateEventRequest request, @MappingTarget Event event);
}