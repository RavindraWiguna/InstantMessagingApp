const net = require("net");
const port = 3000;

// Create a TCP client
const client = new net.Socket();

// Connect to the server
const host = "localhost";
client.connect(port, host, () => {
  console.log("Connected to server");

  // Send data to the server
  client.write("Hello from client");
});

// Handle incoming data from the server
client.on("data", (data) => {
  console.log(`Received data from server: ${data}`);

  // Close the connection
  client.end();
});

// Handle connection termination
client.on("close", () => {
  console.log("Connection closed");
});
