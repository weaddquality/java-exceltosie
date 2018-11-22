package se.addq.exceltosie.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BadDataExceptionTest {

    @Test
    void shouldBeAbleToInitializeExceptionNoAddedException() {
        BadDataException badDataException = new BadDataException("Fel");
        assertThat(badDataException.getMessage()).isEqualTo("Fel");
    }

    @Test
    void shouldBeAbleToInitializeExceptionAddedException() {
        String exceptionMessage = "Exception error";
        BadDataException badDataException = new BadDataException("Fel", new NullPointerException(exceptionMessage));
        assertThat(badDataException.getMessage()).isEqualTo("Fel");
        assertThat(badDataException.getCause().getMessage()).isEqualTo(exceptionMessage);
    }


}