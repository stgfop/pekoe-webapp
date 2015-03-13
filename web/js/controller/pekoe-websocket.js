'use strict';

angular.module('PekoeApp', ['ngWebsocket']).service('EventCommunicationChannel', function() {
    var ecc = {
        event: null,
        newEvent: setEvent
    };
    return ecc;

    function setEvent(e) {
        ecc.event = e;
    }
}).service('PekoeWebsocketService', ['$rootScope', '$websocket', function($rootScope, $websocket) {
        var me = {
            ws: getWS,
            listGames: listGamesAction,
            newGame: newGameAction
        };
        var ws;

        return me;

        function getWS() {
            ws = $websocket.$new({
                url: 'ws://localhost:8080/pekoe/ws',
                reconnect: true // it will reconnect after 2 seconds
            }); // instance of ngWebsocket, handled by $websocket service
            ws.status = "disconnected";

            ws.$on('$open', function() {
                console.log("WEBSOCKET: $open");
                ws.status = "connected";
                listGamesAction();
            });

            ws.$on('me.meeoo.server.event.PekoeListGameEvent', function(data) {
                console.log("WEBSOCKET<-event ", data);
                var event = {};
                event.name = data.shift();
                event.transactionId = data.shift();
                event.gamesCount = data.shift();
                event.games = [];
                for (var i = 0; i < event.gamesCount; i++) {
                    var game = {};
                    game.creation = data.shift();
                    game.visualHash = data.shift();
                    game.name = data.shift();
                    game.playerCount = data.shift();
                    game.isStarted = data.shift();
                    event.games.add(game);
                }
                $rootScope.eventChannel.recieve(event);
            });

            ws.$on('me.meeoo.server.event.PekoeNewGameEvent', function(data) {
                console.log("WEBSOCKET<-event ", data);
                var event = {};
                event.name = data.shift();
                event.transactionId = data.shift();
                event.player = JSON.parse(data.shift());
                event.game = JSON.parse(data.shift());

                $rootScope.eventChannel.recieve(event);
            });

            ws.$on('$close', function() {
                ws.status = "disconnected";
                console.log('WEBSOCKET: $close recieved');
            });

            ws.$on('$error', function(error) {
                console.log('An error occured');
                console.log(error);
            });

            ws.$on('$codedMessage', function(msg) {
                console.log('Could not decode message', msg);
            });


            return ws;
        }

        function listGamesAction() {
            ws.$emit('me.meeoo.server.event.PekoeListGameEvent', ["", 0]); // send a message to the websocket server
        }
        function newGameAction(game_name, player_name) {
            console.log("Ola");
            ws.$emit('me.meeoo.server.event.PekoeNewGameEvent', ["", 0, game_name, player_name]); // send a message to the websocket server
        }

    }]);