package se.addq.exceltosie.config;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigHandlerTest {

    @Test
    void shouldGetPropertiesValueBasedOnKeyExisting() {
        String value = ConfigHandler.getValue("test1");
        String expectedResult = "hej";
        assertThat(value).isEqualTo(expectedResult);
    }

    @Test
    void shouldGetEmptyStringWhenValueBasedOnKeyNotExisting() {
        String value = ConfigHandler.getValue("test7");
        String expectedResult = "";
        assertThat(value).isEqualTo(expectedResult);
    }


}