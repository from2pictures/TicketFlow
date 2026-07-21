package com.ticketflow.booking.mapper;

import com.ticketflow.booking.entity.Booking;
import com.ticketflow.contracts.dto.request.CreateBookingRequest;
import com.ticketflow.contracts.dto.response.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Booking toEntity(CreateBookingRequest request);

    BookingResponse toResponse(Booking booking);
}
