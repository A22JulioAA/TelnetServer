package org.example;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class InterfacesInfo {
    public static void main(String[] args) {
        try {


            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();

                byte[] mac = netInterface.getHardwareAddress();
                String macAddress = (mac != null) ? formatMacAddress(mac) : "Not available";

                System.out.println("-----------------------------------------------");
                System.out.println("Interface: " + netInterface.getName());
                System.out.println("\t Is up: " + netInterface.isUp());
                System.out.println("\t Is loopback: " + netInterface.isLoopback());
                System.out.println("\t Is virtual: " + netInterface.isVirtual());
                System.out.println("\t MAC Address: " + macAddress);

                System.out.println("\t Addresses: ");
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    System.out.println("\t\t- " + address.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static String formatMacAddress (byte[] mac) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
        }

        return sb.toString();
    }
}