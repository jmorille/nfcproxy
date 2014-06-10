package eu.ttbox.nfcparser.emv.status;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Emv41SWLabelTest {

    @Test
    public void testGetError() throws Exception {
        ArrayList<Emv41SWLabelItem> items = Emv41SWLabel.getError((byte)0x90, (byte)0x00);
        assertEquals(1, items.size());

    }
}