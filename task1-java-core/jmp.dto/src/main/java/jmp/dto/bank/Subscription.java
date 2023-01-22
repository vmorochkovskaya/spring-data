package jmp.dto.bank;

import java.time.LocalDate;

public class Subscription {
    public Subscription(String bankcard, LocalDate startDate) {
        this.bankcard = bankcard;
        this.startDate = startDate;
    }

    private String bankcard;
    private LocalDate startDate;

    public String getBankcard() {
        return bankcard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
