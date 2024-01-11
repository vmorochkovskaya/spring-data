package jmp.service.api.main;

import jmp.dto.bank.BankCard;
import jmp.dto.bank.Subscription;
import jmp.dto.bank.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Service {
    List<User> ALL_USERS = List.of(new User("Peter", "Pan", LocalDate.of(1995, 04, 30)),
            new User("Harry", "Potter", LocalDate.of(1997, 03, 30)));

    List<Subscription> ALL_SUBSCRIPTIONS = Arrays.asList(new Subscription("123", LocalDate.of(2022, 06, 10)),
            new Subscription("345", LocalDate.of(2022, 07, 11)));


    default double getAverageUsersAge() {
        var users = getAllUsers();
        return users.stream()
                .mapToLong(Service::getAge)
                .average()
                .orElse(0.0);
    }

    static boolean isPayableUser(User user) {
        return getAge(user) > 18;
    }

    private static long getAge(User user) {
        return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
    }

    void subscribe(BankCard bankCard);

    Optional<Subscription> getSubscriptionByBankCardNumber(String bankCardNumber);

    List<User> getAllUsers();

    List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> subscriptionPredicate);
    }
