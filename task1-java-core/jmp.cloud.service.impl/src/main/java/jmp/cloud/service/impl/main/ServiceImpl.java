package jmp.cloud.service.impl.main;

import jmp.dto.bank.BankCard;
import jmp.dto.bank.Subscription;
import jmp.dto.bank.User;
import jmp.service.api.main.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServiceImpl implements Service {
    @Override
    public void subscribe(BankCard bankCard) {
        ALL_SUBSCRIPTIONS.add(new Subscription(bankCard.getNumber(), LocalDate.now()));
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String bankCardNumber) {
        return ALL_SUBSCRIPTIONS.stream()
                .filter(sb -> sb.getBankcard().equals(bankCardNumber))
                .findAny();
    }

    @Override
    public List<User> getAllUsers() {
        return ALL_USERS;
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> subscriptionPredicate) {
        return ALL_SUBSCRIPTIONS.stream()
                .filter(subscriptionPredicate)
                .collect(Collectors.toList());
    }
}

