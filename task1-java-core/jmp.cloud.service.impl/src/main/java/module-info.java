import jmp.cloud.service.impl.main.ServiceImpl;

module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;
    exports jmp.cloud.service.impl.main;
    provides jmp.service.api.main.Service with ServiceImpl;
}