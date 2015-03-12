'use strict';

angular.module('PekoeApp').controller('PekoeController', ['PekoeWebsocket', '$scope', function(ws, $scope) {

}]);

angular.module('PekoeWebsocket', ['ngWebsocket']).run(
        function($websocket) {
            var ws = $websocket.$new({
                url: 'ws://localhost:8080/pekoe/ws',
                reconnect: true // it will reconnect after 2 seconds
            }); // instance of ngWebsocket, handled by $websocket service

            ws.$on('$open', function() {
                console.log('Oh my gosh, websocket is really open! Fukken awesome!');

                console.log('connecting')
                ws.$emit('me.meeoo.server.event.PekoeListGameEvent', ["", 0]); // send a message to the websocket server

                console.log('pong...')
                // ws.$emit('pong', ['one', 'two', 'three']);
            });

            ws.$on('me.meeoo.server.event.PekoeListGameEvent', function(data) {
                console.log(data);
                var event = {};
                event.name = data.shift();
                event.transactionId = data.shift();
                event.gameCount = data.shift();
                event.games = [];
                for (var i = 0; i < event.gameCount; i++) {
                    var game = {};
                    game.creation = data.shift();
                    game.visualHash = data.shift();
                    game.name = data.shift();
                    game.playerCount = data.shift();
                    game.isStarted = data.shift();
                    event.games.add(game);
                }
                console.log(event);
            });

            ws.$on('pong', function(data) {
                console.log('The websocket server has sent the following data:');
                console.log(data);

                ws.$close();
            });

            ws.$on('$close', function() {
                console.log('Noooooooooou, I want to have more fun with ngWebsocket, damn it!');
            });

            ws.$on('$error', function(error) {
                console.log('An error occured');
                console.log(error);
            });

            ws.$on('$message', function() {
                console.log(message);
            });
        });