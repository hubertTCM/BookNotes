package android.hubert.com.booknotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.*;

import com.hubert.dal.entity.SectionEntity;

import java.util.Collection;


public class SectionAdapter extends ArrayAdapter<SectionEntity> {

    private LayoutInflater mInflater;
    private Context mContext;

    public SectionAdapter(@NonNull Context context, Collection<SectionEntity> sections) {
        super(context, R.layout.section);
        addAll(sections);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SectionEntity section = this.getItem(position);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.section, parent, false);
        }
       // ListView listViewBlock = (ListView) convertView.findViewById(R.id.listViewBlock);
       // BlockAdapter adapter = new BlockAdapter(this._context, section.blocks);
       // listViewBlock.setAdapter(adapter);

        TextView titleView = (TextView)convertView.findViewById(R.id.textViewTitle);
        titleView.setText(section.name);

        return convertView;
    }
}
