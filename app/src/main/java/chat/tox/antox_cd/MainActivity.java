package chat.tox.antox_cd;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import org.jetbrains.annotations.NotNull;

import im.tox.tox4j.core.ToxCore;
import im.tox.tox4j.core.callbacks.ToxCoreEventListener;
import im.tox.tox4j.core.enums.ToxConnection;
import im.tox.tox4j.core.enums.ToxFileControl;
import im.tox.tox4j.core.enums.ToxMessageType;
import im.tox.tox4j.core.enums.ToxUserStatus;
import im.tox.tox4j.core.exceptions.ToxBootstrapException;
import im.tox.tox4j.core.options.ProxyOptions;
import im.tox.tox4j.core.options.SaveDataOptions;
import im.tox.tox4j.core.options.ToxOptions;
import im.tox.tox4j.impl.jni.ToxCoreImpl;

public class MainActivity extends AppCompatActivity
{

    ToxOptions _tox_options = null;
    ToxCore _toxcore = null;
    byte[] _own_address = new byte[100];
    byte[] _own_publickey = new byte[100];
    int _own_nospam = 0;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ProxyOptions po = new ProxyOptions.None$();
        SaveDataOptions saveData = new SaveDataOptions.None$();

        // boolean ipv6Enabled, boolean udpEnabled, boolean localDiscoveryEnabled,
        // ProxyOptions proxy, int startPort, int endPort, int tcpPort,
        // SaveDataOptions saveData, boolean fatalErrors
        _tox_options = new ToxOptions(false, false, true, po, 1024, 65000, 33449, saveData, false);
        _toxcore = new ToxCoreImpl(_tox_options);

        context = this.getBaseContext();

        new Thread(new Runnable()
        {
            public void run()
            {
                _tox_connect();

                _own_address = _toxcore.getAddress();
                _own_publickey = _toxcore.getPublicKey();
                _own_nospam = _toxcore.getNospam();
                System.out.println("my ToxAddress=" + bin2hex(_own_address));
                System.out.println("my Publickey =" + bin2hex(_own_publickey));
                System.out.println("my NoSpam    =" + _own_nospam);

                Object o = new Object();
                ToxCoreEventListener evl = new ToxCoreEventListener()
                {
                    @Override
                    public Object fileChunkRequest(int i, int i1, long l, int i2, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object fileRecv(int i, int i1, int i2, long l, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object fileRecvChunk(int i, int i1, long l, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object fileRecvControl(int i, int i1, @NotNull ToxFileControl toxFileControl, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendConnectionStatus(int i, @NotNull ToxConnection toxConnection, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendLosslessPacket(int i, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendLossyPacket(int i, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendMessage(int i, @NotNull ToxMessageType toxMessageType, int i1, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendName(int i, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendReadReceipt(int i, int i1, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendRequest(@NotNull byte[] bytes, int i, @NotNull byte[] bytes1, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendStatus(int i, @NotNull ToxUserStatus toxUserStatus, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendStatusMessage(int i, @NotNull byte[] bytes, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object friendTyping(int i, boolean b, Object o)
                    {
                        return null;
                    }

                    @Override
                    public Object selfConnectionStatus(@NotNull ToxConnection toxConnection, Object o)
                    {
                        System.out.println("selfConnectionStatus changed to:" + toxConnection.toString());
                        return null;
                    }
                };

                int _toxcore_interval = 0;
                while (true)
                {
                    _toxcore.iterate(evl, o);
                    _toxcore_interval = _toxcore.iterationInterval();
                    System.out.println("_toxcore_interval=" + _toxcore_interval);
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    void _tox_connect()
    {
        // ToxPublicKey.fromString("");
        // scalaz.$bslash$div<im.tox.core.error.CoreError, byte[]> ret = ToxPublicKey.fromString("");

        byte[] _key = null;

        String _ip = "192.168.0.20";
        int _port = 33447;
        String string_key = "578E5F044C98290D0368F425E0E957056B30FB995F53DEB21C3E23D7A3B4E679";

        try
        {
            _toxcore.bootstrap(_ip, _port, hex2bin(string_key));
            _toxcore.addTcpRelay(_ip, _port, hex2bin(string_key));
        }
        catch (ToxBootstrapException e)
        {
            e.printStackTrace();
        }

        // ------------------------------------ bootstrap ------------------------
        // ------------------------------------ bootstrap ------------------------
        // ------------------------------------ bootstrap ------------------------

        /*
        {"178.62.250.138",             33445, "788236D34978D1D5BD822F0A5BEBD2C53C64CC31CD3149350EE27D4D9A2F9B6B", {0}},
        {"2a03:b0c0:2:d0::16:1",       33445, "788236D34978D1D5BD822F0A5BEBD2C53C64CC31CD3149350EE27D4D9A2F9B6B", {0}},
        {"tox.zodiaclabs.org",         33445, "A09162D68618E742FFBCA1C2C70385E6679604B2D80EA6E84AD0996A1AC8A074", {0}},
        {"163.172.136.118",            33445, "2C289F9F37C20D09DA83565588BF496FAB3764853FA38141817A72E3F18ACA0B", {0}},
        {"2001:bc8:4400:2100::1c:50f", 33445, "2C289F9F37C20D09DA83565588BF496FAB3764853FA38141817A72E3F18ACA0B", {0}},
        {"128.199.199.197",            33445, "B05C8869DBB4EDDD308F43C1A974A20A725A36EACCA123862FDE9945BF9D3E09", {0}},
        {"2400:6180:0:d0::17a:a001",   33445, "B05C8869DBB4EDDD308F43C1A974A20A725A36EACCA123862FDE9945BF9D3E09", {0}},
        {"biribiri.org", 33445, "F404ABAA1C99A9D37D61AB54898F56793E1DEF8BD46B1038B9D822E8460FAB67", {0}}
        */
        _ip = "biribiri.org";
        _port = 33445;
        string_key = "F404ABAA1C99A9D37D61AB54898F56793E1DEF8BD46B1038B9D822E8460FAB67";

        try
        {
            System.out.println("bootstrap:01");
            _toxcore.bootstrap(_ip, _port, hex2bin(string_key));
            System.out.println("bootstrap:tcp:01");
            _toxcore.addTcpRelay(_ip, _port, hex2bin(string_key));
        }
        catch (ToxBootstrapException e)
        {
            e.printStackTrace();
        }


        _ip = "163.172.136.118";
        _port = 33445;
        string_key = "2C289F9F37C20D09DA83565588BF496FAB3764853FA38141817A72E3F18ACA0B";

        try
        {
            System.out.println("bootstrap:02");
            _toxcore.bootstrap(_ip, _port, hex2bin(string_key));
            System.out.println("bootstrap:tcp:02");
            _toxcore.addTcpRelay(_ip, _port, hex2bin(string_key));
        }
        catch (ToxBootstrapException e)
        {
            e.printStackTrace();
        }

        System.out.println("bootstrap:ready.");

        // ------------------------------------ bootstrap ------------------------
        // ------------------------------------ bootstrap ------------------------
        // ------------------------------------ bootstrap ------------------------

        //        def bootstrap (address:String, port:Port, publicKey:ToxPublicKey):
        //        Unit = {tox.bootstrap(address, port, publicKey)
        //                tox.addTcpRelay(address, port, publicKey)

    }

    String bin2hex(byte[] in)
    {
        String out = "";
        int i = 0;
        for (i = 0; i < in.length; i++)
        {
            out = out + Integer.toHexString(in[i] & 0xff).toUpperCase();
        }
        return out;
    }

    // convert 32 bytes from hex-string to byte[]
    // convert 32 bytes from hex-string to byte[]
    // convert 32 bytes from hex-string to byte[]
    byte[] hex2bin(String in)
    {
        byte[] out = new byte[in.length() / 2]; // new byte[32];
        int j = 0;
        for (int i = 0; i < in.length(); i = i + 2)
        {
            out[i / 2] = (byte) getByte(in, i);
            // System.out.println("i=" + i + " c1=" + in.charAt(i) + " c2=" + in.charAt(i + 1) + " i/2=" + (i / 2) + " int=" + getByte(in, i) + " byte=" + (byte) getByte(in, i));
        }
        return out;
    }

    public static int getHexDigit(char c)
    {
        if (c >= '0' && c <= '9')
        {
            return c - '0';
        }

        if (c >= 'A' && c <= 'F')
        {
            return c - 'A' + 10;
        }

        // dummy, should never get here!!
        return 0;
    }

    public static int getByte(String s, int ind)
    {
        return (getHexDigit(s.charAt(ind)) << 4) | (getHexDigit(s.charAt(ind + 1)));
    }
    // convert 32 bytes from hex-string to byte[]
    // convert 32 bytes from hex-string to byte[]
    // convert 32 bytes from hex-string to byte[]

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
