import java.net.InetAddress;

import jpcap.*;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class capture
{
    public static int req=0,reply=0,okping=0;
    public static ArrayList<ICMPPacket>echo_packets=new ArrayList<>();
    public static ArrayList<ICMPPacket>req_packets=new ArrayList<>();
    public static HashMap<String,Integer> connections=new HashMap<>();
    public static JpcapCaptor captor;
    public static void main(String[] args) throws java.io.IOException{
        //Get the Device information - Start

        //Obtain the list of network interfaces

        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        //for each network interface
        for (int i = 0; i < devices.length; i++) {
            //print out its name and description
            System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

            //print out its MAC address
            System.out.print(" MAC address:");
            for (byte b : devices[i].mac_address)
                System.out.print(Integer.toHexString(b&0xff) + ":");
            System.out.println();

            //print out its IP address, subnet mask and broadcast address
            for (NetworkInterfaceAddress a : devices[i].addresses)
                System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
        }
        //Get the Device information - End

//Capture the packets

        System.out.println("\n \n ");
        System.out.println("Please Enter the Device Number to Capture the Packet");
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        if(a <= devices.length)
        {
            int index=a;  // set index of the interface that you want to open.

            //Open an interface with openDevice(NetworkInterface intrface, int snaplen, boolean promics, int to_ms)
            captor=JpcapCaptor.openDevice(devices[index], 65535, false, 20);
            captor.setFilter("icmp",true);
            captor.loopPacket(-1,new PacketPrinter());
//            captor.close();
//                for(int i=0;i<50;i++){
//                    Packet temp=captor.getPacket();
//                    if (temp!=null){
//                        System.out.println("data: "+ temp.data);
//                        System.out.println("datalink: "+temp.datalink);
//                        System.out.println("header: "+temp.header);
//                        System.out.println("total: "+temp);
//                    }
//
//                    //capture a single packet and print it out
//    //                System.out.println(captor.getPacket());
//                }

        }
        else
            System.out.println("Please Enter the correct value");
    }
}