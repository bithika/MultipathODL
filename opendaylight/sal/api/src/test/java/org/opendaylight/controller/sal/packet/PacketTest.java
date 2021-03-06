
/*
 * Copyright (c) 2013-2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.sal.packet;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.opendaylight.controller.sal.utils.IPProtocols;

public class PacketTest {

    @Test
    public void testDeserialize() throws NoSuchFieldException, Exception {
        short startOffset, numBits;

        Ethernet eth = new Ethernet();
        byte[] data = { 10, 12, 14, 20, 55, 69, //DMAC
                -90, -20, -100, -82, -78, -97, //SMAC
                8, 6, //ethype
                0, 1, // hw type
                8, 0, // proto (ip)
                6, // hw addr len
                4, // proto addr len
                0, 1, // op codes
                -90, -20, -100, -82, -78, -97, //src hw addr
                9, 9, 9, 1, // src proto
                0, 0, 0, 0, 0, 0, // target hw addr
                9, 9, 9, -2 }; // target proto

        startOffset = 0;
        numBits = 42 * 8;
        eth.deserialize(data, startOffset, numBits);

        byte[] dMAC = eth.getDestinationMACAddress();
        byte[] sMAC = eth.getSourceMACAddress();
        short etherType = eth.getEtherType();

        Assert.assertTrue(dMAC[0] == 10);
        Assert.assertTrue(dMAC[1] == 12);
        Assert.assertTrue(dMAC[2] == 14);
        Assert.assertTrue(dMAC[3] == 20);
        Assert.assertTrue(dMAC[4] == 55);
        Assert.assertTrue(dMAC[5] == 69);

        Assert.assertTrue(sMAC[0] == -90);
        Assert.assertTrue(sMAC[1] == -20);
        Assert.assertTrue(sMAC[2] == -100);
        Assert.assertTrue(sMAC[3] == -82);
        Assert.assertTrue(sMAC[4] == -78);
        Assert.assertTrue(sMAC[5] == -97);

        Assert.assertTrue(etherType == 0x806);

        ARP arpPacket = (ARP) eth.getPayload();

        Assert.assertTrue(arpPacket.getHardwareType() == (byte)0x1);
        Assert.assertTrue(arpPacket.getProtocolType() == 2048);
        Assert.assertTrue(arpPacket.getHardwareAddressLength() == (byte)0x6);
        Assert.assertTrue(arpPacket.getProtocolAddressLength() == (byte)0x4);
        Assert.assertTrue(arpPacket.getOpCode() == 1);

        byte[] senderHwAddress = arpPacket.getSenderHardwareAddress();
        byte[] senderProtocolAddress = arpPacket.getSenderProtocolAddress();

        byte[] targetHwAddress = arpPacket.getTargetHardwareAddress();
        byte[] targetProtocolAddress = arpPacket.getTargetProtocolAddress();


        Assert.assertTrue(senderHwAddress[0] == (byte)0xA6);
        Assert.assertTrue(senderHwAddress[1] == (byte)0xEC);
        Assert.assertTrue(senderHwAddress[2] == (byte)0x9C);
        Assert.assertTrue(senderHwAddress[3] == (byte)0xAE);
        Assert.assertTrue(senderHwAddress[4] == (byte)0xB2);
        Assert.assertTrue(senderHwAddress[5] == (byte)0x9F);

        Assert.assertTrue(senderProtocolAddress[0] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[1] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[2] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[3] == (byte)0x1);

        Assert.assertTrue(targetHwAddress[0] == (byte)0x0);
        Assert.assertTrue(targetHwAddress[1] == (byte)0x0);
        Assert.assertTrue(targetHwAddress[2] == (byte)0x0);
        Assert.assertTrue(targetHwAddress[3] == (byte)0x0);
        Assert.assertTrue(targetHwAddress[4] == (byte)0x0);
        Assert.assertTrue(targetHwAddress[5] == (byte)0x0);

        Assert.assertTrue(senderProtocolAddress[0] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[1] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[2] == (byte)0x9);
        Assert.assertTrue(senderProtocolAddress[3] == (byte)0x1);

        Assert.assertTrue(targetProtocolAddress[0] == (byte)0x9);
        Assert.assertTrue(targetProtocolAddress[1] == (byte)0x9);
        Assert.assertTrue(targetProtocolAddress[2] == (byte)0x9);
        Assert.assertTrue(targetProtocolAddress[3] == (byte)0xFE);
    }

    @Test
    public void testSerialize() throws NoSuchFieldException, Exception {
        Ethernet eth = new Ethernet();
        Map<String, byte[]> fCValues = eth.hdrFieldsMap;

        byte[] dMAC = { 10, 12, 14, 20, 55, 69 };
        byte[] sMAC = { 82, 97, 109, 117, 127, -50 };
        short etherType = 2054;

        byte[] dMACdata, sMACdata, etherTypedata;
        byte[] data = new byte[20];

        eth.setDestinationMACAddress(dMAC);
        eth.setSourceMACAddress(sMAC);
        eth.setEtherType(etherType);

        dMACdata = fCValues.get("DestinationMACAddress");
        sMACdata = fCValues.get("SourceMACAddress");
        etherTypedata = fCValues.get("EtherType");

        Assert.assertTrue(dMACdata[0] == 10);
        Assert.assertTrue(dMACdata[1] == 12);
        Assert.assertTrue(dMACdata[2] == 14);
        Assert.assertTrue(dMACdata[3] == 20);
        Assert.assertTrue(dMACdata[4] == 55);
        Assert.assertTrue(dMACdata[5] == 69);

        Assert.assertTrue(sMACdata[0] == 82);
        Assert.assertTrue(sMACdata[1] == 97);
        Assert.assertTrue(sMACdata[2] == 109);
        Assert.assertTrue(sMACdata[3] == 117);
        Assert.assertTrue(sMACdata[4] == 127);
        Assert.assertTrue(sMACdata[5] == -50);

        Assert.assertTrue(etherTypedata[0] == 8);
        Assert.assertTrue(etherTypedata[1] == 6);
        data = eth.serialize();

        Assert.assertTrue(data[0] == 10);
        Assert.assertTrue(data[1] == 12);
        Assert.assertTrue(data[2] == 14);
        Assert.assertTrue(data[3] == 20);
        Assert.assertTrue(data[4] == 55);
        Assert.assertTrue(data[5] == 69);

        Assert.assertTrue(data[6] == 82);
        Assert.assertTrue(data[7] == 97);
        Assert.assertTrue(data[8] == 109);
        Assert.assertTrue(data[9] == 117);
        Assert.assertTrue(data[10] == 127);
        Assert.assertTrue(data[11] == -50);

        Assert.assertTrue(data[12] == 8);
        Assert.assertTrue(data[13] == 6);

    }

    @Test
    public void testGetMatch() throws Exception {
        TCP tcp = new TCP();
        short sport = (short) 11093;
        short dport = (short) 23;
        tcp.setSourcePort(sport);
        tcp.setDestinationPort(dport);

        IPv4 ip = new IPv4();
        InetAddress sourceAddress = InetAddress.getByName("192.168.100.100");
        InetAddress destintationAddress = InetAddress.getByName("192.168.100.101");
        byte protocol = IPProtocols.TCP.byteValue();
        byte tos = 5;
        ip.setVersion((byte) 4);
        ip.setIdentification((short) 5);
        ip.setDiffServ(tos);
        ip.setECN((byte) 0);
        ip.setTotalLength((short) 84);
        ip.setFlags((byte) 2);
        ip.setFragmentOffset((short) 0);
        ip.setTtl((byte) 64);
        ip.setProtocol(protocol);
        ip.setDestinationAddress(destintationAddress);
        ip.setSourceAddress(sourceAddress);
        ip.setPayload(tcp);

        IEEE8021Q dot1q = new IEEE8021Q();
        byte priority = 4;
        short vlanId = 59;
        short ethType = EtherTypes.IPv4.shortValue();
        dot1q.setPcp(priority);
        dot1q.setVid(vlanId);
        dot1q.setEtherType(ethType);
        dot1q.setPayload(ip);

        Ethernet eth = new Ethernet();
        byte smac[] = { (byte) 0xf0, (byte) 0xde, (byte) 0xf1, (byte) 0x71, (byte) 0x72, (byte) 0x8d };
        byte dmac[] = { (byte) 0xde, (byte) 0x28, (byte) 0xdb, (byte) 0xb3, (byte) 0x7c, (byte) 0xf8 };
        eth.setDestinationMACAddress(dmac);
        eth.setSourceMACAddress(smac);
        eth.setEtherType(EtherTypes.VLANTAGGED.shortValue());
        eth.setPayload(dot1q);

        Match match = eth.getMatch();

        Assert.assertTrue(Arrays.equals(smac, (byte[]) match.getField(MatchType.DL_SRC).getValue()));
        Assert.assertTrue(Arrays.equals(dmac, (byte[]) match.getField(MatchType.DL_DST).getValue()));
        Assert.assertEquals(priority, (byte) match.getField(MatchType.DL_VLAN_PR).getValue());
        Assert.assertEquals(vlanId, (short) match.getField(MatchType.DL_VLAN).getValue());
        Assert.assertEquals(ethType, (short) match.getField(MatchType.DL_TYPE).getValue());
        Assert.assertEquals(sourceAddress, match.getField(MatchType.NW_SRC).getValue());
        Assert.assertEquals(destintationAddress, match.getField(MatchType.NW_DST).getValue());
        Assert.assertEquals(protocol, (byte) match.getField(MatchType.NW_PROTO).getValue());
        Assert.assertEquals(tos, (byte) match.getField(MatchType.NW_TOS).getValue());
        Assert.assertEquals(sport, (short) match.getField(MatchType.TP_SRC).getValue());
        Assert.assertEquals(dport, (short) match.getField(MatchType.TP_DST).getValue());
    }
}
