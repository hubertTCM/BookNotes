export class Stack<T> {
  _store: T[] = [];
  push(val: T) {
    this._store.push(val);
  }
  pop(): T | undefined {
    return this._store.pop();
  }
  top(): T | undefined {
    if (!this._store.length) {
      return undefined;
    }
    return this._store[this._store.length - 1];
  }
  count(): number {
    return this._store.length;
  }
}
