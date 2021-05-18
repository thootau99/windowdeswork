import {createServer} from 'http'
import {Server, Socket} from 'socket.io'
//�إߦ��A��
const httpServer = createServer();
const io = new Server(httpServer, {});
interface onlineUserInterface {
  id : string;
  name : string;
}
let onlineUser : onlineUserInterface[] = []
io.on("connection", (socket) => {//�إ߳s�u
  socket.on("sendMessage", (arg) => { //��ť�Ӧ�client��"sendMessage"�ƥ�
    console.log("someome sended message", arg)//��ܦ��H���e�T���bterminal�W
    socket.broadcast.emit("getMessage", arg)//�s���o�e���Ҧ�client"getMessage"
  })
  socket.on("setName", (arg) => {
    socket.broadcast.emit("")
  })
})

//���w��ťport 3000
httpServer.listen(3000)