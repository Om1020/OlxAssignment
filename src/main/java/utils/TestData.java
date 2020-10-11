package utils;

import org.aeonbits.owner.Config;

@Config.Sources({
        "file:src/main/resources/testdata/${env}/testdata.properties"
})
public interface TestData extends Config {

    String SUCCESS_PAYMENT_CARD();

    String FAILED_PAYMENTCARD();

    String EXP_DATE();

    String CVV();

    String OTP();
}
