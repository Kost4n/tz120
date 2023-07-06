package com.kost4n.tz120.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kost4n.tz120.R;
import com.kost4n.tz120.databinding.FragmentHomeBinding;
import com.kost4n.tz120.room.ScoreDatabase;
import com.kost4n.tz120.room.dao.ScoreDao;
import com.kost4n.tz120.room.entity.Score;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ScoreDao scoreDao;
    DecimalFormat satoshiFormat = new DecimalFormat("#.###");
    DecimalFormat btcFormat = new DecimalFormat("#.########");
    private double scoreSatoshi = 0;
    private double scoreBTC = 0;
    private ProgressBar progressBar;
    private TextView textViewSatoshi;
    private TextView textViewBTC;

    private ImageView server1;
    private ImageView droplet1;
    private TextView speed1;
    private ImageView server2;
    private ImageView droplet2;
    private TextView speed2;
    private ImageView server3;
    private ImageView droplet3;
    private TextView speed3;
    private ImageView server4;
    private ImageView droplet4;
    private TextView speed4;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scoreDao = ScoreDatabase.getInstance(getContext().getApplicationContext()).getScoreDao();

        textViewSatoshi = binding.textScoreSatoshi;
        textViewBTC = binding.btcScore;
        progressBar = binding.progressbar;
        server1 = binding.server1;
        server2 = binding.server2;
        server3 = binding.server3;
        server4 = binding.server4;
        droplet1 = binding.droplet1;
        droplet2 = binding.droplet2;
        droplet3 = binding.droplet3;
        droplet4 = binding.droplet4;
        speed1 = binding.speed1;
        speed2 = binding.speed2;
        speed3 = binding.speed3;
        speed4 = binding.speed4;

        getScore();

        progressBar.setMax(1000);

        binding.buttonBoost.setText(getResources().getString(R.string.button_boost) + "                        " +
                "                                        " +
                getResources().getString(R.string.text_button_boost));

        Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        final boolean[] isPressed = {false, false};
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed[0]) {
                    startScore();
                    isPressed[0] = true;
                    view.startAnimation(animAlpha);
                }
            }
        });
        binding.buttonBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.StandUp)
                        .repeat(1)
                        .duration(700)
                        .playOn(view);
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            animationServer(server1, droplet1, speed4);
                            Thread.sleep(3000);
                            animationServer(server2, droplet2, speed4);
                            Thread.sleep(3000);
                            animationServer(server3, droplet3, speed4);
                            Thread.sleep(3000);
                            animationServer(server4, droplet4, speed4);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
        binding.buttonRaising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(1)
                        .playOn(view);

                sendIntent();
            }
        });

        return root;
    }

    private void animationServer(ImageView server, ImageView droplet, TextView speed) {
        Runnable runnable = new Runnable() {
            public void run() {
                server.post(new Runnable() {
                    @Override
                    public void run() {
                        server.setImageDrawable(null);
                        server.setImageResource(R.drawable.active_server);
                    }
                });
                droplet.post(new Runnable() {
                    @Override
                    public void run() {
                        droplet.setImageDrawable(null);
                        droplet.setBackgroundResource(R.drawable.anim);
                        AnimationDrawable animationDrawable =
                                (AnimationDrawable) droplet.getBackground();
                        animationDrawable.start();
                    }
                });
                speed.post(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {
                        speed.setTextColor(R.color.green_gradient_2);
                    }
                });

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                server.post(new Runnable() {
                    @Override
                    public void run() {
                        server.setImageDrawable(null);
                        server.setImageResource(R.drawable.no_active_server);
                    }
                });
                droplet.post(new Runnable() {
                    @Override
                    public void run() {
                        droplet.setBackground(null);
                        droplet.setImageResource(R.drawable.gray_droplet_1);
                    }
                });
                speed1.post(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {
                        speed.setTextColor(R.color.gray_server);
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void getScore() {
        Runnable runnable = new Runnable() {
            public void run() {
                Score score = scoreDao.getScoreById(1);
                if (score != null) {
                    scoreSatoshi = score.getScore_satoshi();
                    scoreBTC = score.getScore_btc();
                }
                textViewSatoshi.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewSatoshi.setText(satoshiFormat.format(scoreSatoshi) + " " +
                                getResources().getString(R.string.satoshi));
                    }
                });
                textViewBTC.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewBTC.setText(btcFormat.format(scoreBTC) + " " +
                                getResources().getString(R.string.btc));
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private Boolean isHaveScore() {
        final boolean[] isHave = {false};
        Runnable runnable = new Runnable() {
            public void run() {
                Score score = scoreDao.getScoreById(1);
                if (score != null) {
                    isHave[0] = true;
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return isHave[0];
    }

    private void addScore() {
        Runnable runnable = new Runnable() {
            public void run() {
                Score score = new Score(1, scoreSatoshi, scoreBTC);
                scoreDao.addScore(score);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void sendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,
                "https://play.google.com/store/apps/details?id=com.kost4n.tz120" + "\n User balance is " +
                satoshiFormat.format(scoreSatoshi) + " " + getResources().getString(R.string.satoshi)  + ", " +
                btcFormat.format(scoreBTC) + " " + getResources().getString(R.string.btc));
        startActivity(Intent.createChooser(intent, "Share this app"));
    }
    private void updateScore() {
        Runnable runnable = new Runnable() {
            public void run() {
                Score score = new Score(1, scoreSatoshi, scoreBTC);
                scoreDao.updateScore(score);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void startScore() {
        if (scoreSatoshi == 0 && scoreBTC == 0) {
            scoreSatoshi = 15;
            scoreBTC = 0.00000015;
        }
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    scoreSatoshi = scoreSatoshi + (scoreSatoshi * 0.05);
                    scoreBTC = scoreBTC + (scoreBTC * 0.05);

                    textViewSatoshi.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewSatoshi.setText(satoshiFormat.format(scoreSatoshi) + " " +
                                    getResources().getString(R.string.satoshi));
                        }
                    });

                    textViewBTC.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewBTC.setText(btcFormat.format(scoreBTC) + " " +
                                    getResources().getString(R.string.btc));
                        }
                    });

                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((int) scoreSatoshi);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (isHaveScore() == true) {
            updateScore();
        } else {
            addScore();
        }
    }
}
