package com.example.lap10715.demoweek3.demo_live_fb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class FBLiveReactionActivity extends AppCompatActivity {

    private Subscription emoticonSubscription;
    private Subscriber subscriber;
    private final int MINIMUM_DURATION_BETWEEN_EMOTICONS = 300;

    // Emoticons views.
    private Animation emoticonClickAnimation;
    private ImageView likeEmoticonButton ;
    private ImageView loveEmoticonButton ;
    private ImageView hahaEmoticonButton ;
    private ImageView wowEmoticonButton ;
    private ImageView sadEmoticonButton;
    private ImageView angryEmoticonButton;
    private EmoticonsView emoticonsView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the view and do all the necessary init.
        setContentView(R.layout.activity_fb_live);

        likeEmoticonButton = findViewById(R.id.like_emoticon);
        loveEmoticonButton =  findViewById(R.id.love_emoticon);
        hahaEmoticonButton = findViewById(R.id.haha_emoticon);
        wowEmoticonButton = findViewById(R.id.wow_emoticon);
        sadEmoticonButton = findViewById(R.id.sad_emoticon);
        angryEmoticonButton = findViewById(R.id.angry_emoticon);
        emoticonsView = findViewById(R.id.custom_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Create an instance of FlowableOnSubscribe which will convert click events to streams
        FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
            @Override
            public void subscribe(final FlowableEmitter emitter) throws Exception {
                convertClickEventToStream(emitter);
            }
        };
        //Give the backpressure strategy as BUFFER, so that the click items do not drop.
        Flowable emoticonsFlowable = Flowable.create(flowableOnSubscribe,
                BackpressureStrategy.BUFFER);
        //Convert the stream to a timed stream, as we require the timestamp of each event
        Flowable<Timed> emoticonsTimedFlowable = emoticonsFlowable.timestamp();
        subscriber = getSubscriber();
        //Subscribe
        emoticonsTimedFlowable.subscribeWith(subscriber);
    }

    private Subscriber getSubscriber() {
        return new Subscriber<Timed<Emoticons>>() {
            @Override
            public void onSubscribe(Subscription s) {
                emoticonSubscription = s;
                emoticonSubscription.request(1);

                // for lazy evaluation.
                emoticonsView.initView(FBLiveReactionActivity.this);

            }

            @Override
            public void onNext(final Timed<Emoticons> timed) {

                emoticonsView.addView(timed.value());

                long currentTimeStamp = System.currentTimeMillis();
                long diffInMillis = currentTimeStamp - ((Timed) timed).time();
                if (diffInMillis > MINIMUM_DURATION_BETWEEN_EMOTICONS) {
                    emoticonSubscription.request(1);
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emoticonSubscription.request(1);
                        }
                    }, MINIMUM_DURATION_BETWEEN_EMOTICONS - diffInMillis);
                }
            }

            @Override
            public void onError(Throwable t) {
                //Do nothing
            }

            @Override
            public void onComplete() {
                if (emoticonSubscription != null) {
                    emoticonSubscription.cancel();
                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (emoticonSubscription != null) {
            emoticonSubscription.cancel();
        }
    }


    private void convertClickEventToStream(final FlowableEmitter emitter) {
        likeEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(likeEmoticonButton, emitter, Emoticons.LIKE);
            }
        });

        loveEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(loveEmoticonButton, emitter, Emoticons.LOVE);
            }
        });

        hahaEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(hahaEmoticonButton, emitter, Emoticons.HAHA);
            }
        });

        wowEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(wowEmoticonButton, emitter, Emoticons.WOW);
            }
        });

        sadEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(sadEmoticonButton, emitter, Emoticons.SAD);
            }
        });

        angryEmoticonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnClick(angryEmoticonButton, emitter, Emoticons.ANGRY);
            }
        });
    }

    private void doOnClick(View view, FlowableEmitter emitter, Emoticons emoticons) {
        emoticonClickAnimation =
                AnimationUtils.loadAnimation(FBLiveReactionActivity
                .this, R.anim.emoticon_click_animation);
        view.startAnimation(emoticonClickAnimation);
        emitter.onNext(emoticons);
    }
}
