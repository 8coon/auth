

const DEFAULT_TIMEOUT = 100;


export default (func, timeout = DEFAULT_TIMEOUT) => {
    if (func._debounced) {
        return;
    }

    func._debounced = true;

    window.setTimeout(() => {
        func._debounced = false;
        func();
    }, timeout);
};
