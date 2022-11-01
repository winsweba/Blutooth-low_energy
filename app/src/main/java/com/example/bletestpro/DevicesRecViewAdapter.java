package com.example.bletestpro;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DevicesRecViewAdapter extends RecyclerView.Adapter<DevicesRecViewAdapter.ViewHolder> {
    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList;

    private Context context;
    public DevicesRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        holder.bleName.setText(bluetoothDeviceArrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceArrayList.size();
    }

    public void setBluetoothDeviceArrayList(BluetoothDevice bluetoothDeviceArrayList) {
        bluetoothDeviceArrayList = bluetoothDeviceArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bleName;
        private TextView bleDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bleName = itemView.findViewById(R.id.ble_name);
            bleDetails = itemView.findViewById(R.id.ble_detail);
        }
    }
}
