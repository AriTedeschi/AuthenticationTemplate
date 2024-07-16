package internal.management.accounts.domain.model.response;

public record UserRegisterResponse(String uuid, String userCode, String email, String fullname) {}
