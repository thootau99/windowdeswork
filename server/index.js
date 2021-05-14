import {createServer} from 'http'
import {Server, Socket} from 'socket.io'

const httpServer = createServer();
const io = new Server(httpServer, {});

io.on("connection", (socket) => {
  socket.on("hello", (arg) => {
    socket.broadcast.emit("test", `this is a ${arg} message`)
  })
})



httpServer.listen(3000)