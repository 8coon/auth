

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


/**
 * Get difference between two dates in days
 * @param {Date} a
 * @param {Date} b
 * @return {number}
 */
export const dateDiffInDays = (a, b) => {
    const MS_PER_DAY = 1000 * 60 * 60 * 24;

    const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
    const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

    return Math.floor((utc2 - utc1) / MS_PER_DAY);
};


/**
 * Get localized month name by id
 * @param {number|string} month
 * @return {string}
 */
export const formatMonth = (month) => {
    const months = [
        'Января',
        'Февраля',
        'Марта',
        'Апреля',
        'Мая',
        'Июня',
        'Июля',
        'Августа',
        'Сентября',
        'Октября',
        'Ноября',
        'Декабря',
    ];

    month = typeof month !== 'number' ? parseInt(month, 10) : month;
    return months[month - 1];
};

/**
 * Get localized date string
 * @param {Date} date
 * @return {string}
 */
export const formatDate = (date) => {
    let result = date.toISOString();

    result = result.replace(' ', 'T').replace('.', '-').replace('/', '-')
        .substring(0, result.indexOf('T')).split('-');

    const year = result[0];
    const month = formatMonth(result[1]);
    const day = parseInt(result[2], 10);

    return `${day} ${month} ${year}`;
};

/**
 * Remove location trailing slash
 * @param {string} location
 * @return {string}
 */
export const trimLocation = (location) => {
    if (location.startsWith('/')) location = location.substring(1);
    if (location.endsWith('/')) location = location.substring(0, location.length - 1);
    return `/${location}`;
};
