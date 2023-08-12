package com.example.tppy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.example.tppy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PyVm.initVM();

        AssetManager assetManager = getAssets();
        try {

            String[] pythons = assetManager.list("python");
            if (pythons == null) {
                Toast.makeText(this, "没有python代码", Toast.LENGTH_SHORT).show();
            } else {

                String mainSource = "";
                for (String python : pythons) {

                    InputStream input = assetManager.open("python" + File.separator + python);
                    int size = input.available();
                    byte[] buffer = new byte[size];
                    input.read(buffer);
                    input.close();
                    String text = new String(buffer, StandardCharsets.UTF_8);
                    if (python.contains("main")) {
                        mainSource = text;
                    } else {
                        String mName = python.split("\\.")[0];
                        PyVm.lazyModules(text, mName);
                    }
                    String a = "";
                }
                PyVm.exec(mainSource);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}