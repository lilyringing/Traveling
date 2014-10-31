package com.example.traveling;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter{
	private Context context;
	List<Map<String, String>> groups;
	List<List<Map<String, String>>> childs;

/*
 * �c�y���:
 * �Ѽ�1:context����
 * �Ѽ�2:�@�ŲM���ƨӷ�
 * �Ѽ�3:�G�ŲM���ƨӷ�
 */
	public ExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs){
		this.groups = groups;
		this.childs = childs;
		this.context = context;
	}

	public Object getChild(int groupPosition, int childPosition){
		return childs.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition){
		return childPosition;
	}

	//����G�ŲM�檺View����
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
	
		@SuppressWarnings("unchecked")
		String text = ((Map<String, String>) getChild(groupPosition, childPosition)).get("child");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//����G�ŲM��������G����, �ñN��U�����]�m�������ݩ�
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.child, null);
		TextView tv = (TextView) linearLayout.findViewById(R.id.childtextView1);
		tv.setText(text);

		return linearLayout;
	}

	public int getChildrenCount(int groupPosition){
		return childs.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition){
		return groups.get(groupPosition);
	}

	public int getGroupCount(){
		return groups.size();
	}

	public long getGroupId(int groupPosition){
		return groupPosition;
	}

	//����@�ŲM��View����
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
		String text = groups.get(groupPosition).get("group");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//����@�ŲM��G����,�]�m���������ݩ�
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.group, null);
		TextView textView = (TextView)linearLayout.findViewById(R.id.grouptextView1);
		textView.setText(text);

		return linearLayout;
	}

	public boolean hasStableIds(){
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition){
		return true;
	}
}