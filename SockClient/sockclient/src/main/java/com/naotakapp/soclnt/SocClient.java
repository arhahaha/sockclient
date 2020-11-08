package com.naotakapp.soclnt;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Scanner;
import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.BufferedReader;

/**
 * Hello world!
 *
 */
public class SocClient 
{
    private static String   hostname = "localhost";
    private static int      port     = 9998;
    private static int      intsize  = 4;

    public static void main( final String[] args) {
        Socket soc = null;
        OutputStream out = null;
        Scanner scan = null;

        try {
            soc = new Socket(hostname, port);
            out = soc.getOutputStream();
            scan = new Scanner(System.in);

            RecvThread rcv = new RecvThread( soc );
            rcv.start();

            while( true ) {

                System.out.println("-------------------");
                System.out.println(" 送信文字列を入力して下さい :> ");
                String line = scan.nextLine();

                if( line.length() == 0 ) {
                    continue;
                }
                if( "end".equals(line) == true ) {
                    break;
                }

                final byte[] strbuf = line.getBytes("UTF-8");

                final ByteBuffer buf = ByteBuffer.allocate(intsize);
                buf.putInt(strbuf.length);
                out.write(buf.array());

                out.write(strbuf);
            }

        } catch (final Exception e) {
            e.printStackTrace();

        } finally {
            try {
                scan.close();
                out.close();
                soc.close();
            } catch (final IOException e) {

            }
        }
    }
}

class RecvThread extends Thread {

    private static int intsize = 4;

    Socket rcvsoc = null;

    public RecvThread( Socket soc ){
        rcvsoc = soc;
    }

    public void run() {

        System.out.println("Client Receive Thread : Thread=[" + getName() + "]");
        InputStream  in = null;
 
        try {
            in = rcvsoc.getInputStream();
            
            while( !rcvsoc.isClosed() ) {

                int     ret    = 0;
                byte[]  lendat = new byte[intsize];
                
                ret = readData( rcvsoc, in, lendat );
                if( ret != lendat.length ){
                    System.out.println("Socket Error [Length Read].");
                    break;
                }
                ByteBuffer buf = ByteBuffer.wrap( lendat );
                buf.order( ByteOrder.BIG_ENDIAN );
                int soclen = buf.getInt(0);
                System.out.println("Socket Data Length : [" + soclen + "].");

                byte[]  bodydat = new byte[soclen];
                ret = readData( rcvsoc, in, bodydat );
                if( ret != bodydat.length ){
                    System.out.println("Socket Error [Body Read].");
                    break;
                }

                String trhsbt = new String( Arrays.copyOfRange( bodydat,  0,  4), "SJIS");
                String trhkbn = new String( Arrays.copyOfRange( bodydat,  4,  6), "SJIS");
                String shname = new String( Arrays.copyOfRange( bodydat,  6, 38), "SJIS");
                String sprice = new String( Arrays.copyOfRange( bodydat, 38, 49), "UTF-8");
                String plathm = new String( Arrays.copyOfRange( bodydat, 49, 65), "UTF-8");
                String shflag = new String( Arrays.copyOfRange( bodydat, 65, 66), "UTF-8");

                System.out.println("Socket Data Body : 取引種別=[" + trhsbt +
                                                         "] 区分=[" + trhkbn +
                                                         "] 名称=[" + shname +
                                                         "] 価格=[" + sprice +
                                                         "] 媒体=[" + plathm +
                                                         "] フラグ=[" + shflag + "]");
            }

        } catch ( Exception e ) {
            e.printStackTrace();

        } finally {
            try {
                if( in != null ) {
                    in.close();
                }
                if( rcvsoc != null ) {
                    rcvsoc.close();
                }
            } catch ( final IOException e ) { 
            }
        }   
        System.out.println("Client RecvThread Terminate : [" + getName() + "]");
    }

    private static int readData( Socket soc, InputStream in, byte[] data ) throws IOException
    {
        int     datsiz = data.length;
        int     total  = 0;

        while( !soc.isClosed() ) {

            int ret = in.read( data, total, datsiz - total );
            if( ret <= 0 ){
                return -1;
            }
            total += ret;

            if( total >= datsiz ){
                break;
            }
        }
        return total;
    }
}

