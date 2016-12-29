package com.simplememo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

/**
 * Created by USER on 2016-11-29.
 */
public class Dlg {

	public void dlg(Context context) {
		// Toast.makeText(this, "저장할?", Toast.LENGTH_SHORT).show();
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
		alert_confirm
				.setMessage("저장 하시겠습니까?")
				.setCancelable(false)
				.setPositiveButton("저장",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Positive
							}
						})
				.setNegativeButton("안함",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Negative
							}
						});
		final AlertDialog alert = alert_confirm.create();
		alert.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
				positive.setFocusable(true);
				positive.setFocusableInTouchMode(true);
				positive.requestFocus();
			}
		});
		alert.show();
	}
}
