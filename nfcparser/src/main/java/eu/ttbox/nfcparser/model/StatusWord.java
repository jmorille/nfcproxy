/*
 * Copyright (c) 2005 Chang Sau Sheong, Thomas Tarpin-Lyonnet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.ttbox.nfcparser.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import eu.ttbox.nfcparser.utils.NumUtil;

/**
 * The status word in the card response
 * @see CardResponse
 */
public class StatusWord {

    private byte sw1;
    private byte sw2;

    public StatusWord(byte sw1, byte sw2) {
        this.sw1 = sw1;
        this.sw2 = sw2;
    }

    public byte getSw1() {
        return sw1;
    }

    public void setSw1(byte sw1) {
        this.sw1 = sw1;
    }

    public byte getSw2() {
        return sw2;
    }

    public void setSw2(byte sw2) {
        this.sw2 = sw2;
    }

    public final byte[] getBytes() throws IOException {
        byte[] byteArray;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        baos.write(getSw1());
        baos.write(getSw2());

        byteArray = baos.toByteArray();
        baos.close();

        return byteArray;
    }

    public boolean isSuccess() {
        if (sw1 == (byte) 0x90 && sw2 == (byte) 0x00) {
            return true;
        }
        return false;

    }

    public String toString() {
        String str = null;

        try {
            str = NumUtil.byte2Hex(getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
