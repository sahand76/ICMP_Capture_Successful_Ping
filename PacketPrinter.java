import jpcap.PacketReceiver;
import jpcap.packet.*;

import java.util.ArrayList;
import java.util.Scanner;

public class PacketPrinter implements PacketReceiver {
    //this method is called every time Jpcap captures a packet
    public void receivePacket(Packet pkt) {
        System.out.println("So far "+capture.req+" request and "+capture.reply+" replies ");
        IPPacket pac = (IPPacket) pkt;
        ICMPPacket pac2=(ICMPPacket) pkt;
        if (pac2.type==8){
            capture.req_packets.add(pac2);
            capture.connections.putIfAbsent(pac2.src_ip.toString()+" --> "+pac2.dst_ip.toString(),0);
        }

        else
            for (int i = 0; i <capture.req_packets.size() ; i++) {
                Integer temp=0;
                if (pac2.seq==capture.req_packets.get(i).seq){
                    temp=capture.connections.get(pac2.dst_ip.toString()+" --> "+pac2.src_ip.toString());
                    capture.connections.replace(pac2.dst_ip.toString()+" --> "+pac2.src_ip.toString(),temp,temp+1);
                }

            }
        EthernetPacket pac3= (EthernetPacket) pkt.datalink;
        System.out.println("Src IP: " + pac.src_ip + " Dest IP: " + pac.dst_ip +"\n" + "protocol: "+ pac2.protocol
        + " hop: "+ pac2.hop_limit + " identity: " + pac2.ident+" Seq_Num: "+ pac2.seq + " Type: "+ pac2.type);
        System.out.print("Src MAC: ");
        for (byte b : pac3.src_mac)
            System.out.print(Integer.toHexString(b&0xff) + ":");
        System.out.print(" Dst MAC: ");
        for (byte b : pac3.dst_mac)
            System.out.print(Integer.toHexString(b&0xff) + ":");

        System.out.print("\n"+ "------------------------------------------"+"\n");
        if (pac2.type==0){
            capture.connections.forEach((s, integer) ->
                    System.out.println(integer+" successful ping: "+ s));
            System.out.print("\n"+ "------------------------------------------"+"\n");
        }
        if (pac2.type==0)
            capture.reply++;
        else if(pac2.type==8)
            capture.req++;
    }
}