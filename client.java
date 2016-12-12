import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.Scanner;
import java.io.*;
import java.net.*;



public class client
{
    static Scanner in = new Scanner(System.in);
    static DataOutputStream dos;
    static DataInputStream dis;
    static Socket s;
    static boss server;
    static String username;


    //-------For File sharing------------
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    static OutputStream os = null;
    static InputStream is = null;
    static FileOutputStream fos = null;
    static BufferedOutputStream bos = null;
    static String FILE_TO_RECEIVE;
    static int bytesRead;
    static int current = 0;


    //-------------------------------





    static JTextPane chatMessages = new JTextPane();
    static JScrollPane JPchatMessages = new JScrollPane(chatMessages);

    static String msgHistory = new String("");


    public static void main(final String args[]) throws IOException
    {
            JFrame frame1 = new JFrame("Login to LAN Chat & File Sharing");
            JFrame frame2 = new JFrame("LAN Chat & File Sharing");
            JFrame frame3 = new JFrame("Choose a file to send");


//----------------------------------------------------------------
//---------------------- FRAME 2----------------------------------
//----------------------------------------------------------------

            frame2.setSize(500,700);
            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.setLayout(new GridBagLayout());


            JLabel helloUser = new JLabel("Hello and Welcome !");
            frame2.add(helloUser, new GridBagConstraints(0,0,1,1,3,1,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));


            JButton logOutButton = new JButton("Logout");
            frame2.add(logOutButton, new GridBagConstraints(2,0,1,1,.25,1,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));


            chatMessages.setEditable(false);
            frame2.add(JPchatMessages, new GridBagConstraints(0,1,3,1,1.0,100.0,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));



            JTextField message = new JTextField(20);
            frame2.add(message, new GridBagConstraints(0,2,1,1,.5,1,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));



            JButton send = new JButton("send");
            frame2.add(send, new GridBagConstraints(1,2,1,1,.25,.25,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            send.addActionListener(new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent sendButtonClick)
                                        {
                                            String msg = message.getText();
                                            message.setText(null);
                                            try
                                            {
                                                dos.writeUTF(username + " : " + msg);
                                            }
                                            catch(IOException e){}
                                        }
                                    });



            JButton sendFile = new JButton("Send File");
            frame2.add(sendFile, new GridBagConstraints(2,2,1,1,.25,.25,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            sendFile.addActionListener(new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent sendFileButtonClick)
                                        {
                                            frame3.setVisible(true);
                                        }
                                    });
//----------------------------------------------------------------
//---------------------- FRAME 2 ends ----------------------------
//----------------------------------------------------------------



//----------------------------------------------------------------
//------------------------ FRAME 1 -------------------------------
//----------------------------------------------------------------

            frame1.setLayout(new GridBagLayout());

            frame1.setSize(500,700);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            //----For bg image-----------
            JLabel background=new JLabel(new ImageIcon("logo.png"),JLabel.CENTER);
            frame1.add(background, new GridBagConstraints(0,0,1,1,2.0,1.0,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            //---------------------------



            //------Label-----------------
            JLabel enter = new JLabel("Enter your name");
            frame1.add(enter, new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.CENTER,            GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
            //----------------------------


            //------Username Textfield-------
            JTextField usernameTextArea = new JTextField(10);
            frame1.add(usernameTextArea, new GridBagConstraints(0,3,1,1,1.0,1.0,GridBagConstraints.CENTER,            GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
            //----------------------------


            //-------Login Button-------------
            JButton login = new JButton("Join chat");
            frame1.add(login, new GridBagConstraints(0,4,1,1,1.0,1.0,GridBagConstraints.CENTER,            GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));

            login.addActionListener(new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent buttonClick)
                                        {
                                            username = usernameTextArea.getText();
                                            helloUser.setText("Hello "+username+". Welcome !");
                                            frame1.setVisible(false);

                                            try
                                            {
                                                s = new Socket("localhost", 7777);
                                                dos = new DataOutputStream(s.getOutputStream());
                                                dis = new DataInputStream(s.getInputStream());

                                                server = new boss(dis);
                                                Thread t = new Thread(server);
                                                t.start();

                                                frame2.setVisible(true);
                                            }

                                            catch(IOException e)
                                            {
                                                System.out.println("Server unavailable to connect. Press Ctrl+C to exit..");
                                            }
                                        }
                                    });

            //-------------------------------


            frame1.setVisible(true);
//----------------------------------------------------------------
//---------------------- FRAME 1 ends ----------------------------
//----------------------------------------------------------------



//----------------------------------------------------------------
//---------------------- FRAME 3 ---------------------------------
//----------------------------------------------------------------
            frame3.setSize(500,700);
            frame3.setLayout(new GridBagLayout());


            JFileChooser fileSelector = new JFileChooser();
            frame3.add(fileSelector, new GridBagConstraints(1,0,1,1,.25,.25,GridBagConstraints.CENTER,            GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            fileSelector.addActionListener(new ActionListener()
                                    {
                                        public void actionPerformed(ActionEvent fileChooserEvent)
                                        {
                                            String command = fileChooserEvent.getActionCommand();
                                            if(command.equals(JFileChooser.APPROVE_SELECTION))
                                            {
                                                File fileToBeSent = fileSelector.getSelectedFile();
                                                frame3.setVisible(false);

                                                try{
                                                    byte [] fileByteArray  = new byte [(int)fileToBeSent.length()];


                                                    fis = new FileInputStream(fileToBeSent);
                                                    bis = new BufferedInputStream(fis);
                                                    bis.read(fileByteArray,0,fileByteArray.length);
                                                    os = s.getOutputStream();
                                                    dos.flush();
                                                    os.flush();
                                                    dos.flush();
                                                    dos.writeUTF("46511231dsfdsfsd#@$#$#@^$%#@*$#^");
                                                    os.write(fileByteArray,0,fileByteArray.length);
                                                    os.flush();
                                                    System.out.println("Done.");
                                                    updateMessageArea("File Successfully Sent.");

                                                    if (os != null)
                                                        os.close();


                                                }
                                                catch(Exception e){}

                                            }
                                        }
                                    });



//----------------------------------------------------------------
//----------------------------------------------------------------
//----------------------------------------------------------------


    }



    public static void updateMessageArea(String msg)
    {
        msgHistory = msgHistory + "\n";
        msgHistory = msgHistory + msg;
        chatMessages.setText(msgHistory);
    }


    public static void reconnect()
    {
        try
        {
            s.close();
            s = new Socket("192.168.0.101", 7777);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            server = new boss(dis);
            Thread newConnection = new Thread(server);
            newConnection.start();

        }
        catch(Exception e)
        {
            System.out.println("Exception caught in reconnect().");
        }
    }



    public static void receiveFile()
    {
        String workingDir = System.getProperty("user.dir");
        String FILE_TO_RECEIVED = workingDir+ "/imageReceived.jpg";
        int FILE_SIZE = 70000;
        int bytesRead;
        int current = 0;

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = s.getInputStream();
            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            bos.write(mybytearray, 0 , current);
            bos.close();
            System.out.println("File " + FILE_TO_RECEIVED+ " downloaded (" + current + " bytes read)");
            }
        catch(Exception e)
        {
            System.out.println("Exception caught in receiveFile().");

        }

    }


}



class boss extends Thread
{
    DataInputStream disServer;
    String secretCode = new String("46511231dsfdsfsd#@$#$#@^$%#@*$#^");

    public boss(DataInputStream z)
    {
        disServer = z;
    }

    public void run()
    {



        while(true)
        {
            try
            {
                String str = disServer.readUTF();
                if(str.equals(secretCode))
                {
                    client.receiveFile();
                }
                else
                    client.updateMessageArea(str);
            }

            catch(IOException e)
            {
                System.out.println("Exception in run method. Reconnecting..");

                client.reconnect();
                break;
            }

        }
    }
}
