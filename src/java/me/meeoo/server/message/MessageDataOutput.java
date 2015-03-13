/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.meeoo.server.message;

import java.io.DataOutput;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author duncan.berenguier
 */
public class MessageDataOutput implements DataOutput {

    private final List<Object> data;

    MessageDataOutput(Message message) {
        this.data = new LinkedList<>();
    }

    @Override
    public void write(int b) throws IOException {
        data.add(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        data.add(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        data.add(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        data.add(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        data.add(s);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        data.add('"'+s+'"');
    }

    public List<Object> encode() {
        return data;
    }

    public void writeJSON(String s) {
        data.add(s);
    }
}
