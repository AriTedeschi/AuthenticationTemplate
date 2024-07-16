package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.request.UserRegisterRequest;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.register.RequestValidatorFlow;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EmailRegisterValidatorTest {

    @ParameterizedTest
    @MethodSource("validateStream")
    void validate(String email, UserEntity userEntity, boolean expectedError){
        UserRepository repository = Mockito.mock(UserRepository.class);
        UserRegisterRequest request = new UserRegisterRequest(email,null,null,null);
        Optional<UserEntity> queryResponse = Objects.isNull(userEntity) ? Optional.empty() : Optional.of(userEntity);
        when(repository.findByEmail(email)).thenReturn(queryResponse);

        RequestValidatorFlow registerValidator = new RequestValidatorFlow(request,repository);
        new EmailRegisterValidator(email, registerValidator, repository,null).validate();
        assertEquals(expectedError,registerValidator.containsError());
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

}
