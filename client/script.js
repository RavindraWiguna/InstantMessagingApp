const net = require("net");
const port = 1235;
const host = "localhost";

// Create a TCP client
const client = new net.Socket();
let messages = [];
let messagePrivate = {};

let username = "";
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
    username = name;
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
    console.log("====== Online Users ======");
    console.log("client online: ", lengthClient);
    if (lengthClient != 0) {
      for (let i = 0; i < serialData.clients.length; i++) {
        console.log(`${i + 1}. ${serialData.clients[i]}`);
      }
    }
    console.log("==========================");
    goToMenu();
  } else if (serialData.state === 2) {
    console.log("\n====== Public Message ======");
    console.log(`${serialData.sender} : ${serialData.message}`);
    console.log("============================");
    messages.push(serialData);
  } else if (serialData.state === 3) {
    console.log("\n====== Private Message ======");
    console.log(`${serialData.sender} : ${serialData.message}`);
    console.log("============================");
    if (messagePrivate[serialData.sender] == undefined) {
      messagePrivate[serialData.sender] = [];
    }
    const msg = {
      message: serialData.message,
      sender: serialData.sender,
    };
    messagePrivate[serialData.sender].push(msg);
  }
});

// Handle connection termination
client.on("close", () => {
  console.log("Connection closed");
});

// ===================== helper function ===================== //

const readline = require("readline");

function showMenu() {
  console.log("Welcome to the menu");
  console.log("1. Show online users");
  console.log("2. Send public message");
  console.log("3. Show public message");
  console.log("4. Send private message");
  console.log("5. Show private message");
  console.log("6. Exit");
}

function promptInput(question) {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  return new Promise((resolve) => {
    rl.question(question, (input) => {
      rl.close();
      resolve(input);
    });
  });
}

async function goToMenu() {
  showMenu();
  console.log("-----------------");
  const choose = await promptInput("Enter your choice: ");

  if (choose === "1") {
    const data = {
      state: 1,
    };
    sendMessage(JSON.stringify(data));
  } else if (choose === "2") {
    const message = await promptInput("Enter your message: ");
    const data = {
      state: 2,
      message: message,
    };
    console.log("Message sent, waiting for response");
    sendMessage(JSON.stringify(data));
    goToMenu();
  } else if (choose === "3") {
    console.log("================================");
    console.log("OPEN PUBLIC CHAT");
    console.log("--------------------------------");
    for (let i = 0; i < messages.length; i++) {
      console.log(`${messages[i].sender} : ${messages[i].message}`);
      console.log("--------------------------------");
    }
    console.log("================================");
    goToMenu();
  } else if (choose === "4") {
    console.log("choose user to send message");
    const user = await promptInput("Enter username: ");
    const message = await promptInput("Enter your message: ");
    const data = {
      state: 3,
      message: message,
      receiver: user,
    };
    if (messagePrivate[user] == undefined) {
      messagePrivate[user] = [];
    }
    const msg = {
      message: message,
      sender: "me",
    };
    messagePrivate[user].push(msg);
    sendMessage(JSON.stringify(data));
    goToMenu();
  } else if (choose === "5") {
    console.log("================================");
    console.log("OPEN PRIVATE CHAT");
    console.log("--------------------------------");
    if (Object.keys(messagePrivate).length === 0) {
      console.log("No message");
    } else {
      // show listed user
      for (const key in messagePrivate) {
        console.log(`- ${key}`);
      }
      const uname = await promptInput("username: ");
      if (messagePrivate[uname] == undefined) {
        console.log("No message");
      } else {
        for (const key in messagePrivate[uname]) {
          console.log(
            `${messagePrivate[uname][key]["sender"]} : ${messagePrivate[uname][key]["message"]}`
          );
          console.log("--------------------------------");
        }
      }
    }
    console.log("================================");
    goToMenu();
  } else if (choose === "6") {
    console.log("Exiting...");
    closeSocket();
  } else {
    console.log("Invalid choice. Please try again.");
    goToMenu();
  }
}

function sendMessage(message) {
  client.write(message);
}
