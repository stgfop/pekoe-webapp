'use strict';

angular.module('PekoeApp').controller('PekoeController', ['$scope', '$rootScope', 'PekoeWebsocketService', function($scope, $rootScope, PekoeWebsocketService) {

        var eventHandlers = new Map();
        $scope.lobby = {
            games: [],
            playerName: "UnnamedPlayer",
            newGameAction: lobbyNewGameAction
        }
        $rootScope.eventChannel = {
            listeners: [],
            listen: function(o) {
                this.listeners.push(o);
            },
            recieve: function(e) {
                for (var i = 0; i < this.listeners.length; i++) {
                    var l = this.listeners[i];
                    l(e);
                }
            }
        };

        init();

        function init() {

            $rootScope.eventChannel.listen(eventDispatcher);

            $scope.ws = PekoeWebsocketService.ws();

            eventHandlers.set("me.meeoo.server.event.PekoeListGameEvent", eventHandler_listGames);
            eventHandlers.set("me.meeoo.server.event.PekoeNewGameEvent", eventHandler_newGame);
            eventHandlers.set("me.meeoo.server.event.PekoeNewPlayerEvent", null);
            eventHandlers.set("me.meeoo.server.event.PekoeStartGameEvent", null);
        }

        $scope.joinGame = function(game) {
            console.log("joining game", game);
        }

        function eventDispatcher(event) {
            var eventHandler = eventHandlers.get(event.name);
            if (eventHandler == null) {
                console.log("Could not dispatch unkown event", event.name, event);
            } else {
                console.log("WEBSOCKET->event", event);
                eventHandler(event);
            }
            $scope.$apply();
        }

        function eventHandler_listGames(event) {
            $scope.lobby.games = event.games;

            $scope.lobby.games.push({
                creation: 1234716846854231,
                visualHash: "af51e2",
                name: "ola quetal",
                playerCount: 2,
                isStarted: true
            })

            $scope.lobby.games.push({
                creation: 123185168454152,
                visualHash: "fa4587",
                name: "The roxorz",
                playerCount: 1,
                isStarted: false
            })
            console.log("Got the list",  $scope.lobby.games);
        }

        function eventHandler_newGame(event) {
            console.log("new game...", event);
        }

        function lobbyNewGameAction() {
            console.log($scope.ws)
            PekoeWebsocketService.newGame("game_name", $scope.lobby.playerName);
        }

    }]);