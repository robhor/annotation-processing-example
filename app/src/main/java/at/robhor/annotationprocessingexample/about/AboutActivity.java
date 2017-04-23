package at.robhor.annotationprocessingexample.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import at.robhor.annotationprocessingexample.BindView;
import at.robhor.annotationprocessingexample.R;

public class AboutActivity extends AppCompatActivity {
    public static final String GITHUB_URL = "https://github.com/robhor/annotation-processing-example";

    @BindView(R.id.github_icon)
    public ImageView githubIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ViewBinder.bind(this);

        githubIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
                startActivity(browserIntent);
            }
        });
    }
}
