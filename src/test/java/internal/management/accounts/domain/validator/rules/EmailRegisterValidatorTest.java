package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.register.UserRegisterValidatorFlow;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class EmailRegisterValidatorTest {

    @ParameterizedTest
    @MethodSource("validateStream")
    void validate(String email, UserEntity userEntity, boolean expectedError){
        UserRepository repository = Mockito.mock(UserRepository.class);
        UserRegisterRequest request = new UserRegisterRequest(email,null,null,null);
        Optional<UserEntity> queryResponse = Objects.isNull(userEntity) ? Optional.empty() : Optional.of(userEntity);
        when(repository.findByEmail(eq(email), eq(0))).thenReturn(queryResponse);

        UserRegisterValidatorFlow registerValidator = new UserRegisterValidatorFlow(request,0,"en",repository);
        EmailRegisterValidator emailRegisterValidator = new EmailRegisterValidator(email, 0,registerValidator, repository, null);

        emailRegisterValidator.validate();
        assertEquals(expectedError,registerValidator.containsError());
    }

    @ParameterizedTest
    @MethodSource("validateBundleStream")
    void validateBundle(String locale, String expectedMessage){
        String email = null;

        UserRepository repository = Mockito.mock(UserRepository.class);
        UserRegisterRequest request = new UserRegisterRequest(email,null,null,null);
        Optional<UserEntity> queryResponse = Optional.empty();
        when(repository.findByEmail(eq(email), eq(0))).thenReturn(queryResponse);

        UserRegisterValidatorFlow registerValidator = new UserRegisterValidatorFlow(request,0,locale,repository);
        new EmailRegisterValidator(email, 0, registerValidator, repository, null).validate();
        ValidationException exception = assertThrows(ValidationException.class, registerValidator::validate);

        assertEquals(expectedMessage,exception.convert().get("email"));
    }

    private static Stream<Arguments> validateStream() {
        return Stream.of(
                Arguments.of(null, null, true),
                Arguments.of("@", null, true),
                Arguments.of("@gmail.com", null, true),
                Arguments.of("aaaaaa@gmail", null, true),
                Arguments.of("aaaa-aa@.com", null, true),
                Arguments.of("a@gmail.com", UserEntity.builder().build(), true),
                Arguments.of("a@gmail.com", null, false),
                Arguments.of("a.a@hotmail.com", null, false),
                Arguments.of("b@yahoo.com", null, false),
                Arguments.of("c@outlook.com", null, false)
        );
    }

    private static Stream<Arguments> validateBundleStream() {
        return Stream.of(
                Arguments.of("pt", "Informe um email!"),
                Arguments.of("en", "Provide an email!"),
                Arguments.of("fr", "Provide an email!"),
                Arguments.of(null, "Provide an email!")
        );
    }

}
