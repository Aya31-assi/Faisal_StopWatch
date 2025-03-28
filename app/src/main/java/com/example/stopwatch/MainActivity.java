package com.example.stopwatch;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private Button btnStart, btnStop, btnReset;
    private Handler handler;
    private StopWatch stopwatch;
    private Runnable updateTimerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        stopwatch = new StopWatch();
        handler = new Handler(Looper.getMainLooper());

        updateTimerThread = new Runnable() {
            @Override
            public void run() {
                updateDisplay();
                handler.postDelayed(this, 1000);
            }
        };

        if (savedInstanceState != null) {
            stopwatch.setState(
                    savedInstanceState.getLong("startTime"),
                    savedInstanceState.getLong("timeSwapBuff"),
                    savedInstanceState.getBoolean("isRunning")
            );

            if (stopwatch.isRunning()) {
                handler.post(updateTimerThread);
                updateButtonStates(false, true, true);
            } else if (stopwatch.getUpdateTime() > 0) {
                updateButtonStates(true, false, true);
            }
            updateDisplay();
        } else {
            updateButtonStates(true, false, false);
        }

        btnStart.setOnClickListener(v -> {
            stopwatch.start();
            handler.post(updateTimerThread);
            updateButtonStates(false, true, true);
        });

        btnStop.setOnClickListener(v -> {
            stopwatch.stop();
            handler.removeCallbacks(updateTimerThread);
            updateButtonStates(true, false, true);
        });

        btnReset.setOnClickListener(v -> {
            stopwatch.reset();
            updateDisplay();
            handler.removeCallbacks(updateTimerThread);
            updateButtonStates(true, false, false);
        });
    }

    //Sets up buttons and text fields
    private void initViews() {
        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
    }

    @SuppressLint("DefaultLocale")
    private void updateDisplay() {
        long time = stopwatch.getUpdateTime();
        int secs = (int) (time / 1000);
        int minute = secs / 60;
        secs %= 60;
        tvTime.setText(String.format("%02d:%02d", minute, secs));
    }

    private void updateButtonStates(boolean start, boolean stop, boolean reset) {
        btnStart.setEnabled(start);
        btnStop.setEnabled(stop);
        btnReset.setEnabled(reset);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startTime", stopwatch.getStartTime());
        outState.putLong("timeSwapBuff", stopwatch.getTimeSwapBuff());
        outState.putBoolean("isRunning", stopwatch.isRunning());
    }
}
