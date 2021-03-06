package fr.lyonjug.kittenmash;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BestKittenActivity extends Activity {

	private static final int FIGHT_REQUEST_CODE = 1;
	private int[] kittenResIds;
	private ImageView catImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_best_kitten);

		findViewById(R.id.fightButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fight();
			}
		});

		catImageView = (ImageView) findViewById(R.id.catImageView);

		kittenResIds = new int[] { R.drawable.kitten_0, //
				R.drawable.kitten_1, //
				R.drawable.kitten_2, //
				R.drawable.kitten_3, //
				R.drawable.kitten_4,//
				R.drawable.kitten_5, //
				R.drawable.kitten_6, //
				R.drawable.kitten_7, //
				R.drawable.kitten_8, //
				R.drawable.kitten_9 //
		};

		loadWinningCat();

	}

	protected void fight() {
		Intent intent = new Intent(this, KittenFightActivity.class);

		Random random = new Random();
		int kitten1 = kittenResIds[random.nextInt(kittenResIds.length)];
		int kitten2 = kittenResIds[random.nextInt(kittenResIds.length)];

		intent.putExtra(KittenFightActivity.KITTEN1_EXTRA, kitten1);
		intent.putExtra(KittenFightActivity.KITTEN2_EXTRA, kitten2);

		startActivityForResult(intent, FIGHT_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FIGHT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				int winExtra = data.getIntExtra(KittenFightActivity.WIN_EXTRA, R.drawable.kitten_0);
				saveWinningCat(winExtra);
				catImageView.setImageResource(winExtra);
			}
		}
	}

	private void saveWinningCat(final int winningCat) {
		new Thread() {
			@Override
			public void run() {
				SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
				preferences.edit().putInt("catResId", winningCat).commit();
			};
		}.start();
	}

	private void loadWinningCat() {
		new Thread() {
			@Override
			public void run() {
				SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
				int winningCat = preferences.getInt("catResId", R.drawable.kitten_0);
				updateWinningCat(winningCat);
			};
		}.start();
	}

	protected void updateWinningCat(final int winningCat) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				catImageView.setImageResource(winningCat);
			}
		});
	}
}
