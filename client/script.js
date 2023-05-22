const net = require("net");
const port = 3000;
const host = "localhost";

// Create a TCP client
const client = new net.Socket();
let onlineClient = [];

// Connect to the server
client.connect(port, host, async () => {
  console.log("Connected to server");
  // input from the user
  const readline = require("readline").createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  readline.question("What is your username? ", (name) => {
    readline.close();
    const data = {
      state: 0,
      username: name,
    };
    client.write(JSON.stringify(data));
  });
});

// Handle incoming data from the server
client.on("data", (data) => {
  const serialData = JSON.parse(data.toString());
  if (serialData.state === 0) {
    if (serialData.status === 100) {
      console.log(serialData.message);
      goToMenu();
    } else {
      console.log(serialData.message);
      closeSocket();
    }
  } else if (serialData.state === 1) {
    const lengthClient = serialData.clients.length;
    console.log("client connected: ", lengthClient);
    if (lengthClient != 0) {
      for (let i = 0; i < serialData.clients.length; i++) {
        console.log(`${i + 1}. ${serialData.clients[i]}`);
      }
    }
    console.log("================================");
    goToMenu();
  }
});

// Handle connection termination
client.on("close", () => {
  console.log("Connection closed");
});

// ===================== helper function ===================== //

function showMenu() {
  console.log("Welcome to the menu");
  console.log("1. Show online users");
  console.log("2. Send message");
  console.log("3. Exit");
}

function goToMenu() {
  showMenu();
  const readline = require("readline").createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  readline.question("What do you want to do? ", (name) => {
    readline.close();
    if (name == 1) {
      const data = {
        state: 1,
      };
      sendMessage(JSON.stringify(data));
    }
  });
}

function sendMessage(message) {
  client.write(message);
}
