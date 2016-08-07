package me.ni554n.rebootrouter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void ip_isCorrect() throws Exception {
        int address = 16885952;
        String ip = "192.168.1.1";

        assertEquals(Utils.parseIp(address), ip);

        address = 0;
        ip = "0.0.0.0";

        assertEquals(Utils.parseIp(address), ip);
    }
}