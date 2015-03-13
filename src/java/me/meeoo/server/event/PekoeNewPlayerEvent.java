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
import me.meeoo.pekoe.server.PekoePlayer;
import me.meeoo.server.message.MessageDataOutput;


public class PekoeNewPlayerEvent extends ListGamesEvent<PekoeGame> {
    
    private String visualHash;
    private String player_name;

    private GameHolder<PekoeGame> gameHolder;
    private PekoeGame game;
    private PekoePlayer player;

    public void setGameHolder(GameHolder<PekoeGame> gameHolder) {
        this.gameHolder = gameHolder;
    }

    @Override
    public boolean execute(PekoeGame g) {
        game = gameHolder.get(visualHash);
        player = new PekoePlayer(player_name);
        game.addPlayer(player);
        return true;
    }
    
    @Override
    protected void sendEvent(DataOutput out) throws IOException {
        ((MessageDataOutput)out).writeJSON(player.toJSON(new StringBuilder()).toString());
        ((MessageDataOutput)out).writeJSON(game.toJSON(new StringBuilder()).toString());
    }

    @Override
    protected void readEvent(DataInput in) throws IOException {
        visualHash = in.readUTF();
    }


}
