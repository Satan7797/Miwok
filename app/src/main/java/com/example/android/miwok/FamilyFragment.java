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
public class FamilyFragment extends Fragment {

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


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_view,container,false);

        mAudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> numbers = new ArrayList<>();

        numbers.add(new Word("әpә","Father",R.drawable.family_father,R.raw.family_father));
        numbers.add(new Word("әṭa","Mother",R.drawable.family_mother,R.raw.family_mother));
        numbers.add(new Word("angsi","Son",R.drawable.family_son,R.raw.family_son));
        numbers.add(new Word("tune","Daughter",R.drawable.family_daughter,R.raw.family_daughter));
        numbers.add(new Word("taachi","Older Brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        numbers.add(new Word("chalitti","Young Brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        numbers.add(new Word("teṭe","Older Sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        numbers.add(new Word("kolliti","Young Sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        numbers.add(new Word("ama","Grandmother",R.drawable.family_grandmother,R.raw.family_grandmother));
        numbers.add(new Word("paapa","Grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(),numbers, R.color.category_family);

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
