import jmp.service.api.main.Service;

module application {
    uses jmp.bank.api.main.Bank;
    uses Service;
    requires jmp.cloud.service.impl;
    requires jmp.cloud.bank.impl;
    requires jmp.dto;
}