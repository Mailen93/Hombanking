package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.Utilities;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
public class CardUtilsTest {
    @Test
    public void numerosRandom(){
        String cardNumber = Utilities.randomString();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void cvvNumber(){
        String completeNumber = Utilities.returnCvvNumber();
        assertThat(completeNumber, is(not(emptyOrNullString())));
    }
}
