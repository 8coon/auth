
const USERNAME_KEY = 'username';


export default class Storage {

    static localStorage = window.localStorage;

    static getKeyName(key, username) {
        return `${username}:${key}`;
    }

    static serializeValue(value) {
        return value = typeof value === 'string' ? value : JSON.stringify(value);
    }

    static deserializeValue(value) {
        return value[0] === '{' ? JSON.parse(value) : value;
    }

    /**
     * Get current username
     * @return {string}
     */
    static getUsername() {
        return Storage.getForAll(USERNAME_KEY);
    }

    /**
     * Set current username
     * @param username
     */
    static setUsername(username) {
        Storage.setForAll(USERNAME_KEY, username);
    }

    /**
     * Write user-specific value to localStorage
     * @param {string} key
     * @param {string|object} value
     */
    static set(key, value) {
        value = Storage.serializeValue(value);
        const username = Storage.getUsername();
        Storage.localStorage.setItem(Storage.getKeyName(key, username), value);
    }

    /**
     * Write user-independent value to localStorage
     * @param key
     * @param value
     */
    static setForAll(key, value) {
        value = Storage.serializeValue(value);
        Storage.localStorage.setItem(key, value);
    }

    /**
     * Read user-specific value from localStorage
     * @param {string} key
     * @return {string|object}
     */
    static get(key) {
        const username = Storage.getUsername();
        return Storage.deserializeValue(Storage.localStorage.getItem(Storage.getKeyName(key, username)));
    }

    /**
     * Read user-independent value from localStorage
     * @param key
     * @return {string|object}
     */
    static getForAll(key) {
        return Storage.deserializeValue(Storage.localStorage.getItem(key));
    }

}
