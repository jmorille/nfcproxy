package eu.ttbox.nfcproxy.ui.proxy;

import android.nfc.Tag;
import android.nfc.tech.TagTechnology;

import java.io.IOException;

// https://code.google.com/p/dexmaker/
public class TagTechnologyProxy implements TagTechnology {

    private TagTechnology tagTechnology;

    public TagTechnologyProxy(TagTechnology tagTechnology) {
        this.tagTechnology = tagTechnology;
    }

    @Override
    public Tag getTag() {
        return this.tagTechnology.getTag();
    }

    @Override
    public void connect() throws IOException {
        this.tagTechnology.connect();
    }

    @Override
    public void close() throws IOException {
        this.tagTechnology.close();
    }

    @Override
    public boolean isConnected() {
       return  this.tagTechnology.isConnected();
    }
}
