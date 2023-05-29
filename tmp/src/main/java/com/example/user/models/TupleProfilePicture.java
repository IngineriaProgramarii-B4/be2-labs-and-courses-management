package com.example.user.models;

public class TupleProfilePicture {
    private byte[] bytes;
    private String extension;

    public TupleProfilePicture(byte[] bytes, String extension) {
        this.bytes = bytes;
        this.extension = extension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
