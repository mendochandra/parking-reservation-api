package com.gtech.gtech.service;

import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.service.billing.BillingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BillingServiceImplTest {

    @InjectMocks
    private BillingServiceImpl billingService;


    @Test
    void shouldCalculateStandardHourlyBilling() {

        RateCard rateCard = new RateCard();
        rateCard.setHourlyRate(BigDecimal.valueOf(5000));
        rateCard.setDailyCap(BigDecimal.valueOf(50000));
        rateCard.setOvernightSurcharge(BigDecimal.valueOf(20000));
        rateCard.setGracePeriodMinutes(15);

        BigDecimal total = billingService.calculateAmount(
                LocalDateTime.of(2024,3,15,9,0),
                LocalDateTime.of(2024,3,15,12,45),
                rateCard
        );

        assertEquals(
                BigDecimal.valueOf(17500).setScale(2),
                total);
    }

    @Test
    void shouldApplyDailyCap() {

        RateCard rateCard = new RateCard();

        rateCard.setHourlyRate(BigDecimal.valueOf(15000));

        rateCard.setDailyCap(BigDecimal.valueOf(120000));

        rateCard.setOvernightSurcharge(BigDecimal.valueOf(30000));

        rateCard.setGracePeriodMinutes(15);

        BigDecimal total =
                billingService.calculateAmount(

                        LocalDateTime.of(
                                2024,3,15,7,0),

                        LocalDateTime.of(
                                2024,3,16,9,0),

                        rateCard);

        assertEquals(
                BigDecimal.valueOf(270000)
                        .setScale(2),
                total);
    }

    @Test
    void shouldReturnZeroWhenGracePeriodCoversStay() {

        RateCard rateCard = new RateCard();

        rateCard.setHourlyRate(
                BigDecimal.valueOf(3000));

        rateCard.setDailyCap(
                BigDecimal.valueOf(50000));

        rateCard.setOvernightSurcharge(
                BigDecimal.valueOf(20000));

        rateCard.setGracePeriodMinutes(15);

        BigDecimal total =
                billingService.calculateAmount(

                        LocalDateTime.of(
                                2024,3,15,14,0),

                        LocalDateTime.of(
                                2024,3,15,14,12),

                        rateCard);

        assertEquals(
                BigDecimal.ZERO.setScale(2),
                total);
    }


}
