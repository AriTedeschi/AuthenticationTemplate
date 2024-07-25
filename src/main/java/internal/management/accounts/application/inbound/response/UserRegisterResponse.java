package internal.management.accounts.application.inbound.response;

public record UserRegisterResponse(String userId, String userCode, String email, String fullname) {}
