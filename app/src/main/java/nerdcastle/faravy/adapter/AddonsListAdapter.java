package nerdcastle.faravy.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nerdcastle.faravy.activity.R;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

public class AddonsListAdapter extends ArrayAdapter<String> implements
		SectionIndexer {

	HashMap<String, Integer> mapIndex;
	String[] sections;
	List<String> fruits;

	public AddonsListAdapter(Context context, List<String> fruitList) {
		super(context, R.layout.addons_text, fruitList);

		this.fruits = fruitList;
		mapIndex = new LinkedHashMap<String, Integer>();

		for (int x = 0; x < fruits.size(); x++) {
			String fruit = fruits.get(x);
			String ch = fruit.substring(0, 1);
			ch = ch.toUpperCase(Locale.US);

			// HashMap will prevent duplicates
			if (!mapIndex.containsKey(ch)) {
				mapIndex.put(ch, x);
			}
			

		}

		Set<String> sectionLetters = mapIndex.keySet();

		// create a list from the set to sort
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

		Log.d("sectionList", sectionList.toString());
		Collections.sort(sectionList);

		sections = new String[sectionList.size()];

		sectionList.toArray(sections);
	}

	public int getPositionForSection(int section) {
		//Log.d("getPositionForSection section", "" + section);
		return mapIndex.get(sections[section]);
	}

	public int getSectionForPosition(int position) {
		//Log.d("getSectionForPosition position", "" + position);
		return 0;
	}

	public Object[] getSections() {
		return sections;
	}

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.addons_text, parent, false);
			holder.checkTV = (CheckedTextView) convertView
					.findViewById(android.R.id.text1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkTV.setText(Temp.getAddonsNameList().get(position));

		return convertView;
	}

	class ViewHolder {
		CheckedTextView checkTV;
	}*/

}
