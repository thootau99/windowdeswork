import {createServer} from 'http'
import {Server, Socket} from 'socket.io'

//建立伺服器
const IP = "0.0.0.0"
const httpServer = createServer();
const io = new Server(httpServer, {});
let onlineUser = []
let battles = []
function getSelf(id, onlineUser) {
  return onlineUser.filter(user => user.id === id)
}


io.on("connection", (socket) => {//建立連線


  onlineUser = [...onlineUser, {id: socket.id, name: "default name"}] // add user object to onlineUser
  
  const updateUser = () => { // Function that update the onlineUser list to all clients.
    socket.emit("receiveOnlineUserList", JSON.stringify(onlineUser)); 
    socket.broadcast.emit("receiveOnlineUserList", JSON.stringify(onlineUser));
  } 

  console.log('Online user:', onlineUser)
  updateUser()

  socket.on("sendMessage", (arg) => { //監聽來自client的"sendMessage"事件
    console.log("someome sended message", arg) //顯示有人重送訊息在terminal上
    socket.broadcast.emit("getMessage", arg) //廣播發送給所有client"getMessage"
  })

  socket.on("sendImage", (arg) => { //監聽來自client的"sendMessage"事件
    console.log("someome sended Image", arg) //顯示有人重送訊息在terminal上
    socket.broadcast.emit("getImage", arg) //廣播發送給所有client"getMessage"
  })

  socket.on("setMusic", (arg) => { //監聽來自client的"sendMessage"事件
    console.log("someome set Music", arg) //顯示有人重送訊息在terminal上
    socket.broadcast.emit("changeMusic", arg) //廣播發送給所有client"getMessage"
  })

  socket.on("changeName", (arg) => {
    onlineUser = onlineUser.map(user => user.id === socket.id ? {...user, name: arg} : user) // update username
    console.log(`${socket.id} has change name to ${arg}`)
    updateUser() // update the change result to all clients.
  })

  socket.on("guessNumber", (arg) => { // 發起猜數字挑戰
    const argToJson = JSON.parse(arg); // parse arg : string => arg : jsonObject
    const argWillSend = {
      battleid: `${socket.id}${argToJson.id}`,  // id of the battle room
      p1: socket.id, // player1's id
      p2: argToJson.id, // player2's id
      number: argToJson.number, // number to guess 
      finished: false, // 是否結束
      winner: '',
      players: [{ team1: [socket.id] }, { team2: [argToJson.id] }] // players 分兩組
    }
    socket.broadcast.emit("guessNumber", JSON.stringify(argWillSend)); // broadcast 給 user, 更新房間清單
    battles = [...battles, argWillSend] // 在 server 上存 battle
    console.log(argWillSend)
    console.log(`${argToJson.id} is guessing ${argToJson.number}`)
  })

  socket.on("finishBattle", (arg) => { // 戰鬥結束
    const result = JSON.parse(arg) // parse result : string => result : jsonObject
    result.battle = JSON.parse(result.battle) // parse battle : string => battle : jsonObject
    battles = battles.map(battle => { // change the status of battle which saved in server
      if (battle.battleid === result.battle.battleid) { 
        return {
          ...battle, // expand battle
          winner: result.result, //change winner in battle
          finished: true // change finished in battle
        }
      } else return battle // 若 id 不同就跳過
    })
    console.log("battle finished...", battles, result)
    socket.broadcast.emit("setBattleList", JSON.stringify(battles)) // 更新給其他 users 知道戰鬥結束
  })

  socket.on("getBattleList", () => {
    console.log("All user updating battlelist...")
    socket.broadcast.emit("setBattleList", JSON.stringify(battles)) // 更新 battle room 清單
    socket.emit("setBattleList", JSON.stringify(battles))
  })

  socket.on("disconnect", () => {
    onlineUser = onlineUser.filter(user => user.id !== socket.id)
    console.log(`User ${socket.id} has disconnected.`)
    updateUser();
  })

})

//Socket.io serve at port 3000
httpServer.listen(3000, IP, () => {
  console.log(`Server is listening on ${IP}`)
})