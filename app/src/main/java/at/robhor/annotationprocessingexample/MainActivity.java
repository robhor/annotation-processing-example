package at.robhor.annotationprocessingexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.robhor.annotationprocessingexample.about.AboutActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.about_button)
    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewBinder.bind(this);

        titleView.setText("Annotation Processing Example");
        textView.setText("TextView bound using generated binder class");

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
