package com.marz.snapprefs;

import android.graphics.Bitmap;

import com.marz.snapprefs.Saving.SnapType;
import com.marz.snapprefs.Saving.MediaType;

import java.io.FileInputStream;
import java.util.ArrayList;

public class SnapData
{
    private String mId;
    private String mKey;
    private String strSender;
    private String strTimestamp;
    private FileInputStream inputStream;
    private Bitmap bmpImage;
    private MediaType mediaType;
    private SnapType snapType;
    private ArrayList<FlagState> flags = new ArrayList<>();

    public SnapData() {

    }

    public SnapData( String mKey ) {
        this.mKey = mKey;
    }

    public void setHeader( String mId, String mKey, String strSender, String strTimestamp,
                           SnapType snapType ) {
        this.mId = mId;
        this.mKey = mKey;
        this.strSender = strSender;
        this.strTimestamp = strTimestamp;
        this.snapType = snapType;
        flags.add( FlagState.HEADER );
        checkForCompletion();
    }

    public boolean setPayload( Object payload ) {
        if ( payload instanceof Bitmap ) {
            bmpImage = (Bitmap) payload;
            mediaType = MediaType.IMAGE;
        } else if ( payload instanceof FileInputStream ) {
            inputStream = (FileInputStream) payload;
            mediaType = MediaType.VIDEO;
        } else
            return false;

        flags.add( FlagState.PAYLOAD );
        checkForCompletion();
        return true;
    }

    public Object getPayload() {
        if ( mediaType == MediaType.IMAGE )
            return bmpImage;
        else if ( mediaType == MediaType.VIDEO )
            return inputStream;

        return null;
    }

    /***
     * Used to remove any unnecessary data
     * Should save memory in the long run
     */
    public void wipePayload() {
        this.bmpImage = null;
        this.inputStream = null;
        flags.remove( FlagState.COMPLETED );
        flags.add( FlagState.SAVED );
    }

    private void checkForCompletion() {
        if ( flags.contains( FlagState.COMPLETED ) )
            return;

        boolean isComplete =
                flags.contains( FlagState.HEADER ) && flags.contains( FlagState.PAYLOAD );

        if ( isComplete )
            flags.add( FlagState.COMPLETED );
    }

    // ### GETTERS & SETTERS ### \\

    public String getmId() {
        return mId;
    }

    public void setmId( String mId ) {
        this.mId = mId;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey( String mKey ) {
        this.mKey = mKey;
    }

    public String getStrSender() {
        return strSender;
    }

    public void setStrSender( String strSender ) {
        this.strSender = strSender;
    }

    public String getStrTimestamp() {
        return strTimestamp;
    }

    public void setStrTimestamp( String strTimestamp ) {
        this.strTimestamp = strTimestamp;
    }

    public FileInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream( FileInputStream inputStream ) {
        this.inputStream = inputStream;
    }

    public Bitmap getBmpImage() {
        return bmpImage;
    }

    public void setBmpImage( Bitmap bmpImage ) {
        this.bmpImage = bmpImage;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType( MediaType mediaType ) {
        this.mediaType = mediaType;
    }

    public SnapType getSnapType() {
        return snapType;
    }

    public void setSnapType( SnapType snapType ) {
        this.snapType = snapType;
    }

    public ArrayList<FlagState> getFlags() {
        return flags;
    }

    public void setFlags( ArrayList<FlagState> flags ) {
        this.flags = flags;
    }

    // ### Enums ### \\

    public enum FlagState
    {
        HEADER, PAYLOAD, PROCESSING, COMPLETED, SAVED, FAILED
    }
}
