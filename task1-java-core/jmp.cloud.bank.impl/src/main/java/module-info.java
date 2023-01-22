module jmp.cloud.bank.impl {
    requires transitive jmp.bank.api;
    requires jmp.dto;
    exports jmp.cloud.bank.impl.main;
    provides jmp.bank.api.main.Bank with jmp.cloud.bank.impl.main.BankImpl;
}