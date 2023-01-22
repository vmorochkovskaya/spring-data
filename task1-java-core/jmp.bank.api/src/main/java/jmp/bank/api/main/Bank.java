package jmp.bank.api.main;

import jmp.dto.bank.BankCardType;
import jmp.dto.bank.User;
import jmp.dto.bank.BankCard;

import java.util.function.Supplier;

public interface Bank {
    Supplier<BankCard> createBankCard(User user, BankCardType bankCardType);
}
