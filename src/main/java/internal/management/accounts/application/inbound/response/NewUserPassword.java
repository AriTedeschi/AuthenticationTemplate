package internal.management.accounts.application.inbound.response;

public record NewUserPassword(String userId, String userCode, String password) {}
