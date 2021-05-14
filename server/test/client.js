import {io} from 'socket.io-client'
const URI = 'http://localhost:3000'
const ioClient = io.connect(URI)

ioClient.emit('hello', "world")
