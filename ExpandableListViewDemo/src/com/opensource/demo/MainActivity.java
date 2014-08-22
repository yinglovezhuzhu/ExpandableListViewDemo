package com.opensource.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	

    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
	
	public LinearLayout			ll_top;
	private int					the_group_expand_position	= -1;
	private int					indicatorGroupHeight;
	private boolean				isExpanding					= false;

	private TextView			tv_citylist_province;
	private ExpandableListView	expandableListView;

	private Context				context;


    private ExpandableListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
			context = MainActivity.this;
			ll_top = (LinearLayout) findViewById(R.id.ll_citylist_top);
			ll_top.setVisibility(View.GONE);
			tv_citylist_province = (TextView) findViewById(R.id.tv_citylist_province);
			expandableListView = (ExpandableListView) findViewById(R.id.el_expandableListView);
			
			final List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	        final List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	        for (int i = 0; i < 20; i++) {
	            Map<String, String> curGroupMap = new HashMap<String, String>();
	            groupData.add(curGroupMap);
	            curGroupMap.put(NAME, "Group " + i);
	            curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd");
	            
	            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
	            for (int j = 0; j < 15; j++) {
	                Map<String, String> curChildMap = new HashMap<String, String>();
	                children.add(curChildMap);
	                curChildMap.put(NAME, "Child " + j);
	                curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even" : "This child is odd");
	            }
	            childData.add(children);
	        }

	     // Set up our adapter
	        mAdapter = new SimpleExpandableListAdapter(
	                this,
	                groupData,
	                android.R.layout.simple_expandable_list_item_1,
	                new String[] { NAME, IS_EVEN },
	                new int[] { android.R.id.text1, android.R.id.text2 },
	                childData,
	                android.R.layout.simple_expandable_list_item_2,
	                new String[] { NAME, IS_EVEN },
	                new int[] { android.R.id.text1, android.R.id.text2 }
	                );
	        expandableListView.setAdapter(mAdapter);
	        

			expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
				@Override
				public void onGroupExpand(int groupPosition) {
					the_group_expand_position = groupPosition;
					ll_top.setVisibility(View.VISIBLE);
					// lineView.setVisibility(View.VISIBLE);
					isExpanding = true;
					if (the_group_expand_position != -1) {
						tv_citylist_province.setText(groupData.get(the_group_expand_position).get(NAME) 
								+ "\n" + groupData.get(the_group_expand_position).get(IS_EVEN));
					}
				}

			});

			expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
				@Override
				public void onGroupCollapse(int groupPosition) {
					if (isExpanding) {
						ll_top.setVisibility(View.GONE);
						// lineView.setVisibility(View.GONE);
					}
					the_group_expand_position = -1;
					isExpanding = false;
				}

			});


			ll_top.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// linear.setVisibility(View.GONE);
					if (isExpanding) {
						ll_top.setVisibility(View.GONE);
						// lineView.setVisibility(View.GONE);
						expandableListView.collapseGroup(the_group_expand_position);
						isExpanding = false;
					}
				}

			});

			expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
					if (the_group_expand_position == -1) {
						expandableListView.expandGroup(groupPosition);
						expandableListView.setSelectedGroup(groupPosition);
						the_group_expand_position = groupPosition;
						ll_top.setVisibility(View.VISIBLE);
						// lineView.setVisibility(View.VISIBLE);
						isExpanding = true;
					}
					else if (the_group_expand_position == groupPosition) {
				
						ll_top.setVisibility(View.GONE);
						
						expandableListView.collapseGroup(groupPosition);
						the_group_expand_position = -1;
						isExpanding = false;
					}
					else {
						expandableListView.collapseGroup(the_group_expand_position);
						expandableListView.expandGroup(groupPosition);
						expandableListView.setSelectedGroup(groupPosition);
						the_group_expand_position = groupPosition;
					}
					return true;
				}
			});

		
			expandableListView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					
					int npos = view.pointToPosition(0, 0);
					if (npos != AdapterView.INVALID_POSITION) {
						long pos = expandableListView.getExpandableListPosition(npos);
						int childPos = ExpandableListView.getPackedPositionChild(pos);
						int groupPos = ExpandableListView.getPackedPositionGroup(pos);
						if (childPos == AdapterView.INVALID_POSITION) {
							View groupView = expandableListView.getChildAt(npos
									- expandableListView.getFirstVisiblePosition());
							indicatorGroupHeight = groupView.getHeight();
						}
						if (indicatorGroupHeight == 0) {
							return;
						}
						if (isExpanding) {
							if (the_group_expand_position != -1) {
								tv_citylist_province.setText(groupData.get(the_group_expand_position).get(NAME) 
										+ "\n" + groupData.get(the_group_expand_position).get(IS_EVEN));
//								tv_citylist_province.setText(list.get(the_group_expand_position).getInitial() + "  "
//										+ list.get(the_group_expand_position).getProvince());
							}
							
							if (the_group_expand_position != groupPos) {
								ll_top.setVisibility(View.GONE);
							}
							else {
								ll_top.setVisibility(View.VISIBLE);
							}
						}
					}

					if (the_group_expand_position == -1) {
						return;
					}


					int showHeight = t();

					MarginLayoutParams layoutParams = (MarginLayoutParams) ll_top.getLayoutParams();

					layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
				}

			});

		}

		private int t() {
			int showHeight = indicatorGroupHeight;
			int nEndPos = expandableListView.pointToPosition(0, indicatorGroupHeight);
			if (nEndPos != AdapterView.INVALID_POSITION) {
				long pos = expandableListView.getExpandableListPosition(nEndPos);
				int groupPos = ExpandableListView.getPackedPositionGroup(pos);
				if (groupPos != the_group_expand_position) {
					View viewNext = expandableListView.getChildAt(nEndPos - expandableListView.getFirstVisiblePosition());
					showHeight = viewNext.getTop();
				}
			}
			return showHeight;
		}

}
