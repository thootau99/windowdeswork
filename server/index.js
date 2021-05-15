import {createServer} from 'http'
import {Server, Socket} from 'socket.io'

const httpServer = createServer();
const io = new Server(httpServer, {});

io.on("connection", (socket) => {
  socket.on("sendMessage", (arg) => {
    console.log("someome sended message", arg)
    socket.broadcast.emit("getMessage", arg)
  })
})


httpServer.listen(3000)