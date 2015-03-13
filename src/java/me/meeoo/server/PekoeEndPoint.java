/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.meeoo.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import me.meeoo.boardgameserver.BoardGameServer;
import me.meeoo.boardgameserver.event.ServerEvent;
import me.meeoo.otomaton.event.Event;
import me.meeoo.pekoe.server.PekoeGame;
import me.meeoo.pekoe.server.PekoePlayer;
import me.meeoo.pekoe.server.rule.PekoeOtomaton;
import me.meeoo.server.event.PekoeListGameEvent;
import me.meeoo.server.event.PekoeNewGameEvent;
import me.meeoo.server.event.PekoeNewPlayerEvent;
import me.meeoo.server.message.Message;
import me.meeoo.server.message.MessageDecoder;
import me.meeoo.server.message.MessageEncoder;

/**
 *
 * @author duncan.berenguier
 */
@ServerEndpoint(value = "/ws", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class PekoeEndPoint extends BoardGameServer<PekoeGame, PekoeOtomaton, PekoePlayer> {

    private static final Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void broadcastMessage(Message message, Session session) {
        try {
            System.err.println("broadcastMessage " + message);

            Event<PekoeGame> event = null;
            try {
                event = recieve(message.getIn());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                System.err.println("Can't instantiate event " + message.getEventName() + ", " + ex);
                //message.getOut().writeUTF("Unknown Event");
            }
            System.err.println("event : " + event);
            if (event != null) {
                send(message.getOut(), event);
            }

            for (Session peer : peers) {
                //if (!peer.equals(session)) {
                peer.getBasicRemote().sendObject(message);
                //}

            }
        } catch (Throwable ex) {
            System.err.printf("got throwable: %s\n%s\n", ex.getClass().getSimpleName(), ex.getMessage());
            ex.printStackTrace(System.err);
        }
        System.err.println("broadcastMessage end");
    }

    @OnOpen
    public void onOpen(Session peer) {
        System.err.printf("peer connected %s\n", peer.toString());
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        System.err.printf("peer disconnected %s\n", peer.toString());
        peers.remove(peer);
    }

    @Override
    protected boolean recieveServerEvent(ServerEvent event) {
        if (event instanceof PekoeListGameEvent) {
            ((PekoeListGameEvent) event).setGameHolder(games);
            return true;
        } else if (event instanceof PekoeNewPlayerEvent) {
            ((PekoeNewPlayerEvent) event).setGameHolder(games);
            return event.execute(null);
        } else if (event instanceof PekoeNewGameEvent) {
            ((PekoeNewGameEvent) event).setGameHolder(games);
            return event.execute(null);
        }
        return false;
    }

}
