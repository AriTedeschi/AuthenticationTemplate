package internal.management.accounts.domain.model.request;

public record UserRegisterRequest(String email, String password, String firstName, String lastName) {}
