/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.meeoo.server.message;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Message {

    public static Message decode(String message) {
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        String eventName = jsonObject.getString("event");
        List data = jsonObject.getJsonArray("data");
        return new Message(eventName, data);
    }

    private final String eventName;
    private List<Object> data;
    private MessageDataOutput out;
    private MessageDataInput in;

    public Message(String eventName, List<Object> data) {
        this.eventName = eventName;
        if (data == null) {
            this.data = new LinkedList<>();
        } else {
            this.data = data;
        }
    }

    public String encode() {
        if (out != null) {
            data = out.encode();
        }
        return String.format("{\"event\":\"%s\",\"data\":%s}", eventName, data);
    }

    @Override
    public String toString() {
        return String.format("%s[%d]", eventName, data.size());
    }

    public DataInput getIn() {
        if (in == null) {
            in = new MessageDataInput(this);
        }
        return in;
    }

    public DataOutput getOut() {
        if (out == null) {
            out = new MessageDataOutput(this);
        }
        return out;
    }

    public List<Object> getData() {
        return data;
    }

    public String getEventName() {
        return eventName;
    }
}
