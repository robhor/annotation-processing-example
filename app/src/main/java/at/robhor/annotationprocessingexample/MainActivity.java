package at.robhor.annotationprocessingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.title)
    private TextView titleView;

    @BindView(R.id.text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = (TextView) findViewById(R.id.title);
        textView = (TextView) findViewById(R.id.text);

        titleView.setText("Annotation Processing Example");
        textView.setText("The old way - calling findViewById manually");
    }
}
