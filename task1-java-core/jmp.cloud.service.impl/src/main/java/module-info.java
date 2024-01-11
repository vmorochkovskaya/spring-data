import jmp.cloud.service.impl.main.ServiceImpl;

module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;
    provides jmp.service.api.main.Service with ServiceImpl;
}