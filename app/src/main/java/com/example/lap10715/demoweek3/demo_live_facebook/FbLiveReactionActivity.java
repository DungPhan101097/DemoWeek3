package com.example.lap10715.demoweek3.demo_live_facebook;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.lap10715.demoweek3.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Timed;

public class FbLiveReactionActivity extends AppCompatActivity {

    private Subscription emoticonSubscription;
    private Subscriber subscriber;
    private final int MINIMUM_DURATION_BETWEEN_EMOTICONS = 100;

    private ImageView likeBtn, loveBtn, wowBtn, hahaBtn, sadBtn, angryBtn;
    private Animation animationEmo;
    private EmoticonsView emoticonsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_live);

        initViews();

        startFlowable();
    }

    private void startFlowable() {

        // Flowable tu dong goi ham subcribe()
        Flowable emoFlowable = Flowable.create(
                (FlowableOnSubscribe) emitter -> {
                    likeBtn.setOnClickListener(v -> {
                        doOnClick(likeBtn, emitter, Emoticons.LIKE);
                    });

                    loveBtn.setOnClickListener(v -> {
                        doOnClick(loveBtn, emitter, Emoticons.LOVE);
                    });

                    hahaBtn.setOnClickListener(v -> {
                        doOnClick(hahaBtn, emitter, Emoticons.HAHA);
                    });

                    wowBtn.setOnClickListener(v -> {
                        doOnClick(wowBtn, emitter, Emoticons.WOW);
                    });

                    sadBtn.setOnClickListener(v -> {
                        doOnClick(sadBtn, emitter, Emoticons.SAD);
                    });

                    angryBtn.setOnClickListener(v -> {
                        doOnClick(angryBtn, emitter, Emoticons.ANGRY);
                    });
                },
                BackpressureStrategy.BUFFER);

        Flowable<Timed> emoTimedFlowable = emoFlowable.timestamp();

        subscriber = getSubscriber();

        emoTimedFlowable.subscribe(subscriber);
    }

    private Subscriber getSubscriber() {
        return new Subscriber<Timed<Emoticons>>() {

            @Override
            public void onSubscribe(Subscription s) {
                emoticonSubscription = s;
                emoticonSubscription.request(1);

                emoticonsView.initView(FbLiveReactionActivity.this);
            }

            @Override
            public void onNext(Timed<Emoticons> emoticonsTimed) {
                emoticonsView.addView(emoticonsTimed.value());

                long currentTimeStamp = System.currentTimeMillis();
                long diffInMillis = currentTimeStamp - (emoticonsTimed).time();
                if (diffInMillis > MINIMUM_DURATION_BETWEEN_EMOTICONS) {
                    emoticonSubscription.request(1);
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(() ->
                            emoticonSubscription.request(1),
                            MINIMUM_DURATION_BETWEEN_EMOTICONS - diffInMillis);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                if(emoticonSubscription != null)
                    emoticonSubscription.cancel();
            }
        };

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(emoticonSubscription != null)
            emoticonSubscription.cancel();
    }


    private void doOnClick(ImageView imageView, FlowableEmitter emitter, Emoticons emoticons) {
        animationEmo = AnimationUtils.loadAnimation(FbLiveReactionActivity.this,
                R.anim.emoticon_click_animation);
        imageView.startAnimation(animationEmo);

        emitter.onNext(emoticons);
    }

    private void initViews() {
        likeBtn = findViewById(R.id.like_emoticon);
        loveBtn = findViewById(R.id.love_emoticon);
        hahaBtn = findViewById(R.id.haha_emoticon);
        wowBtn = findViewById(R.id.wow_emoticon);
        sadBtn = findViewById(R.id.sad_emoticon);
        angryBtn = findViewById(R.id.angry_emoticon);
        emoticonsView = findViewById(R.id.custom_view);
    }




}
