package com.hubert.parser.AST.Evaluation.Common;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

public class Context {

    public Context() {
        sGlobalIndex += 1;
        Storage root = new Storage(getGlobalPrefix());
        mStorages.push(root);
    }

    public Storage createStorage() {
        Storage storage = null;
            Storage parent = mStorages.peek();
            storage = new Storage(parent);

        return mStorages.push(storage);
    }

    public Storage getActiveStorage() {
        return mStorages.peek();
    }

    public void remove() {
        if (mStorages.isEmpty()) {
            System.out.println("error happens");
        }

        Storage scope = mStorages.pop();
        scope.destroy();
    }

    public <T> T setGlobalData(String key, T data) {
        Storage root = mStorages.get(0);
        String globalKey = root.convertToGlobalKey(key);
        root.setVariable(globalKey, data);
        return data;
    }
    
    private String getGlobalPrefix(){
        return Integer.toString(sGlobalIndex);
    }

    private Stack<Storage> mStorages = new Stack<Storage>();
    
    private static int sGlobalIndex = 0;
}
