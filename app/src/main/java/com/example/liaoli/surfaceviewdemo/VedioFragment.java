package com.example.liaoli.surfaceviewdemo;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


public class VedioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "VedioFragment";
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MediaPlayer mediaPlayer;


    public VedioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VedioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VedioFragment newInstance(String param1, String param2) {
        VedioFragment fragment = new VedioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vedio, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (!EasyPermissions.hasPermissions(getContext(), permissions)) {

            EasyPermissions.requestPermissions(this, "", 1, permissions);

        }

        mediaPlayer = new MediaPlayer();

        String path = Environment.getExternalStorageDirectory() + File.separator + "a.mp4";
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setLooping(true);
            SurfaceHolder holder = surfaceView.getHolder();


            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });



            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    int sW = surfaceView.getWidth();
                    int sH = surfaceView.getHeight();
                    int vW = mp.getVideoWidth();
                    int vH = mp.getVideoHeight();





                    float ratio =  vW*1.0f/vH;

                    int sfW,sfH;

                    if(ratio > 1){
                        sfW = sW;
                        sfH = (int) (sfW / ratio);
                    }else{
                        sfW = sW;
                        sfH = (int) (sfW / ratio);
                    }


                    mp.setVideoScalingMode(1);




                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(sfW,sfH);

                    layoutParams.gravity = Gravity.CENTER;
                    surfaceView.setLayoutParams(layoutParams);

                    mediaPlayer.start();
                }
            });
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mediaPlayer.setDisplay(holder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    Log.e(TAG, "surfaceChanged --->format = " + format + ", width = " + width + ",height = " + height);

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
