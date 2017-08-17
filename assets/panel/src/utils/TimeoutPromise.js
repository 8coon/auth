
/**
 * @param {number} timeout
 * @param arg
 * @return {Promise}
 * @constructor
 */
export default function TimeoutPromise(timeout, arg = null) {
    return new Promise(resolve => {
        window.setTimeout(() => resolve(arg), timeout);
    });
}
