
// Application Root
const appRoot = window.location.hostname.startsWith('localhost') ? '' : '/app';


export default {
    root: `${appRoot}/`,
    login: `${appRoot}/login`,
    signup: `${appRoot}/signup`,
    restoreAccess: `${appRoot}/restore_access`,
    restoreSuccess: `${appRoot}/restore_success`,
    passwordReset: `${appRoot}/password_reset`,
    settings: `${appRoot}/settings`,
    notifications: `${appRoot}/notifications`,
};
