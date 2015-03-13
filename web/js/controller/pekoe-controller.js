'use strict';

angular.module('PekoeApp').controller('PekoeController', ['$scope', '$rootScope', 'PekoeWebsocketService', function($scope, $rootScope, PekoeWebsocketService) {

        var eventHandlers = new Map();
        $scope.lobby = {
            games: [],
            gamesCount: "loading..."
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
            eventHandlers.set("me.meeoo.server.event.PekoeNewGameEvent", null);
            eventHandlers.set("me.meeoo.server.event.PekoeNewPlayerEvent", null);
            eventHandlers.set("me.meeoo.server.event.PekoeStartGameEvent", null);

        }

        function eventDispatcher(event) {
            var eventHandler = eventHandlers.get(event.name);
            if (eventHandler == null) {
                console.log("Could not dispatch unkown event", event.name, event);
            } else {
                console.log("dispatching event to", event.name, event);
                eventHandler(event);
            }
            $scope.$apply();
        }

        function eventHandler_listGames(event) {
            console.log("Got the list");
            $scope.lobby.games = event.games;
            $scope.lobby.gamesCount = event.gamesCount;
        }

    }]);