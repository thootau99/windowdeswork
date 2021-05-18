import {createServer} from 'http'
import {Server, Socket} from 'socket.io'
//建立伺服器
const httpServer = createServer();
const io = new Server(httpServer, {});

io.on("connection", (socket) => {//建立連線
  socket.on("sendMessage", (arg) => { //監聽來自client的"sendMessage"事件
    console.log("someome sended message", arg)//顯示有人重送訊息在terminal上
    socket.broadcast.emit("getMessage", arg)//廣播發送給所有client"getMessage"
  })
})

//指定聆聽port 3000
httpServer.listen(3000)