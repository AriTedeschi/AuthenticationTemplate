package internal.management.accounts.application.outbound.response;

public record UserResponse(String userId, String userCode, String email, String firstName, String lastName) {}
