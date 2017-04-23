package at.robhor.annotationprocessingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewBinder.bind(this);

        titleView.setText("Annotation Processing Example");
        textView.setText("TextView bound using generated binder class");
    }
}
