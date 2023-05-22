const net = require("net");
const port = 1235;
const clientNames = ["rama", "suka", "mendol", "rere", "sama", "afiq", "apin"];
/*
  socket: socket
  username: ""
*/
let clientSockets = [];

/*
  state
  0 = login
  1 = online user
  2 = msg public
  3 = msg private
*/

/*
  status code
  100 = login success
  101 = login fail
*/

// Create a TCP server
const server = net.createServer((socket) => {
  console.log("Client connected");
  // Handle incoming data from the client
  let username = "";
  socket.on("data", (data) => {
    const serialData = JSON.parse(data.toString());
    if (serialData.state === 0) {
      const state = 0;
      if (clientNames.includes(serialData.username)) {
        const data = {
          state: state,
          status: 100,
          message: "Login success",
        };
        const dataSocket = {
          socket: socket,
          username: serialData.username,
        };
        username = serialData.username;
        clientSockets.push(dataSocket);
        console.log("total socket: ", clientSockets.length);
        socket.write(JSON.stringify(data));
      } else {
        const data = {
          state: state,
          status: 101,
          message: "Login fail",
        };
        socket.write(JSON.stringify(data));
      }
    } else if (serialData.state === 1) {
      // send all connected clients
      let connectedClient = [];
      for (let i = 0; i < clientSockets.length; i++) {
        if (username != clientSockets[i].username) connectedClient.push(clientSockets[i].username);
      }
      const msg = {
        state: 1,
        clients: connectedClient,
      };
      socket.write(JSON.stringify(msg));
    } else if (serialData.state === 2) {
      // send to all connected clients
      const msg = {
        state: 2,
        message: serialData.message,
        sender: username,
      };
      sendToAll(JSON.stringify(msg));
    } else if (serialData.state === 3) {
    }
  });

  // Handle client disconnection
  socket.on("close", () => {
    console.log("Client disconnected");
    deleteSocket({
      socket: socket,
      username: username,
    });
  });

  socket.on("error", (err) => {
    console.log("Client error", err.stack);
    deleteSocket({
      socket: socket,
      username: username,
    });
  });
});

// Start the server and listen on a specific port
server.listen(port, () => {
  console.log(`Server started and listening on port ${port}`);
});

// ===================== helper function ===================== //

/*
  s :{
    socket: socket,
    username: ""
  }
*/

function deleteSocket(s) {
  for (let i = 0; i < clientSockets.length; i++) {
    if (clientSockets[i].socket === s.socket) {
      console.log(`socket ${s.username} deleted`);
      clientSockets.splice(i, 1);
      console.log("total socket ", clientSockets.length);
      break;
    }
  }
}

function sendToAll(message) {
  for (let i = 0; i < clientSockets.length; i++) {
    clientSockets[i].socket.write(message);
  }
}
