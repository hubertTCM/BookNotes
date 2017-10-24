package com.hubert.parser.AST.Evaluation.Common;

import java.util.*;

public class Context {

    public Context() {
    }

    public Storage createStorage() {
        Storage storage = null;
        if (!mStorages.isEmpty()) {
            Storage parent = mStorages.peek();
            storage = new Storage(parent);
        } else {
            storage = new Storage();
        }

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

    private Stack<Storage> mStorages = new Stack<Storage>();
}
