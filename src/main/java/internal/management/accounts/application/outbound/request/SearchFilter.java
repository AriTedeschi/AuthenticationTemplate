package internal.management.accounts.application.outbound.request;

public record SearchFilter(String userId, String userCode, String email, String firstName, String lastName, String lang) { }