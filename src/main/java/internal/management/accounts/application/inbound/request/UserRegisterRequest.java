package internal.management.accounts.application.inbound.request;

public record UserRegisterRequest(String email, String password, String firstName, String lastName) {}
