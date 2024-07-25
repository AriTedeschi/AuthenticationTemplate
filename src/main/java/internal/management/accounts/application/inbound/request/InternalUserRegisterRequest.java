package internal.management.accounts.application.inbound.request;

public record InternalUserRegisterRequest(String email, String password, String firstName, String lastName, Integer roleId) {}
