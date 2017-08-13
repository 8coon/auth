

export const getEventPath = (evt) => {
    if (evt.path) {
        return evt.path;
    }

    const path = [];
    let el = evt.target;

    while (el) {
        el && path.push(el);
        el = el.parentElement;
    }

    return path;
};
