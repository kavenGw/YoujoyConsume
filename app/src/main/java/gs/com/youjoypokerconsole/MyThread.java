package gs.com.youjoypokerconsole;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import org.msgpack.MessagePack;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import org.msgpack.type.ValueType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by wucan on 16/2/24.
 */
public class MyThread extends Thread {
    private final String TAG = "MyThread";
    Socket socket = null;

    private Handler mHandler = null;
    public MyThread(Handler handler) {
        super();

        mHandler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            //连接服务器 并设置连接超时为20秒
            socket = new Socket();
            //输入服务器ip
            socket.connect(new InetSocketAddress("XXXXX", 0), 20000);
            Log.d(TAG,"startSocket");
//            Log.d(TAG, String.format("%lu",System.currentTimeMillis()));
//
//            Log.d(TAG, String.format("%lu",System.currentTimeMillis()));
            Log.d(TAG,"endSocket");
            //获取输入输出流
            OutputStream ou = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
//            InputStream inputStream = socket.getInputStream();
            while(MyConst.RUNABLE)
            {
                byte[] writeArr = new byte[6];

//                int value = 4057;
//                byte value1 = (byte) Math.floor(value/128/128/128);
//                byte value2 = (byte)Math.floor(value/128/128 - value1*128);
//                byte value3 = (byte)Math.floor(value/128 - value1*128*128 - value2*128);
//                byte value4 = (byte)(value % 128);撒
                writeArr[0] = 0;
                writeArr[1] = 4;
                writeArr[2] = 0;
                writeArr[3] = 0;
                writeArr[4] = 15;
                writeArr[5] = -39;
                ou.write(writeArr);
                //向服务器发送信息
                ou.flush();

                //读取发来服务器信息
//                int[] arr = new int[100];
//                int index = 0;
//                while ( bff.ready()) {
//                    arr[index] = (bff.read());
//                    index ++;
//                }

                while( true)
                {
                    int count = inputStream.available() ;
                    System.out.print("当前"+count);
                    if(count > 0)
                    {
                        byte[] data = new byte[500];
                        count = inputStream.read(data);
                        MessagePack msgPack = new MessagePack();
                        int msgLen = data[0] * 256 + data[1];
                        MapValue value = msgPack.read(data,6,msgLen-4).asMapValue();
                        Value[] values= value.getKeyValueArray();
                        System.out.println("数量" + values.length);
                        int aliveNum = values[1].asIntegerValue().intValue();
                        int diffsNum = values[5].asIntegerValue().intValue();
                        System.out.print("finishRead" + aliveNum + " " + diffsNum);

                        Message message = new Message();
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("ALiveNum",aliveNum);
                        bundle1.putInt("Diffs",diffsNum);
                        message.setData(bundle1);
                        mHandler.sendMessage(message);
                        break;
                    }

                }
//                if(index > 0)
//                {
//                    MessagePack msgPack = new MessagePack();
////                    Value getValues = msgPack.read(arr);
////                    msgPack.read(socket.getInputStream());
//
//
//
//                    int d = 1;
//                }

                try {
                    sleep(MyConst.REFRESHINTERVAL);
                }catch (InterruptedException e)
                {

                }
            }
            //关闭各种输入输出流
//            bff.close();
            ou.close();
            socket.close();
        } catch (SocketTimeoutException e) {
            //连接超时 在UI界面显示消息
//            bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
//            msg.setData(bundle);
//            //发送消息 修改UI线程中的组件
//            myHandler.sendMessage(msg);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
