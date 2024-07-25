package internal.management.accounts.application.outbound.request;

public record UserSearchFilter(String userId, String userCode, String email, String firstName, String lastName, String lang) { }