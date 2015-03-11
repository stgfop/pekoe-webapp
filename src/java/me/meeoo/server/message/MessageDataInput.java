/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.meeoo.server.message;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonNumber;
import javax.json.JsonString;

/**
 *
 * @author duncan.berenguier
 */
public class MessageDataInput implements DataInput {

    private final List<Object> data;
    private int index = 0;

    MessageDataInput(Message message) {
        this.data = new ArrayList<>(message.getData().size() + 1);
        this.data.add(message.getEventName());
        this.data.addAll(message.getData());
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int skipBytes(int n) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean readBoolean() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        throw new IOException("Not a boolean");
    }

    @Override
    public byte readByte() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Byte) {
            return (Byte) v;
        }
        throw new IOException("Not a Byte");
    }

    @Override
    public int readUnsignedByte() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Integer) {
            final Integer s = (Integer) v;
            if (s < 0) {
                throw new IOException("Given byte is negative");
            } else if (s > Byte.MAX_VALUE * 2) {
                throw new IOException("Given byte is out of bound");
            }
            return s;
        }
        throw new IOException("Not a UnsignedByte");
    }

    @Override
    public short readShort() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Short) {
            return (Short) v;
        }
        throw new IOException("Not a Short");
    }

    @Override
    public int readUnsignedShort() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Integer) {
            final Integer s = (Integer) v;
            if (s < 0) {
                throw new IOException("Given short is negative");
            } else if (s > Short.MAX_VALUE * 2) {
                throw new IOException("Given short is out of bound");
            }
            return s;
        }
        throw new IOException("Not a UnsignedShort");
    }

    @Override
    public char readChar() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Character) {
            return (Character) v;
        }
        throw new IOException("Not a Character");
    }

    @Override
    public int readInt() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Integer) {
            return (Integer) v;
        } else if (v instanceof JsonNumber) {
            try {
                return ((JsonNumber) v).intValueExact();
            } catch (ArithmeticException ex) {
                throw new IOException("Not an Integer", ex);
            }
        }
        throw new IOException("Not an Integer");
    }

    @Override
    public long readLong() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Long) {
            return (Long) v;
        } else if (v instanceof JsonNumber) {
            try {
                return ((JsonNumber) v).longValueExact();
            } catch (ArithmeticException ex) {
                throw new IOException("Not a Long", ex);
            }
        }
        throw new IOException("Not a Long");
    }

    @Override
    public float readFloat() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Float) {
            return (Float) v;
        } else if (v instanceof JsonNumber) {
            try {
                return (float)((JsonNumber) v).doubleValue();
            } catch (ArithmeticException ex) {
                throw new IOException("Not a Float", ex);
            }
        }
        throw new IOException("Not a Float");
    }

    @Override
    public double readDouble() throws IOException {
        Object v = data.get(index++);
        if (v instanceof Double) {
            return (Double) v;
        } else if (v instanceof JsonNumber) {
            try {
                return ((JsonNumber) v).doubleValue();
            } catch (ArithmeticException ex) {
                throw new IOException("Not a Double", ex);
            }
        }
        throw new IOException("Not a Double");
    }

    @Override
    public String readLine() throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String readUTF() throws IOException {
        Object v = data.get(index++);
        if (v instanceof String) {
            return (String) v;
        } else if (v instanceof JsonString) {
            return v.toString();
        }
        throw new IOException("Not a UTF String");
    }

}
