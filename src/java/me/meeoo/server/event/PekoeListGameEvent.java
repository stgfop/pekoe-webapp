/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.meeoo.server.event;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import me.meeoo.boardgameserver.GameHolder;
import me.meeoo.boardgameserver.event.ListGamesEvent;
import me.meeoo.pekoe.server.PekoeGame;


public class PekoeListGameEvent extends ListGamesEvent<PekoeGame> {

    private GameHolder<PekoeGame> gameHolder;

    public void setGameHolder(GameHolder<PekoeGame> gameHolder) {
        this.gameHolder = gameHolder;
    }

    @Override
    public boolean execute(PekoeGame game) {
        return true;
    }
    
    
    
    @Override
    protected void sendEvent(DataOutput out) throws IOException {
        GameHolder.TTL[] listAll = gameHolder.listAll();
        out.writeLong(listAll.length);
        for (GameHolder.TTL ttl : listAll) {
            out.writeLong(ttl.getTimeOfCreation());
            PekoeGame game = gameHolder.get(ttl.getKey());
            out.writeUTF(game.getVisualHash());
            out.writeUTF(game.getName());
            out.writeInt(game.getPlayers().size());
            out.writeBoolean(game.isStarted());
        }
    }

    @Override
    protected void readEvent(DataInput in) throws IOException {
    }


}
