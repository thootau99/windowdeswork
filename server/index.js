"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __spreadArray = (this && this.__spreadArray) || function (to, from) {
    for (var i = 0, il = from.length, j = to.length; i < il; i++, j++)
        to[j] = from[i];
    return to;
};
exports.__esModule = true;
var http_1 = require("http");
var socket_io_1 = require("socket.io");
//建立伺服器
var IP = "0.0.0.0";
var httpServer = http_1.createServer();
var io = new socket_io_1.Server(httpServer, {});
var onlineUser = [];
io.on("connection", function (socket) {
    onlineUser = __spreadArray(__spreadArray([], onlineUser), [{ id: socket.id, name: "default name" }]); // add user object to onlineUser
    var updateUser = function () {
        socket.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
        socket.broadcast.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
    };
    console.log('Online user:', onlineUser);
    updateUser();
    socket.on("sendMessage", function (arg) {
        console.log("someome sended message", arg); //顯示有人重送訊息在terminal上
        socket.broadcast.emit("getMessage", arg); //廣播發送給所有client"getMessage"
    });
    socket.on("changeName", function (arg) {
        onlineUser = onlineUser.map(function (user) { return user.id === socket.id ? __assign(__assign({}, user), { name: arg }) : user; }); // update username
        console.log(socket.id + " has change name to " + arg);
        updateUser(); // update the change result to all clients.
    });
    socket.on("guessNumber", function (arg) {
        var argToJson = JSON.parse(arg);
        socket.broadcast.to(argToJson.id).emit("guessNumber", argToJson.number);
        console.log(argToJson.id + " is guessing " + argToJson.number);
    });
    socket.on("disconnect", function () {
        onlineUser = onlineUser.filter(function (user) { return user.id !== socket.id; });
        console.log("User " + socket.id + " has disconnected.");
        updateUser();
    });
});
//Socket.io serve at port 3000
httpServer.listen(3000, IP, function () {
    console.log("Server listening on " + IP);
});
