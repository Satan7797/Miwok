package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mAudioFocusChange=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||focusChange== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mMediaPlayer.start();
                mMediaPlayer.seekTo(0);
            }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                mMediaPlayer.start();
            }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS)
                releaseMediaPlayer();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_view,container,false);

        mAudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> numbers = new ArrayList<>();

        numbers.add(new Word("weṭeṭṭi","Red",R.drawable.color_red,R.raw.color_red));
        numbers.add(new Word("chokokki","Green",R.drawable.color_green,R.raw.color_green));
        numbers.add(new Word("ṭakaakki","Brown",R.drawable.color_brown,R.raw.color_brown));
        numbers.add(new Word("ṭopoppi","Gray",R.drawable.color_gray,R.raw.color_gray));
        numbers.add(new Word("kululli","Black",R.drawable.color_black,R.raw.color_gray));
        numbers.add(new Word("kelelli","White",R.drawable.color_white,R.raw.color_white));
        numbers.add(new Word("ṭopiisә","Dusty Yellow",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        numbers.add(new Word("chiwiiṭә","Mustard Yellow",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(),numbers,R.color.category_colors);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        assert listView!=null;
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                int result=mAudioManager.requestAudioFocus(mAudioFocusChange, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                Word word=numbers.get(position);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioSource());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mAudioFocusChange);
        }
    }


}
