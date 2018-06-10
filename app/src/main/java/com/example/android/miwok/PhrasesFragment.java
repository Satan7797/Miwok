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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_view,container,false);
        mAudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> numbers = new ArrayList<>();

        numbers.add(new Word("minto wuksus","Where are you going?",R.raw.phrase_where_are_you_going));
        numbers.add(new Word("tinnә oyaase'nә","What is your name?",R.raw.phrase_what_is_your_name));
        numbers.add(new Word("oyaaset...","My name is...",R.raw.phrase_my_name_is));
        numbers.add(new Word("michәksәs?","How are you feeling?", R.raw.phrase_how_are_you_feeling));
        numbers.add(new Word("kuchi achit","I'm feeling good.",R.raw.phrase_im_feeling_good));
        numbers.add(new Word("әәnәs'aa?","Are you coming?",R.raw.phrase_are_you_coming));
        numbers.add(new Word("hәә’ әәnәm","Yes, I'm coming.",R.raw.phrase_yes_im_coming));
        numbers.add(new Word("әәnәm","I'm coming.",R.raw.phrase_im_coming));
        numbers.add(new Word("yoowutis","Let's go.",R.raw.phrase_lets_go));
        numbers.add(new Word("әnni'nem","Come here.",R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(),numbers, R.color.category_phrases);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        assert listView != null;
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

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
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
