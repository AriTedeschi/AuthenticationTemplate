package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.register.UserRegisterValidatorFlow;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameRegisterValidatorTest {

    @ParameterizedTest
    @MethodSource("validateStream")
    void validate(String firstName, String lastName, boolean expectedError){
        UserRepository repository = Mockito.mock(UserRepository.class);
        UserRegisterRequest request = new UserRegisterRequest("email","password",firstName,lastName);
        NameVO name = new NameVO(firstName,lastName);

        UserRegisterValidatorFlow registerValidator = new UserRegisterValidatorFlow(request,0,null,repository);
        new NameRegisterValidator(name, registerValidator,null).validate();
        assertEquals(expectedError,registerValidator.containsError());
    }

    private static Stream<Arguments> validateStream() {
        return Stream.of(
                Arguments.of("_Arthur","Ribeiro", true),
                Arguments.of("Arthur","#Ribeiro", true),
                Arguments.of("Jose@","Cunha", true),
                Arguments.of("Marcos"," S.", true),
                Arguments.of("Ari",null, true),
                Arguments.of("Rafael","Justo", false),
                Arguments.of("ViNiCIUS","Lima", false)
        );
    }

}
