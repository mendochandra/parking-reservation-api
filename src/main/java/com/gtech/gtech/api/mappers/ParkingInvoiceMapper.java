package com.gtech.gtech.api.mappers;

import com.gtech.gtech.api.response.ParkingInvoiceResponse;
import com.gtech.gtech.domain.entity.ParkingInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingInvoiceMapper {

    @Mapping(target = "baseAmount",
            expression = "java(invoice.getBaseAmount().toPlainString())")

    @Mapping(target = "overnightSurchargeApplied",
            expression = "java(invoice.getOvernightSurchargeApplied().toPlainString())")

    @Mapping(target = "discountAmount",
            expression = "java(invoice.getDiscountAmount().toPlainString())")

    @Mapping(target = "totalAmount",
            expression = "java(invoice.getTotalAmount().toPlainString())")
    ParkingInvoiceResponse toResponse(ParkingInvoice invoice);
}
