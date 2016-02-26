package gs.com.youjoypokerconsole;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    TextView mAliveNumText = null;
    TextView mDiffsText = null;

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int aliveNum = bundle.getInt("ALiveNum");
            int Diffs = bundle.getInt("Diffs");

            mAliveNumText.setText(aliveNum+"");
            mDiffsText.setText(Diffs + "");
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAliveNumText = (TextView) this.findViewById(R.id.TextID_inlineNum);
        mDiffsText = (TextView) this.findViewById(R.id.TextID_diffs);

        new MyThread(myHandler).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyConst.RUNABLE = false;
    }
}
