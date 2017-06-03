package android.hubert.com.booknotes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.*;

import com.hubert.dal.entity.SectionEntity;

import java.util.Collection;
import java.util.List;


public class SectionAdapter extends ArrayAdapter<SectionEntity> {

    private LayoutInflater _inflater;
    private Context _context;

    public SectionAdapter(@NonNull Context context, Collection<SectionEntity> sections) {
        super(context, R.layout.section);
        addAll(sections);
        _inflater = LayoutInflater.from(context);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SectionEntity section = this.getItem(position);

        if(convertView == null){
            convertView = _inflater.inflate(R.layout.section, parent, false);
        }
       // ListView listViewBlock = (ListView) convertView.findViewById(R.id.listViewBlock);
       // BlockAdapter adapter = new BlockAdapter(this._context, section.blocks);
       // listViewBlock.setAdapter(adapter);

        TextView titleView = (TextView)convertView.findViewById(R.id.textViewTitle);
        titleView.setText(section.name);

        return convertView;
    }
}
