package eu.ttbox.nfcparser.parser.pdol;

import org.junit.Test;

import eu.ttbox.nfcparser.utils.NumUtil;

import static org.junit.Assert.assertEquals;

public class PdolGeneratorTest {

    @Test
    public void testGeneratePdolRequestData() throws Exception {
        byte[] pdol = NumUtil.hex2Byte("9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04");
        PdolGenerator gen = new PdolGenerator(pdol);
        byte[] pdolData =  gen.generatePdolRequestData();

        assertEquals(33, pdolData.length);
    }
}