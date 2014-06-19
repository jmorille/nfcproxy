package eu.ttbox.nfcproxy.ui.proxy.emulator;


import android.nfc.Tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//TODO: HACK since BasicTagTechnology is not currently visible from SDK. primiarly want the transceive() method (otherwise we could just use TagTechnology)
//TODO: Just access BasicTagTechnolgy directly but modifying platform library android.jar. (use library with hidden classes from framework.jar)

public class BasicTagTechnologyWrapper {


    //	Method get;
    Method transceive;
    Method isConnected;
    Method connect;
    //Method getMaxTransceiveLength;
    Method close;
    Tag mTag;
    Object mTagTech;

    public BasicTagTechnologyWrapper(Tag tag, String tech) throws ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class cls = Class.forName(tech);
        Method get = cls.getMethod("get", Tag.class);
        mTagTech = get.invoke(null, tag);
        transceive = cls.getMethod("transceive", byte[].class);
        isConnected = cls.getMethod("isConnected");
        connect = cls.getMethod("connect");
        //getMaxTransceiveLength = cls.getMethod("getMaxTransceiveLength");
        close = cls.getMethod("close");
        mTag = tag;
    }

    // @Override
    public boolean isConnected() {
        Boolean ret = false;
        try {
            ret = (Boolean) isConnected.invoke(mTagTech);
        } catch (Exception e) {
            throw new RuntimeException("Could not isConnected Tag : " + e.getMessage());
        }
        return ret;
    }

    // @Override
    public void connect() {
        try {
            connect.invoke(mTagTech);
        } catch (Exception e) {
            throw new RuntimeException("Could not connect Tag : " + e.getMessage());
        }
    }

    // @Override
    public void close() throws IOException {
        try {
            close.invoke(mTagTech);
        } catch (Exception e) {
            throw new RuntimeException("Could not close Tag : " + e.getMessage());
        }
    }

    /*
    public int getMaxTransceiveLength() {
        try {
            return (Integer)getMaxTransceiveLength.invoke(mTagTech);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
            e.printStackTrace();
        }
        return 0;
    }
    */
    public byte[] transceive(byte[] data) {
        try {
            return (byte[]) transceive.invoke(mTagTech, data);
        } catch (Exception e) {
            throw new RuntimeException("Could not transceive Tag : " + e.getMessage());
        }
    }

    //  @Override
    public Tag getTag() {
        return mTag;
    }
/*
    @Override
	public void reconnect() throws IOException {
		// TODO Auto-generated method stub

	}
*/
}
