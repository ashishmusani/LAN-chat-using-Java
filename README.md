Attached file
-------------
1) server.java - command line based server that works as intermediate between two clients by receiving data from one sender and broadcasting it to all other receivers connected to the server
2) client.java - Java swing GUI program that runs on each independent client and is connected to the server program running on a terminal window.


How to run this code?
--------------------
1) download the files and save it in a folder.
2) compile server.java and client.java using 'javac filename.java' command.
3) open first cmd/terminal window with present working directory as the one where you have saved these downloaded files
4) type 'java server' and press enter. This will start ur server(will not show any output except when a new client is connected).
5) open second cmd/terminal window with present working directory as the one where you have saved these downloaded files.
6) type 'java client' and press enter. This will start ur first client GUI window. Enter necessary details and press 'join chat' button. now your first client is connected to server. the server will print a line in terminal when this connection is established.
7) open thir cmd/terminal window with present working directory as the one where you have saved these downloaded files.
8) type 'java client' and press enter. This will start ur second client GUI window. Enter necessary details and press 'join chat' button. now your second client is connected to server. the server will print a line in terminal when this connection is established.

Now the connected client can send and receive messages by using features in the GUI program..

The given program supports upto 10 clients(can be changed by changing just one line of code).
There are few bugs which can be cleared out easily e.g.removing that client from list which gets disconnected.

Email : ashish_musani@ymail.com
