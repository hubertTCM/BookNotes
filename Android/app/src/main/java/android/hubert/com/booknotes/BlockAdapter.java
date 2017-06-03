package android.hubert.com.booknotes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.*;

import com.hubert.dal.entity.BlockEntity;

import java.util.Collection;

/**
 * Created by hubert.fu on 5/29/2017.
 */

public class BlockAdapter extends ArrayAdapter<BlockEntity> {

    public BlockAdapter(@NonNull Context context, Collection<BlockEntity> blocks) {
        super(context, R.layout.block);
        addAll(blocks);
    }
}
