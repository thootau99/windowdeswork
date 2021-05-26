import { createServer } from 'http';
import { Server } from 'socket.io';
//建立伺服器
const IP = "0.0.0.0";
const httpServer = createServer();
const io = new Server(httpServer, {});
let onlineUser = [];
io.on("connection", (socket) => {
    onlineUser = [...onlineUser, { id: socket.id, name: "default name" }]; // add user object to onlineUser
    const updateUser = () => {
        socket.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
        socket.broadcast.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
    };
    console.log('Online user:', onlineUser);
    updateUser();
    socket.on("sendMessage", (arg) => {
        console.log("someome sended message", arg); //顯示有人重送訊息在terminal上
        socket.broadcast.emit("getMessage", arg); //廣播發送給所有client"getMessage"
    });
    socket.on("changeName", (arg) => {
        onlineUser = onlineUser.map(user => user.id === socket.id ? { ...user, name: arg } : user); // update username
        console.log(`${socket.id} has change name to ${arg}`);
        updateUser(); // update the change result to all clients.
    });
    socket.on("guessNumber", (arg) => {
        const argToJson = JSON.parse(arg);
        socket.broadcast.to(argToJson.id).emit("guessNumber", argToJson.number);
        console.log(`${argToJson.id} is guessing ${argToJson.number}`);
    });
    socket.on("disconnect", () => {
        onlineUser = onlineUser.filter(user => user.id !== socket.id);
        console.log(`User ${socket.id} has disconnected.`);
        updateUser();
    });
});
//Socket.io serve at port 3000
httpServer.listen(3000, IP, () => {
    console.log(`Server is listening on ${IP}`);
});
