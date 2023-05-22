const net = require("net");
const port = 3000;

// Create a TCP server
const server = net.createServer((socket) => {
  console.log("Client connected");

  // Handle incoming data from the client
  socket.on("data", (data) => {
    console.log(`Received data from client: ${data}`);

    // Send a response back to the client
    socket.write("Server received your message");
  });

  // Handle client disconnection
  socket.on("end", () => {
    console.log("Client disconnected");
  });
});

// Start the server and listen on a specific port
server.listen(port, () => {
  console.log(`Server started and listening on port ${port}`);
});
