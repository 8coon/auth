
// Models
import User from 'minecraftshire-jsapi/src/models/User/User';

// Methods
import statusUser from 'minecraftshire-jsapi/src/method/user/status';


const STATUS_MIN_INTERVAL = 1000;


export default class Status {

    static user = null;
    static lastFetched = 0;

    /**
     * Force fetch status
     * @param {Date} lastModified
     * @return {Promise}
     */
    static fetch(lastModified = null) {
        return new Promise((resolve, reject) => {
            statusUser(lastModified)
                .then(user => {
                    Status.lastFetched = Date.now();
                    Status.user = user;
                    resolve(user);
                })
                .catch(xhr => {
                    // Content not modified
                    if (xhr.status === 304) {
                        Status.lastFetched = Date.now();
                        resolve(Status.user);
                        return;
                    }

                    reject(xhr);
                })
        });
    }

    /**
     * Reload status
     * @param {boolean} forced
     * @return {Promise}
     */
    static reload(forced = false) {
        if (forced || !Status.user || Date.now() - Status.lastFetched > STATUS_MIN_INTERVAL) {
            const lastModified = Status.user && Status.user.get('lastModified');
            return Status.fetch(lastModified);
        }

        return Promise.resolve(Status.user);
    }

    /**
     * Reload status and never reject, resolve with {model: User}
     * @param {boolean} forced
     */
    static reloadModel(forced = false) {
        return new Promise(resolve => {
            Status.reload(forced)
                .then(user => {
                    resolve({user});
                })
                .catch(() => {
                    resolve({});
                });
        });
    }

}
