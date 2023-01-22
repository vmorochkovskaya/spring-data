package application;

import jmp.bank.api.main.Bank;
import jmp.dto.bank.BankCardType;
import jmp.dto.bank.User;
import jmp.service.api.main.Service;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        //1 get BankImpl instance
        var serviceLoader = ServiceLoader.load(Bank.class);
        var optionalBank = serviceLoader.findFirst();
        optionalBank.orElseThrow(() -> new RuntimeException("No Bank service provider found"));
        var bank = optionalBank.get();

        //2 get BankCard impl
        var bankCard = bank.createBankCard(new User(), BankCardType.CREDIT).get();

        // 3 get ServiceImpl instance
        var serviceLoader2 = ServiceLoader.load(Service.class);
        var optionalService = serviceLoader2.findFirst();
        optionalService.orElseThrow(() -> new RuntimeException("No Service service provider found"));
        var service = optionalService.get();
        var subscription = service.getSubscriptionByBankCardNumber("123").orElseThrow(UnableToGetSubscriptionException::new);
        var subscriptionByCondition = service.getAllSubscriptionsByCondition(sb -> sb.getBankcard().equals("123"));
    }
}