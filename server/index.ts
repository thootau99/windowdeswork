import {createServer} from 'http'
import {Server, Socket} from 'socket.io'
//建立伺服器
const httpServer = createServer();
const io = new Server(httpServer, {});

interface onlineUserInterface {
  id : string;
  name : string;
}

let onlineUser : onlineUserInterface[] = []


io.on("connection", (socket:Socket) => {//建立連線


  onlineUser = [...onlineUser, {id: socket.id, name: "default name"}] // add user object to onlineUser
  const updateUser = () => {
    socket.emit("receiveOnlineUserList", JSON.stringify(onlineUser)); 
    socket.broadcast.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
  } // Function that 

  console.log('Online user:', onlineUser)
  updateUser()

  socket.on("sendMessage", (arg:string) => { //監聽來自client的"sendMessage"事件
    console.log("someome sended message", arg)//顯示有人重送訊息在terminal上
    socket.broadcast.emit("getMessage", arg)//廣播發送給所有client"getMessage"
  })

  socket.on("changeName", (arg:string) => {
    onlineUser = onlineUser.map(user => user.id === socket.id ? {...user, name: arg} : user) // update username
    console.log(`${socket.id} has change name to ${arg}`)
    updateUser() // update the change result to all clients.
  })

  socket.on("disconnect", () => {
    onlineUser = onlineUser.filter(user => user.id !== socket.id)
    console.log(`User ${socket.id} has connected.`)
  })
})

//指定聆聽port 3000
httpServer.listen(3000)