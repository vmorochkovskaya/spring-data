import jmp.service.api.main.Service;

module application {
    requires jmp.bank.api;
    uses jmp.bank.api.main.Bank;
    requires jmp.service.api;
    uses Service;
    requires jmp.cloud.service.impl;
    requires jmp.cloud.bank.impl;
    requires jmp.dto;
}