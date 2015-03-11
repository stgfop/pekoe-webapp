/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.meeoo.server.message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author duncan.berenguier
 */
public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(String message) throws DecodeException {
        System.err.printf("decoding %s\n", message);
        return Message.decode(message);
    }

    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
