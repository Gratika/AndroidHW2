package com.gatikahome.androidhw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ISaveMessage {
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS=1;
    private static boolean POST_NOTIFICATIONS_GRANTED =false;

    // Идентификатор канала
    private static String CHANNEL_ID = "MyApp channel";
    private EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.message);
        message.setEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_edit){
                showDialog();
                return true;
        }else{
            if (id==R.id.action_send){
                //перевіряємо чи маємо ми дозвіл надсилати сповіщення
                int permissionStatus = ContextCompat.checkSelfPermission(
                        this, Manifest.permission.POST_NOTIFICATIONS);
                //якщо маємо
                if(permissionStatus== PackageManager.PERMISSION_GRANTED){
                    sendNotification();
                }else{
                    //якщо не маємо, то запрашуємо цей дозвіл
                    //викликаючи відповідне вікно
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_CODE_POST_NOTIFICATIONS);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {

        CreateMessageDialogFragment dialog = new CreateMessageDialogFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    @Override
    public void saveMessage(String new_message) {
        message.setText(new_message);

    }
   // @SuppressLint("MissingPermission")
    private void sendNotification(){
        // Создаём уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_action_near_me)
                .setContentTitle(getString(R.string.header_notification))
                .setContentText(message.getText())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(message.getContext());
        notificationManager.notify(NOTIFY_ID, builder.build());

    }
    @Override
    //метод, що опрацьовує результат, отриманий з діалогового вікна на дозвіл надсилати повіщення
    //requestCode  - це код запросу, який ми встановлювали як 3-й параметр в ActivityCompat.requestPermissions()
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                POST_NOTIFICATIONS_GRANTED = true;
            }
        }
        if(POST_NOTIFICATIONS_GRANTED){
            sendNotification();
        }
        else{
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }
}