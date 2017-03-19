package com.akristic.www.tkrally;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import java.util.Locale;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
    }
    public void webAddressClicked(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kvalitetnaskolatenisa.com"));
        startActivity(browserIntent);
    }
    public void callPhoneNumber(View v) {
        Uri number = Uri.parse("tel:385959034421");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }
    public void sendEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:info@revoloop.hr"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void openGoogleMaps (View v){
        String uri = String.format(Locale.ENGLISH, "https://www.google.hr/maps/place/Ul.+Rebro+46,+10360,+Sesvete" +
                "/@45.8307781,16.1005225,17z/data=!3m1!4b1!4m5!3m4!1s0x476679d3557012a5:0x9817f11323b9c44b!8m2!3d45.8307781!4d16.1027112?hl=en");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}
