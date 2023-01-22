package jmp.cloud.bank.impl.main;

import jmp.dto.bank.*;
import jmp.bank.api.main.Bank;

import java.util.function.Supplier;

import static jmp.dto.bank.BankCardType.CREDIT;

public class BankImpl implements Bank {
    @Override
    public Supplier<BankCard> createBankCard(User user, BankCardType bankCardType) {
        if (CREDIT.equals(bankCardType)) {
            return CreditBankCard::new;
        } else return DebitBankCard::new;
    }
}
