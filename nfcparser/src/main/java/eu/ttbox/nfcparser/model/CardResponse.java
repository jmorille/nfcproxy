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

import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.NumUtil;

/**
 * The response from the smart card after a command is sent to the card. Contains a status word object
 */
public class CardResponse {

    private byte[] command;

    private byte[] data;

    public StatusWord statusWord;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public StatusWord getStatusWord() {
        return statusWord;
    }

    public void setStatusWord(StatusWord statusWord) {
        this.statusWord = statusWord;
    }

    public CardResponse(byte[] command, byte[] recv) {
        this.command = command;
        this.statusWord = new StatusWord(recv[recv.length - 2], recv[recv.length - 1]);
        this.data =  TLVParser.getData(recv);
    }

    public boolean isSuccess() {
        if (statusWord != null) {
            return statusWord.isSuccess();
        }
        return false;

    }

    public final byte[] getBytes()   {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();


            if (data != null) {
                baos.write(data);
            }
            baos.write(statusWord.getSw1());
            baos.write(statusWord.getSw2());

            byteArray = baos.toByteArray();
            baos.close();
        } catch (IOException ioe) {
            throw new RuntimeException("IOException :" + ioe.getMessage(), ioe);
        }
        return byteArray;
    }

    public final byte[] getCommandBytes() {
        return command;
    }
    public final byte[] getRespBytes() {
        byte[] byteArray = data;

        return byteArray;
    }

    public String toString() {
        String ret = null;

        try {
            if (data.length != 0) {
                ret = "[R] " + NumUtil.byte2Hex(getRespBytes()) + "\n[SW] " + NumUtil.byte2Hex(statusWord.getBytes()) + "\n";
            } else {
                ret = "[SW] " + NumUtil.byte2Hex(statusWord.getBytes()) + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
