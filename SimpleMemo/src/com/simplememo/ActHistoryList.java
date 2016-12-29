package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by USER on 2016-12-06.
 */
public class ActHistoryList extends Activity {
	MemoData memoData = MemoData.getInstance();
	ListView lvHistoryList;
	static ActHistoryList act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this;
		setContentView(R.layout.history_list);
		lvHistoryList = (ListView) findViewById(R.id.lvHistoryList);
		reloadHistoryList();
		lvHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				memoData.setNowHistorySelect(position);
				startActivity(new Intent(ActHistoryList.this, ActNowHistoryMemo.class));
			}
		});

		lvHistoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int selPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ActHistoryList.this);
				builder
						.setMessage("삭제 하시겠습니까?\n→ " + arrayAdapter.getItem(selPos) + " ←")
						.setCancelable(false)
						.setPositiveButton("삭제함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										memoData.setNowHistorySelect(selPos);
										memoData.removeNowHistorySelect();
										reloadHistoryList();

									}
								})
						.setNegativeButton("안함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
				builder.setCancelable(true);
				builder.create().show();
				return false;
			}
		});
	}

	ArrayAdapter<String> arrayAdapter;

	private void reloadHistoryList() {
		ArrayList<String> nowHistoryList = (ArrayList<String>) memoData.getNowSelectHistoryList().clone();
		Debug.d("nowHistoryList.size() : " + nowHistoryList.size());
		for (int i = 0; i < nowHistoryList.size(); i++) {
			String nowHistory = nowHistoryList.get(i);
			String memo = nowHistory.split(MemoData.TAG_MEMO_END)[0].split("\n")[0];
			// if (memo.length() > memoData.titleShowLength) memo = memo.substring(0, memoData.titleShowLength) + "...";
			nowHistoryList.set(i, memo);
		}
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_element, nowHistoryList);
		Debug.d("nowHistoryList : " + nowHistoryList);
		Debug.d("lvHistoryList : " + lvHistoryList);
		Debug.d("arrayAdapter : " + arrayAdapter);
		lvHistoryList.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();

	}
}
