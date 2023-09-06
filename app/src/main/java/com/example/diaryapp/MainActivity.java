package com.example.diaryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diaryapp.retrofit.Quotes;
import com.example.diaryapp.retrofit.RetrofitInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ConstraintLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        sendNotification();

        mainLayout = findViewById(R.id.mainLayout);
        imageView = findViewById(R.id.splash_image);
        retrofitQuotes();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
            Toast.makeText(this, "Shake device to turn on/off music", Toast.LENGTH_SHORT).show();
        }, 3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = "DiaryReminderChannel";
        String description = "Channel for MyDiary Reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyDiary", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification() {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 22);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 20*1000 , AlarmManager.INTERVAL_DAY, pendingIntent);
    }
//0 + calendar.getTimeInMillis()
    private void retrofitQuotes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface apiInterface = retrofit.create(RetrofitInterface.class);
        Call<Quotes> currentQuote =  apiInterface.getRandomQuote();

        currentQuote.enqueue(new Callback<Quotes>() {
            @Override
            public void onResponse(Call<Quotes> call, Response<Quotes> response) {
                final Quotes returnedQuote = response.body();
                showSnackBar(returnedQuote);
            }

            @Override
            public void onFailure(Call<Quotes> call, Throwable t) {
            }
        });
    }

    private void showSnackBar(Quotes returnedQuote) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Snackbar gotBar = Snackbar.make(mainLayout, returnedQuote.getQuote(), Snackbar.LENGTH_SHORT);
        gotBar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_color));
        gotBar.show();
    }
}
