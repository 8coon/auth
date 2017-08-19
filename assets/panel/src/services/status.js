
// Emitter
import Emitter from 'minecraftshire-utils/src/emitter/Emitter';

// Models
import User from 'minecraftshire-jsapi/src/models/User/User';

// Methods
import statusUser from 'minecraftshire-jsapi/src/method/user/status';


const STATUS_MIN_INTERVAL = 1000;


function Status() {
    Emitter.apply(this);

    this.user = null;
    this.lastFetched = 0;

    this.reloadModel = this.reloadModel.bind(this);
}

Status.prototype = Object.create(Emitter.prototype);

Object.assign(Status.prototype, {

    EVT_STATUS_CHANGE: 'status',

    /**
     * Force fetch status
     * @param {Date} lastModified
     * @return {Promise}
     */
    fetch(lastModified = null) {
        return new Promise((resolve, reject) => {
            statusUser(lastModified)
                .then(user => {
                    this.lastFetched = Date.now();
                    this.user = user;

                    console.log('EMITTING', user);
                    this.$emit(this.EVT_STATUS_CHANGE, user);
                    resolve(user);
                })
                .catch(xhr => {
                    // Content not modified
                    if (xhr.status === 304) {
                        this.lastFetched = Date.now();
                        resolve(this.user);
                        return;
                    }

                    reject(xhr);
                })
        });
    },

    /**
     * Reload status
     * @param {boolean} forced
     * @return {Promise}
     */
    reload(forced = false) {
        if (forced || !this.user || Date.now() - this.lastFetched > STATUS_MIN_INTERVAL) {
            const lastModified = this.user && this.user.get('lastModified');
            return this.fetch(lastModified);
        }

        return Promise.resolve(this.user);
    },

    /**
     * Reload status and never reject, resolve with {model: User}
     * @param {boolean} forced
     */
    reloadModel(forced = false) {
        return new Promise(resolve => {
            this.reload(forced)
                .then(user => {
                    resolve({user});
                })
                .catch(() => {
                    resolve({});
                });
        });
    },

});

const status = new Status();
export default status;

// todo: Service worker
let timer;

const schedule = () => {
    timer = window.setTimeout(reload, 5000);
};

const reload = () => {
    window.clearTimeout(timer);
    status.reloadModel().then(schedule);
};

schedule();
